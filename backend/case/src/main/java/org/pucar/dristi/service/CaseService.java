package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.jetbrains.annotations.NotNull;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.CaseRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.util.BillingUtil;
import org.pucar.dristi.util.EncryptionDecryptionUtil;
import org.pucar.dristi.validators.CaseRegistrationValidator;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;
import static org.pucar.dristi.enrichment.CaseRegistrationEnrichment.enrichLitigantsOnCreateAndUpdate;
import static org.pucar.dristi.enrichment.CaseRegistrationEnrichment.enrichRepresentativesOnCreateAndUpdate;


@Service
@Slf4j
public class CaseService {

    private final CaseRegistrationValidator validator;
    private final CaseRegistrationEnrichment enrichmentUtil;
    private final CaseRepository caseRepository;
    private final WorkflowService workflowService;
    private final Configuration config;
    private final Producer producer;
    private final BillingUtil billingUtil;
    private final EncryptionDecryptionUtil encryptionDecryptionUtil;

    @Autowired
    public CaseService(@Lazy CaseRegistrationValidator validator,
                       CaseRegistrationEnrichment enrichmentUtil,
                       CaseRepository caseRepository,
                       WorkflowService workflowService,
                       Configuration config,
                       Producer producer,
                       BillingUtil billingUtil,
                       EncryptionDecryptionUtil encryptionDecryptionUtil) {      
        this.validator = validator;
        this.enrichmentUtil = enrichmentUtil;
        this.caseRepository = caseRepository;
        this.workflowService = workflowService;
        this.config = config;
        this.producer = producer;
        this.billingUtil = billingUtil;
        this.encryptionDecryptionUtil = encryptionDecryptionUtil;
    }

    public CourtCase createCase(CaseRequest body) {
        try {
            validator.validateCaseRegistration(body);

            enrichmentUtil.enrichCaseRegistrationOnCreate(body);

            workflowService.updateWorkflowStatus(body);

            body.setCases(encryptionDecryptionUtil.encryptObject(body.getCases(), "CourtCase", CourtCase.class));

            producer.push(config.getCaseCreateTopic(), body);

            CourtCase cases = encryptionDecryptionUtil.decryptObject(body.getCases(), "CaseDecryptSelf",CourtCase.class,body.getRequestInfo());
            cases.setAccessCode(null);

            return cases;
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

            caseSearchRequests.getCriteria().forEach(caseCriteria -> {
                List<CourtCase> decryptedCourtCases = new ArrayList<>();
                caseCriteria.getResponseList().forEach(cases -> {
                    cases.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(caseSearchRequests.getRequestInfo(), cases.getTenantId(), cases.getCaseNumber())));
                    decryptedCourtCases.add(encryptionDecryptionUtil.decryptObject(cases,null, CourtCase.class,caseSearchRequests.getRequestInfo()));
                });
                caseCriteria.setResponseList(decryptedCourtCases);
            });

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

            caseRequest.setCases(encryptionDecryptionUtil.encryptObject(caseRequest.getCases(), "CourtCase", CourtCase.class));

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
                throw new CustomException(VALIDATION_ERR, JOIN_CASE_INVALID_REQUEST);

            log.info("enriching litigants");
            enrichLitigantsOnCreateAndUpdate(caseObj, auditDetails);

