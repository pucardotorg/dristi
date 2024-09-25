package org.pucar.dristi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
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
import org.pucar.dristi.util.EncryptionDecryptionUtil;
import org.pucar.dristi.validators.CaseRegistrationValidator;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private final ObjectMapper objectMapper;
    private final CacheService cacheService;


    @Autowired
    public CaseService(@Lazy CaseRegistrationValidator validator,
                       CaseRegistrationEnrichment enrichmentUtil,
                       CaseRepository caseRepository,
                       WorkflowService workflowService,
                       Configuration config,
                       Producer producer,
                       BillingUtil billingUtil,
                       EncryptionDecryptionUtil encryptionDecryptionUtil,
                       ObjectMapper objectMapper, CacheService cacheService) {
        this.validator = validator;
        this.enrichmentUtil = enrichmentUtil;
        this.caseRepository = caseRepository;
        this.workflowService = workflowService;
        this.config = config;
        this.producer = producer;
        this.billingUtil = billingUtil;
        this.encryptionDecryptionUtil = encryptionDecryptionUtil;
        this.objectMapper = objectMapper;
        this.cacheService = cacheService;
    }


    public CourtCase createCase(CaseRequest body) {
        try {
            validator.validateCaseRegistration(body);

            enrichmentUtil.enrichCaseRegistrationOnCreate(body);

            workflowService.updateWorkflowStatus(body);

            body.setCases(encryptionDecryptionUtil.encryptObject(body.getCases(), config.getCourtCaseEncrypt(), CourtCase.class));

            cacheService.save(body.getCases().getTenantId() + ":" + body.getCases().getId().toString(), body.getCases());

            producer.push(config.getCaseCreateTopic(), body);

            CourtCase cases = encryptionDecryptionUtil.decryptObject(body.getCases(), config.getCaseDecryptSelf(), CourtCase.class, body.getRequestInfo());
            cases.setAccessCode(null);

            return cases;
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
            List<CaseCriteria> caseCriteriaList = caseSearchRequests.getCriteria();

            List<CaseCriteria> caseCriteriaInRedis = new ArrayList<>();

            for (CaseCriteria criteria : caseCriteriaList) {
                CourtCase courtCase = null;
                if (!criteria.getDefaultFields() && criteria.getCaseId() != null) {
                    courtCase = searchRedisCache(caseSearchRequests.getRequestInfo(), criteria);
                }
                if (courtCase != null) {
                    criteria.setResponseList(Collections.singletonList(courtCase));
                    caseCriteriaInRedis.add(criteria);
                }
            }

            if (!caseCriteriaInRedis.isEmpty()) {
                caseCriteriaList.removeAll(caseCriteriaInRedis);
            }
            List<CaseCriteria> casesList = caseRepository.getCases(caseSearchRequests.getCriteria(), caseSearchRequests.getRequestInfo());
            saveInRedisCache(casesList, caseSearchRequests.getRequestInfo());

            casesList.addAll(caseCriteriaInRedis);

            casesList.forEach(caseCriteria -> {
                List<CourtCase> decryptedCourtCases = new ArrayList<>();
                caseCriteria.getResponseList().forEach(cases -> {
                    decryptedCourtCases.add(encryptionDecryptionUtil.decryptObject(cases, CASE_DECRYPT_SELF, CourtCase.class, caseSearchRequests.getRequestInfo()));
                });
                caseCriteria.setResponseList(decryptedCourtCases);
            });

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

            if (CASE_ADMIT_STATUS.equals(caseRequest.getCases().getStatus())) {
                enrichmentUtil.enrichAccessCode(caseRequest);
                enrichmentUtil.enrichCourtCaseNumber(caseRequest);
                enrichmentUtil.enrichRegistrationDate(caseRequest);
                caseRequest.getCases().setCaseType(ST);
            }

            if (PENDING_ADMISSION_HEARING_STATUS.equals(caseRequest.getCases().getStatus())) {
                enrichmentUtil.enrichAccessCode(caseRequest);
                enrichmentUtil.enrichCNRNumber(caseRequest);
                enrichmentUtil.enrichCMPNumber(caseRequest);
                caseRequest.getCases().setCaseType(CMP);
            }

            log.info("Encrypting: {}", caseRequest);
            caseRequest.setCases(encryptionDecryptionUtil.encryptObject(caseRequest.getCases(), "CourtCase", CourtCase.class));
            cacheService.save(caseRequest.getCases().getTenantId() + ":" + caseRequest.getCases().getId(), caseRequest.getCases());

            producer.push(config.getCaseUpdateTopic(), caseRequest);

            CourtCase cases = encryptionDecryptionUtil.decryptObject(caseRequest.getCases(), null, CourtCase.class, caseRequest.getRequestInfo());
            cases.setAccessCode(null);

            return cases;


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
            CourtCase caseObj = CourtCase.builder()
                    .filingNumber(filingNumber)
                    .build();
            caseObj.setAdditionalDetails(addWitnessRequest.getAdditionalDetails());

            caseObj = encryptionDecryptionUtil.encryptObject(caseObj, config.getCourtCaseEncrypt(), CourtCase.class);

            addWitnessRequest.setAdditionalDetails(caseObj.getAdditionalDetails());
            producer.push(config.getAdditionalJoinCaseTopic(), addWitnessRequest);

            caseObj = encryptionDecryptionUtil.decryptObject(caseObj, config.getCaseDecryptSelf(),CourtCase.class,addWitnessRequest.getRequestInfo());

            addWitnessRequest.setAdditionalDetails(caseObj.getAdditionalDetails());
            return AddWitnessResponse.builder().addWitnessRequest(addWitnessRequest).build();

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while adding witness to the case :: {}", e.toString());
            throw new CustomException(ADD_WITNESS_TO_CASE_ERR, "Exception occurred while adding witness to case: " + e.getMessage());
        }

    }

    private void verifyAndEnrichLitigant(JoinCaseRequest joinCaseRequest, CourtCase courtCase,CourtCase caseObj, AuditDetails auditDetails) {
        log.info("enriching litigants");
        enrichLitigantsOnCreateAndUpdate(caseObj, auditDetails);

        log.info("Pushing join case litigant details :: {}", joinCaseRequest.getLitigant());
        producer.push(config.getLitigantJoinCaseTopic(), joinCaseRequest.getLitigant());

        if (joinCaseRequest.getAdditionalDetails() != null) {

            caseObj.setAdditionalDetails(editRespondantDetails(joinCaseRequest.getAdditionalDetails(),courtCase.getAdditionalDetails(),joinCaseRequest.getLitigant().getIndividualId()));
            courtCase.setAdditionalDetails(caseObj.getAdditionalDetails());
            caseObj = encryptionDecryptionUtil.encryptObject(caseObj, config.getCourtCaseEncrypt(), CourtCase.class);
            joinCaseRequest.setAdditionalDetails(caseObj.getAdditionalDetails());

            log.info("Pushing additional details for litigant:: {}", joinCaseRequest.getAdditionalDetails());
            producer.push(config.getAdditionalJoinCaseTopic(), joinCaseRequest);
            caseObj.setAuditdetails(courtCase.getAuditdetails());
            caseObj = encryptionDecryptionUtil.decryptObject(caseObj, config.getCaseDecryptSelf(),CourtCase.class,joinCaseRequest.getRequestInfo());
            joinCaseRequest.setAdditionalDetails(caseObj.getAdditionalDetails());

        }
    }

    private void verifyAndEnrichRepresentative(JoinCaseRequest joinCaseRequest, CourtCase courtCase, CourtCase caseObj, AuditDetails auditDetails) {
        log.info("enriching representatives");
        log.info("enriching representatives");
        enrichRepresentativesOnCreateAndUpdate(caseObj, auditDetails);

        log.info("Pushing join case representative details :: {}", joinCaseRequest.getRepresentative());
        producer.push(config.getRepresentativeJoinCaseTopic(), joinCaseRequest.getRepresentative());

        if (joinCaseRequest.getAdditionalDetails() != null) {
            caseObj.setAdditionalDetails(editAdvocateDetails(joinCaseRequest.getAdditionalDetails(),courtCase.getAdditionalDetails()));
            caseObj = encryptionDecryptionUtil.encryptObject(caseObj, config.getCourtCaseEncrypt(), CourtCase.class);
            joinCaseRequest.setAdditionalDetails(caseObj.getAdditionalDetails());
            log.info("Pushing additional details :: {}", joinCaseRequest.getAdditionalDetails());
            producer.push(config.getAdditionalJoinCaseTopic(), joinCaseRequest);
            caseObj.setAuditdetails(courtCase.getAuditdetails());
            caseObj = encryptionDecryptionUtil.decryptObject(caseObj, config.getCaseDecryptSelf(),CourtCase.class,joinCaseRequest.getRequestInfo());
            joinCaseRequest.setAdditionalDetails(caseObj.getAdditionalDetails());
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

            Object additionalDetails = joinCaseRequest.getAdditionalDetails();

            verifyLitigantsAndJoinCase(joinCaseRequest, courtCase, caseObj, auditDetails);

            if (joinCaseRequest.getRepresentative() != null) {

                joinCaseRequest.setAdditionalDetails(additionalDetails);

                verifyRepresentativesAndJoinCase(joinCaseRequest, courtCase, caseObj, auditDetails);
            }

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
        verifyAndEnrichRepresentative(joinCaseRequest, courtCase, caseObj, auditDetails);
    }

    private void advocatePartOfCaseHandler(List<String> advocateIds, JoinCaseRequest joinCaseRequest, CourtCase courtCase, AuditDetails auditDetails) {
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
            verifyAndEnrichLitigant(joinCaseRequest, courtCase, caseObj, auditDetails);
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

        CourtCase courtCase = encryptionDecryptionUtil.decryptObject(courtCaseList.get(0), config.getCaseDecryptSelf(), CourtCase.class,joinCaseRequest.getRequestInfo());

        if (courtCase.getAccessCode() == null || courtCase.getAccessCode().isEmpty()) {
            throw new CustomException(VALIDATION_ERR, "Access code not generated");
        }
        String caseAccessCode = courtCase.getAccessCode();

        if (!joinCaseRequest.getAccessCode().equalsIgnoreCase(caseAccessCode)) {
            throw new CustomException(VALIDATION_ERR, "Invalid access code");
        }
        return courtCase;
    }

    private String getRedisKey(RequestInfo requestInfo, String caseId) {
        return requestInfo.getUserInfo().getTenantId() + ":" + caseId;
    }

    public CourtCase searchRedisCache(RequestInfo requestInfo, CaseCriteria criteria) {
        try {
            Object value = cacheService.findById(getRedisKey(requestInfo, criteria.getCaseId()));
            if (value != null) {
                String caseObject = objectMapper.writeValueAsString(value);
                return objectMapper.readValue(caseObject, CourtCase.class);
            } else {
                return null;
            }
        } catch (JsonProcessingException e) {
            log.error("Error occurred while searching case in redis cache :: {}", e.toString());
            throw new CustomException(SEARCH_CASE_ERR, e.getMessage());
        }
    }

    public void saveInRedisCache(List<CaseCriteria> casesList, RequestInfo requestInfo) {
        for (CaseCriteria criteria : casesList) {
            if (criteria.getResponseList() != null && !criteria.getResponseList().isEmpty()) {
                for (CourtCase courtCase : criteria.getResponseList()) {
                    cacheService.save(requestInfo.getUserInfo().getTenantId() + ":" + courtCase.getId().toString(), courtCase);
                }
            }
        }
    }


    private void setOrRemoveField(ObjectNode sourceNode, ObjectNode targetNode, String fieldName) {
        if (sourceNode.has(fieldName)) {
            targetNode.set(fieldName, sourceNode.get(fieldName));
        } else {
            targetNode.remove(fieldName);
        }
    }

    private Object editAdvocateDetails(Object additionalDetails1, Object additionalDetails2) {
        // Convert the Objects to ObjectNodes for easier manipulation
        ObjectNode details1Node = objectMapper.convertValue(additionalDetails1, ObjectNode.class);
        ObjectNode details2Node = objectMapper.convertValue(additionalDetails2, ObjectNode.class);

        // Replace the specified field in additionalDetails2 with the value from additionalDetails1
        if (details1Node.has("advocateDetails")) {
            details2Node.set("advocateDetails", details1Node.get("advocateDetails"));
        } else {
            throw new CustomException(VALIDATION_ERR, "advocateDetails not found in additionalDetails object.");
        }

        // Convert the updated ObjectNode back to its original form
        return objectMapper.convertValue(details2Node, additionalDetails2.getClass());
    }


    private Object editRespondantDetails(Object additionalDetails1, Object additionalDetails2, String individualId) {
        // Convert the Objects to ObjectNodes for easier manipulation
        ObjectNode details1Node = objectMapper.convertValue(additionalDetails1, ObjectNode.class);
        ObjectNode details2Node = objectMapper.convertValue(additionalDetails2, ObjectNode.class);

        // Check if respondentDetails exists in both details1Node and details2Node
        if (details1Node.has("respondentDetails") && details2Node.has("respondentDetails")) {
            ObjectNode respondentDetails1 = (ObjectNode) details1Node.get("respondentDetails");
            ObjectNode respondentDetails2 = (ObjectNode) details2Node.get("respondentDetails");

            // Check if formdata exists and is an array in both respondentDetails
            if (respondentDetails1.has("formdata") && respondentDetails1.get("formdata").isArray()
                    && respondentDetails2.has("formdata") && respondentDetails2.get("formdata").isArray()) {
                ArrayNode formData1 = (ArrayNode) respondentDetails1.get("formdata");
                ArrayNode formData2 = (ArrayNode) respondentDetails2.get("formdata");

                // Iterate over formData in respondentDetails1 to find matching individualId and copy fields
                for (int i = 0; i < formData1.size(); i++) {
                    ObjectNode dataNode1 = (ObjectNode) formData1.get(i).path("data");
                    ObjectNode dataNode2 = (ObjectNode) formData2.get(i).path("data");
                    if(dataNode1.has("respondentVerification")){
                        JsonNode individualDetails1 = dataNode1.path("respondentVerification").path("individualDetails");
                        if (individualDetails1.has("individualId") && individualId.equals(individualDetails1.get("individualId").asText())) {
                            // Set or remove fields in dataNode2 based on dataNode1
                            setOrRemoveField(dataNode1, dataNode2, "respondentLastName");
                            setOrRemoveField(dataNode1, dataNode2, "respondentFirstName");
                            setOrRemoveField(dataNode1, dataNode2, "respondentMiddleName");
                            setOrRemoveField(dataNode1, dataNode2, "respondentVerification");
                            break;
                        }
                    }
                }
            } else {
                throw new CustomException(VALIDATION_ERR, "formdata is not found or is not an array in one of the respondentDetails objects.");
            }
        } else {
            throw new CustomException(VALIDATION_ERR, "respondentDetails not found in one of the additional details objects.");
        }

        // Convert the updated ObjectNode back to its original form
        return objectMapper.convertValue(details2Node, additionalDetails2.getClass());
    }
}