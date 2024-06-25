package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.CaseRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.util.BillingUtil;
import org.pucar.dristi.validators.CaseRegistrationValidator;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;
import static org.pucar.dristi.enrichment.CaseRegistrationEnrichment.enrichLitigantsOnCreateAndUpdate;
import static org.pucar.dristi.enrichment.CaseRegistrationEnrichment.enrichRepresentativesOnCreateAndUpdate;


@Service
@Slf4j
public class CaseService {

    private CaseRegistrationValidator validator;

    @Autowired
    private CaseRegistrationEnrichment enrichmentUtil;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private Configuration config;

    @Autowired
    private Producer producer;

    @Autowired
    private BillingUtil billingUtil;

    @Autowired
    public void setValidator(@Lazy CaseRegistrationValidator validator) {
        this.validator = validator;
    }

    public CourtCase createCase(CaseRequest body) {
        try {
            validator.validateCaseRegistration(body);

            enrichmentUtil.enrichCaseRegistrationOnCreate(body);

            workflowService.updateWorkflowStatus(body);

            producer.push(config.getCaseCreateTopic(), body);
            return body.getCases();
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while creating case :: {}", e.toString());
            throw new CustomException(CREATE_CASE_ERR, e.getMessage());
        }
    }

    public void searchCases(CaseSearchRequest caseSearchRequests) {

        try {
            // Fetch applications from database according to the given search criteria
            caseRepository.getApplications(caseSearchRequests.getCriteria(), caseSearchRequests.getRequestInfo());

            // If no applications are found matching the given criteria, return an empty list
            for (CaseCriteria searchCriteria : caseSearchRequests.getCriteria()){
                searchCriteria.getResponseList().forEach(cases -> cases.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(caseSearchRequests.getRequestInfo(), cases.getTenantId(), cases.getCaseNumber()))));
            }
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching to search results :: {}",e.toString());
            throw new CustomException(SEARCH_CASE_ERR, e.getMessage());
        }
    }

    public CourtCase updateCase(CaseRequest caseRequest) {

        try {
            // Validate whether the application that is being requested for update indeed exists
            if (!validator.validateApplicationExistence(caseRequest))
                throw new CustomException(VALIDATION_ERR, "Case Application does not exist");

            // Enrich application upon update
            enrichmentUtil.enrichCaseApplicationUponUpdate(caseRequest);

            workflowService.updateWorkflowStatus(caseRequest);

            if (CREATE_DEMAND_STATUS.equals(caseRequest.getCases().getStatus())){
                billingUtil.createDemand(caseRequest);
            }
            if (CASE_ADMIT_STATUS.equals(caseRequest.getCases().getStatus())) {
                enrichmentUtil.enrichAccessCode(caseRequest);
                enrichmentUtil.enrichCaseNumberAndCNRNumber(caseRequest);
            }
            producer.push(config.getCaseUpdateTopic(), caseRequest);

            return caseRequest.getCases();

        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating case :: {}",e.toString());
            throw new CustomException(UPDATE_CASE_ERR, "Exception occurred while updating case: " + e.getMessage());
        }

    }

    public List<CaseExists> existCases(CaseExistsRequest caseExistsRequest) {
        try {
            // Fetch applications from database according to the given search criteria
            return caseRepository.checkCaseExists(caseExistsRequest.getCriteria());
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching to exist case :: {}",e.toString());
            throw new CustomException(CASE_EXIST_ERR, e.getMessage());
        }
    }

    private void verifyAndEnrichLitigant(JoinCaseRequest joinCaseRequest, CourtCase caseObj, AuditDetails auditDetails) {
            if (!validator.validateLitigantJoinCase(joinCaseRequest))
                throw new CustomException(VALIDATION_ERR, "Invalid request for joining a case");

            log.info("enriching litigants");
            enrichLitigantsOnCreateAndUpdate(caseObj, auditDetails);

            producer.push(config.getLitigantJoinCaseTopic(), joinCaseRequest.getLitigant());
    }

    private void verifyAndEnrichRepresentative(JoinCaseRequest joinCaseRequest, CourtCase caseObj, AuditDetails auditDetails) {
            if (!validator.validateRepresentativeJoinCase(joinCaseRequest))
                throw new CustomException(VALIDATION_ERR, "Invalid request for joining a case");

            log.info("enriching representatives");
            enrichRepresentativesOnCreateAndUpdate(caseObj, auditDetails);

            producer.push(config.getRepresentativeJoinCaseTopic(), joinCaseRequest.getRepresentative());
    }

    public JoinCaseResponse verifyJoinCaseRequest(JoinCaseRequest joinCaseRequest) {
        try {
            String filingNumber = joinCaseRequest.getCaseFilingNumber();
            List<CaseCriteria> existingApplications = caseRepository.getApplications(Collections.singletonList(CaseCriteria.builder().filingNumber(filingNumber).build()));
            List<CourtCase> courtCaseList = existingApplications.get(0).getResponseList();
            if (courtCaseList.isEmpty()) {
                throw new CustomException(CASE_EXIST_ERR, "Case does not exist");
            }

            CourtCase courtCase = courtCaseList.get(0);
            UUID caseId = courtCase.getId();

            if (!CASE_ADMIT_STATUS.equals(courtCase.getStatus())) {
                throw new CustomException(VALIDATION_ERR, "Case must be in CASE_ADMITTED state");
            }
            String caseAccessCode = courtCase.getAccessCode();

            if(!joinCaseRequest.getAccessCode().equalsIgnoreCase(caseAccessCode)) {
                throw new CustomException(VALIDATION_ERR, "Invalid access code");
            }

            AuditDetails auditDetails = AuditDetails.builder()
                    .createdBy(joinCaseRequest.getRequestInfo().getUserInfo().getUuid())
                    .createdTime(System.currentTimeMillis())
                    .lastModifiedBy(joinCaseRequest.getRequestInfo().getUserInfo().getUuid())
                    .lastModifiedTime(System.currentTimeMillis()).build();

            CourtCase caseObj = CourtCase.builder()
                    .id(caseId)
                    .build();

            if(joinCaseRequest.getLitigant() != null) {
                // Stream over the litigants to create a list of individualIds
                List<String> individualIds = courtCase.getLitigants().stream()
                        .map(Party::getIndividualId)
                        .toList();
                if(joinCaseRequest.getLitigant().getIndividualId() != null &&
                        individualIds.contains(joinCaseRequest.getLitigant().getIndividualId())){
                    throw new CustomException(VALIDATION_ERR, "Litigant is already a part of the given case");
                }
                caseObj.setLitigants(Collections.singletonList(joinCaseRequest.getLitigant()));
                verifyAndEnrichLitigant(joinCaseRequest, caseObj, auditDetails);
            }

            if(joinCaseRequest.getRepresentative() != null) {
                // Stream over the representatives to create a list of advocateIds
                List<String> advocateIds = courtCase.getRepresentatives().stream()
                        .map(AdvocateMapping::getAdvocateId)
                        .toList();
                if(joinCaseRequest.getRepresentative().getAdvocateId() != null &&
                        advocateIds.contains(joinCaseRequest.getRepresentative().getAdvocateId())){
                    throw new CustomException(VALIDATION_ERR, "Advocate is already a part of the given case");
                }
                caseObj.setRepresentatives(Collections.singletonList(joinCaseRequest.getRepresentative()));
                verifyAndEnrichRepresentative(joinCaseRequest, caseObj, auditDetails);
            }

            return JoinCaseResponse.builder()
                    .accessCode(caseAccessCode)
                    .caseFilingNumber(filingNumber)
                    .representative(joinCaseRequest.getRepresentative())
                    .litigant(joinCaseRequest.getLitigant()).build();

        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Invalid request for joining a case :: {}",e.toString());
            throw new CustomException(JOIN_CASE_ERR, "Invalid request for joining a case");
        }
    }
}