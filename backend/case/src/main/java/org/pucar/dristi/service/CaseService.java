package org.pucar.dristi.service;

import static org.pucar.dristi.config.ServiceConstants.*;
import static org.pucar.dristi.enrichment.CaseRegistrationEnrichment.enrichLitigantsOnCreateAndUpdate;
import static org.pucar.dristi.enrichment.CaseRegistrationEnrichment.enrichRepresentativesOnCreateAndUpdate;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.jetbrains.annotations.NotNull;
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
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class CaseService {

    private CaseRegistrationValidator validator;

    private CaseRegistrationEnrichment enrichmentUtil;

    private CaseRepository caseRepository;

    private WorkflowService workflowService;

    private Configuration config;

    private Producer producer;

    private BillingUtil billingUtil;


    @Autowired
    public CaseService(CaseRegistrationValidator validator, CaseRegistrationEnrichment enrichmentUtil, CaseRepository caseRepository, WorkflowService workflowService, Configuration config, Producer producer, BillingUtil billingUtil) {
        this.validator = validator;
        this.enrichmentUtil = enrichmentUtil;
        this.caseRepository = caseRepository;
        this.workflowService = workflowService;
        this.config = config;
        this.producer = producer;
        this.billingUtil = billingUtil;
    }

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
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while creating case :: {}", e.toString());
            throw new CustomException(CREATE_CASE_ERR, e.getMessage());
        }
    }

    public void searchCases(CaseSearchRequest caseSearchRequests) {

        try {
            // Fetch applications from database according to the given search criteria
            caseRepository.getCases(caseSearchRequests.getCriteria(), caseSearchRequests.getRequestInfo());

            // If no applications are found matching the given criteria, return an empty list
            for (CaseCriteria searchCriteria : caseSearchRequests.getCriteria()) {
                searchCriteria.getResponseList().forEach(cases -> cases.setWorkflow(workflowService.getWorkflowFromProcessInstance(workflowService.getCurrentWorkflow(caseSearchRequests.getRequestInfo(), cases.getTenantId(), cases.getFilingNumber()))));
            }
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching to search results :: {}", e.toString());
            throw new CustomException(SEARCH_CASE_ERR, e.getMessage());
        }
    }

    public CourtCase updateCase(CaseRequest caseRequest) {

        try {
            // Validate whether the application that is being requested for update indeed exists
            if (!validator.validateUpdateRequest(caseRequest))
                throw new CustomException(VALIDATION_ERR, "Case Application does not exist");

            // Enrich application upon update
            enrichmentUtil.enrichCaseApplicationUponUpdate(caseRequest);

            workflowService.updateWorkflowStatus(caseRequest);

            if (CREATE_DEMAND_STATUS.equals(caseRequest.getCases().getStatus())) {
                billingUtil.createDemand(caseRequest);
            }
            if (CASE_ADMIT_STATUS.equals(caseRequest.getCases().getStatus())) {
                enrichmentUtil.enrichAccessCode(caseRequest);
                enrichmentUtil.enrichCaseNumberAndCNRNumber(caseRequest);
                enrichmentUtil.enrichRegistrationDate(caseRequest);
            }

            producer.push(config.getCaseUpdateTopic(), caseRequest);

            return caseRequest.getCases();

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating case :: {}", e.toString());
            throw new CustomException(UPDATE_CASE_ERR, "Exception occurred while updating case: " + e.getMessage());
        }

    }

    public List<CaseExists> existCases(CaseExistsRequest caseExistsRequest) {
        try {
            // Fetch applications from database according to the given search criteria
            return caseRepository.checkCaseExists(caseExistsRequest.getCriteria());
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching to exist case :: {}", e.toString());
            throw new CustomException(CASE_EXIST_ERR, e.getMessage());
        }
    }

    public AddWitnessResponse addWitness(AddWitnessRequest addWitnessRequest) {

        try {
            String filingNumber = addWitnessRequest.getCaseFilingNumber();
            CaseExists caseExists = CaseExists.builder().filingNumber(filingNumber).build();
            List<CaseExists> caseExistsList = caseRepository.checkCaseExists(Collections.singletonList(caseExists));

            if (!caseExistsList.get(0).getExists())
                throw new CustomException(INVALID_CASE, "No case found for the given filling Number");

            if (addWitnessRequest.getAdditionalDetails() == null)
                throw new CustomException(VALIDATION_ERR, "Additional details are required");

            RequestInfo requestInfo = addWitnessRequest.getRequestInfo();
            User userInfo = requestInfo.getUserInfo();
            String userType = userInfo.getType();
            if (!EMPLOYEE.equalsIgnoreCase(userType) || userInfo.getRoles().stream().filter(role -> EMPLOYEE.equalsIgnoreCase(role.getName())).findFirst().isEmpty())
                throw new CustomException(VALIDATION_ERR, "Not a valid user to add witness details");

            AuditDetails auditDetails = AuditDetails.builder().lastModifiedBy(addWitnessRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
            addWitnessRequest.setAuditDetails(auditDetails);
            producer.push(config.getAdditionalJoinCaseTopic(), addWitnessRequest);

            return AddWitnessResponse.builder().addWitnessRequest(addWitnessRequest).build();

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while adding witness to the case :: {}", e.toString());
            throw new CustomException(ADD_WITNESS_TO_CASE_ERR, "Exception occurred while adding witness to case: " + e.getMessage());
        }

    }

    private void verifyAndEnrichLitigant(JoinCaseRequest joinCaseRequest, CourtCase caseObj, AuditDetails auditDetails) {
        log.info("enriching litigants");
        enrichLitigantsOnCreateAndUpdate(caseObj, auditDetails);

        log.info("Pushing join case litigant details :: {}", joinCaseRequest.getLitigant());
        producer.push(config.getLitigantJoinCaseTopic(), joinCaseRequest.getLitigant());

        if (joinCaseRequest.getAdditionalDetails() != null) {
            log.info("Pushing additional details for litigant:: {}", joinCaseRequest.getAdditionalDetails());
            producer.push(config.getAdditionalJoinCaseTopic(), joinCaseRequest);
        }
    }

    private void verifyAndEnrichRepresentative(JoinCaseRequest joinCaseRequest, CourtCase caseObj, AuditDetails auditDetails) {
        log.info("enriching representatives");
        enrichRepresentativesOnCreateAndUpdate(caseObj, auditDetails);

        log.info("Pushing join case representative details :: {}", joinCaseRequest.getRepresentative());
        producer.push(config.getRepresentativeJoinCaseTopic(), joinCaseRequest.getRepresentative());

        if (joinCaseRequest.getAdditionalDetails() != null) {
            log.info("Pushing additional details :: {}", joinCaseRequest.getAdditionalDetails());
            producer.push(config.getAdditionalJoinCaseTopic(), joinCaseRequest);
        }
    }

    public JoinCaseResponse verifyJoinCaseRequest(JoinCaseRequest joinCaseRequest) {
        try {
            String filingNumber = joinCaseRequest.getCaseFilingNumber();
            List<CaseCriteria> existingApplications = caseRepository.getCases(Collections.singletonList(CaseCriteria.builder().filingNumber(filingNumber).build()), joinCaseRequest.getRequestInfo());
            log.info("Existing application list :: {}", existingApplications.size());
            CourtCase courtCase = validateAccessCodeAndReturnCourtCase(joinCaseRequest, existingApplications);
            UUID caseId = courtCase.getId();


            AuditDetails auditDetails = AuditDetails.builder()
                    .createdBy(joinCaseRequest.getRequestInfo().getUserInfo().getUuid())
                    .createdTime(System.currentTimeMillis())
                    .lastModifiedBy(joinCaseRequest.getRequestInfo().getUserInfo().getUuid())
                    .lastModifiedTime(System.currentTimeMillis()).build();
            joinCaseRequest.setAuditDetails(auditDetails);

            CourtCase caseObj = CourtCase.builder()
                    .id(caseId)
                    .build();

            verifyLitigantsAndJoinCase(joinCaseRequest, courtCase, caseObj, auditDetails);

            verifyRepresentativesAndJoinCase(joinCaseRequest, courtCase, caseObj, auditDetails);

            return JoinCaseResponse.builder().joinCaseRequest(joinCaseRequest).build();

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Invalid request for joining a case :: {}", e.toString());
            throw new CustomException(JOIN_CASE_ERR, JOIN_CASE_INVALID_REQUEST);
        }
    }

    private void verifyRepresentativesAndJoinCase(JoinCaseRequest joinCaseRequest, CourtCase courtCase, CourtCase caseObj, AuditDetails auditDetails) {
        //for representative to join a case
        if (joinCaseRequest.getRepresentative() != null) {

            if (!validator.canRepresentativeJoinCase(joinCaseRequest))
                throw new CustomException(VALIDATION_ERR, JOIN_CASE_INVALID_REQUEST);

            // Stream over the representatives to create a list of advocateIds
            List<String> advocateIds = Optional.ofNullable(courtCase.getRepresentatives())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(AdvocateMapping::getAdvocateId)
                    .toList();

            //Setting representative ID as null to resolve later as per need
            joinCaseRequest.getRepresentative().setId(null);

            //when advocate is part of the case
            //Scenario 1 -> If advocate is already representing the individual throw error
            //Scenario 2 -> If individual exists and advocate don't represent the individual then add the representing to the advocate and disable from existing one when relation is primary
            //Scenario 3 -> If individual doesn't exist then add him to the respective advocate

            advocatePartOfCaseHandler(advocateIds, joinCaseRequest, courtCase, auditDetails);

            //when advocate is not the part of the case
            //Scenario 1 -> If individual exist then replace other advocate when relation is primary
            //Scenario 2 -> If individual doesn't exist then add advocate

            if (!advocateIds.isEmpty() && joinCaseRequest.getRepresentative().getAdvocateId() != null && !advocateIds.contains(joinCaseRequest.getRepresentative().getAdvocateId())) {
                String joinCasePartyIndividualID = joinCaseRequest.getRepresentative().getRepresenting().get(0).getIndividualId();
                disableExistingRepresenting(courtCase, joinCasePartyIndividualID, auditDetails);
            }

            caseObj.setRepresentatives(Collections.singletonList(joinCaseRequest.getRepresentative()));
            verifyAndEnrichRepresentative(joinCaseRequest, caseObj, auditDetails);
        }
    }

    private  void advocatePartOfCaseHandler(List<String> advocateIds , JoinCaseRequest joinCaseRequest, CourtCase courtCase, AuditDetails auditDetails){
        if (!advocateIds.isEmpty() && joinCaseRequest.getRepresentative().getAdvocateId() != null &&
                advocateIds.contains(joinCaseRequest.getRepresentative().getAdvocateId())) {

            Optional<AdvocateMapping> existingRepresentativeOptional = courtCase.getRepresentatives().stream()
                    .filter(advocateMapping -> joinCaseRequest.getRepresentative().getAdvocateId().equals(advocateMapping.getAdvocateId()))
                    .findFirst();

            if (existingRepresentativeOptional.isEmpty())
                throw new CustomException(INVALID_ADVOCATE_ID, INVALID_ADVOCATE_DETAILS);

            AdvocateMapping existingRepresentative = existingRepresentativeOptional.get();
            List<String> individualIds = existingRepresentative.getRepresenting().stream()
                    .map(Party::getIndividualId)
                    .toList();

            log.info("Advocate is part of the case :: {}", existingRepresentative);
            String joinCasePartyIndividualID = joinCaseRequest.getRepresentative().getRepresenting().get(0).getIndividualId();

            if (joinCasePartyIndividualID != null &&
                    individualIds.contains(joinCasePartyIndividualID)) {
                log.info("Advocate is already representing the individual");
                throw new CustomException(VALIDATION_ERR, "Advocate is already a part of the given case");
            } else {
                log.info("Advocate is not representing the individual");
                disableExistingRepresenting(courtCase, joinCasePartyIndividualID, auditDetails);
                joinCaseRequest.getRepresentative().setId(existingRepresentative.getId());
            }
        }
    }

    private void disableExistingRepresenting(CourtCase courtCase, String joinCasePartyIndividualID, AuditDetails auditDetails) {
        courtCase.getRepresentatives().forEach(representative ->
                representative.getRepresenting().forEach(party -> {

                    //For getting the representing of the representative by the individualID for whom representative is primary
                    if (party.getIndividualId().equals(joinCasePartyIndividualID) && (COMPLAINANT_PRIMARY.equalsIgnoreCase(party.getPartyType()) || RESPONDENT_PRIMARY.equalsIgnoreCase(party.getPartyType()))) {
                        log.info("Setting isActive false for the existing individual :: {}", party);
                        party.setIsActive(false);
                        party.getAuditDetails().setLastModifiedTime(auditDetails.getLastModifiedTime());
                        party.getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());

                        if (representative.getRepresenting().size() == 1) {
                            log.info("Setting isActive false for the representative if he is only representing the above party :: {}", representative);
                            representative.setIsActive(false);
                            representative.getAuditDetails().setLastModifiedTime(auditDetails.getLastModifiedTime());
                            representative.getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());
                        }
                        producer.push(config.getUpdateRepresentativeJoinCaseTopic(), representative);
                    }
                })
        );
    }

    private void verifyLitigantsAndJoinCase(JoinCaseRequest joinCaseRequest, CourtCase courtCase, CourtCase caseObj, AuditDetails auditDetails) {
        if (joinCaseRequest.getLitigant() != null) { //for litigant to join a case
            // Stream over the litigants to create a list of individualIds
            if (!validator.canLitigantJoinCase(joinCaseRequest))
                throw new CustomException(VALIDATION_ERR, JOIN_CASE_INVALID_REQUEST);

            List<String> individualIds = courtCase.getLitigants().stream()
                    .map(Party::getIndividualId)
                    .toList();
            if (joinCaseRequest.getLitigant().getIndividualId() != null &&
                    individualIds.contains(joinCaseRequest.getLitigant().getIndividualId())) {
                throw new CustomException(VALIDATION_ERR, "Litigant is already a part of the given case");
            }
            caseObj.setLitigants(Collections.singletonList(joinCaseRequest.getLitigant()));
            verifyAndEnrichLitigant(joinCaseRequest, caseObj, auditDetails);
        }
    }

    private static @NotNull CourtCase validateAccessCodeAndReturnCourtCase(JoinCaseRequest joinCaseRequest, List<CaseCriteria> existingApplications) {
        if (existingApplications.isEmpty()) {
            throw new CustomException(CASE_EXIST_ERR, "Case does not exist");
        }
        List<CourtCase> courtCaseList = existingApplications.get(0).getResponseList();
        if (courtCaseList.isEmpty()) {
            throw new CustomException(CASE_EXIST_ERR, "Case does not exist");
        }

        CourtCase courtCase = courtCaseList.get(0);

        if (courtCase.getAccessCode() == null || courtCase.getAccessCode().isEmpty()) {
            throw new CustomException(VALIDATION_ERR, "Access code not generated");
        }
        String caseAccessCode = courtCase.getAccessCode();

        if (!joinCaseRequest.getAccessCode().equalsIgnoreCase(caseAccessCode)) {
            throw new CustomException(VALIDATION_ERR, "Invalid access code");
        }
        return courtCase;
    }
}