            producer.push(config.getLitigantJoinCaseTopic(), joinCaseRequest.getLitigant());
    }

    private void verifyAndEnrichRepresentative(JoinCaseRequest joinCaseRequest, CourtCase caseObj, AuditDetails auditDetails) {
            if (!validator.validateRepresentativeJoinCase(joinCaseRequest))
                throw new CustomException(VALIDATION_ERR, JOIN_CASE_INVALID_REQUEST);

            log.info("enriching representatives");
            enrichRepresentativesOnCreateAndUpdate(caseObj, auditDetails);

            producer.push(config.getRepresentativeJoinCaseTopic(), joinCaseRequest.getRepresentative());
    }

    public JoinCaseResponse verifyJoinCaseRequest(JoinCaseRequest joinCaseRequest) {
        try {
            String filingNumber = joinCaseRequest.getCaseFilingNumber();
            List<CaseCriteria> existingApplications = caseRepository.getApplications(Collections.singletonList(CaseCriteria.builder().filingNumber(filingNumber).build()), joinCaseRequest.getRequestInfo());
            CourtCase courtCase = validateAccessCodeAndReturnCourtCase(joinCaseRequest, existingApplications);
            UUID caseId = courtCase.getId();


            AuditDetails auditDetails = AuditDetails.builder()
                    .createdBy(joinCaseRequest.getRequestInfo().getUserInfo().getUuid())
                    .createdTime(System.currentTimeMillis())
                    .lastModifiedBy(joinCaseRequest.getRequestInfo().getUserInfo().getUuid())
                    .lastModifiedTime(System.currentTimeMillis()).build();

            CourtCase caseObj = CourtCase.builder()
                    .id(caseId)
                    .build();

            verifyLitigantsAndJoinCase(joinCaseRequest, courtCase, caseObj, auditDetails);

            verifyRepresentativesAndJoinCase(joinCaseRequest, courtCase, caseObj, auditDetails);

            return JoinCaseResponse.builder()
                    .accessCode(joinCaseRequest.getAccessCode())
                    .caseFilingNumber(filingNumber)
                    .representative(joinCaseRequest.getRepresentative())
                    .litigant(joinCaseRequest.getLitigant()).build();

        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Invalid request for joining a case :: {}",e.toString());
            throw new CustomException(JOIN_CASE_ERR, JOIN_CASE_INVALID_REQUEST);
        }
    }

    private void verifyRepresentativesAndJoinCase(JoinCaseRequest joinCaseRequest, CourtCase courtCase, CourtCase caseObj, AuditDetails auditDetails) {
        if(joinCaseRequest.getRepresentative() != null) {
            //for representative to join a case
            // Stream over the representatives to create a list of advocateIds
            List<String> advocateIds = courtCase.getRepresentatives().stream()
                    .map(AdvocateMapping::getAdvocateId)
                    .toList();
            if(joinCaseRequest.getRepresentative().getAdvocateId() != null &&
                    advocateIds.contains(joinCaseRequest.getRepresentative().getAdvocateId())){

                Optional<AdvocateMapping> existingRepresentativeOptional = courtCase.getRepresentatives().stream()
                        .filter(advocateMapping -> joinCaseRequest.getRepresentative().getAdvocateId().equals(advocateMapping.getAdvocateId()))
                        .findFirst();

                if(existingRepresentativeOptional.isEmpty())
                    throw new CustomException(INVALID_ADVOCATE_ID, INVALID_ADVOCATE_DETAILS);

                AdvocateMapping existingRepresentative = existingRepresentativeOptional.get();
                List<String> individualIds = existingRepresentative.getRepresenting().stream()
                        .map(Party::getIndividualId)
                        .toList();

                if(joinCaseRequest.getRepresentative().getRepresenting().get(0).getIndividualId() != null &&
                    individualIds.contains(joinCaseRequest.getRepresentative().getRepresenting().get(0).getIndividualId())){
                    throw new CustomException(VALIDATION_ERR, "Advocate is already a part of the given case");
                } else{
                    joinCaseRequest.getRepresentative().setId(existingRepresentative.getId());
                }

            }
            caseObj.setRepresentatives(Collections.singletonList(joinCaseRequest.getRepresentative()));
            verifyAndEnrichRepresentative(joinCaseRequest, caseObj, auditDetails);
        }
    }

    private void verifyLitigantsAndJoinCase(JoinCaseRequest joinCaseRequest, CourtCase courtCase, CourtCase caseObj, AuditDetails auditDetails) {
        if(joinCaseRequest.getLitigant() != null) { //for litigant to join a case
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
    }

    private @NotNull CourtCase validateAccessCodeAndReturnCourtCase(JoinCaseRequest joinCaseRequest, List<CaseCriteria> existingApplications) {
        if (existingApplications.isEmpty()) {
            throw new CustomException(CASE_EXIST_ERR, "Case does not exist");
        }
        List<CourtCase> courtCaseList = existingApplications.get(0).getResponseList();
        if (courtCaseList.isEmpty()) {
            throw new CustomException(CASE_EXIST_ERR, "Case does not exist");
        }
        
        CourtCase courtCase = encryptionDecryptionUtil.decryptObject(courtCaseList.get(0),"CaseDecryptSelf", CourtCase.class,joinCaseRequest.getRequestInfo());

        if (courtCase.getAccessCode() == null || courtCase.getAccessCode().isEmpty()) {
            throw new CustomException(VALIDATION_ERR, "Access code not generated");
        }
        String caseAccessCode = courtCase.getAccessCode();

        if(!joinCaseRequest.getAccessCode().equalsIgnoreCase(caseAccessCode)) {
            throw new CustomException(VALIDATION_ERR, "Invalid access code");
        }
        return courtCase;
    }
}