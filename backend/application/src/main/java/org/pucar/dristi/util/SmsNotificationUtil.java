package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.service.SmsNotificationService;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.pucar.dristi.config.ServiceConstants.*;
import static org.pucar.dristi.config.ServiceConstants.COMPLAINANT;

@Component
@Slf4j
public class SmsNotificationUtil {

    private final CaseUtil caseUtil;
    private final ObjectMapper objectMapper;
    private final SmsNotificationService notificationService;
    private final IndividualService individualService;

    @Autowired
    public SmsNotificationUtil(CaseUtil caseUtil, ObjectMapper objectMapper, SmsNotificationService notificationService, IndividualService individualService) {
        this.caseUtil = caseUtil;
        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
        this.individualService = individualService;
    }

    public void callNotificationService(ApplicationRequest applicationRequest, String updatedState, String applicationType) {

        try {
            CaseSearchRequest caseSearchRequest = createCaseSearchRequest(applicationRequest.getRequestInfo(), applicationRequest.getApplication().getFilingNumber());
            JsonNode caseDetails = caseUtil.searchCaseDetails(caseSearchRequest);

            Object additionalDetailsObject = applicationRequest.getApplication().getAdditionalDetails();
            String jsonData = objectMapper.writeValueAsString(additionalDetailsObject);
            JsonNode additionalData = objectMapper.readTree(jsonData);

            String owner = additionalData.get("onBehalOfName").asText();
            String party = getPartyTypeByName(caseDetails.get("litigants"), owner);
            JsonNode formData = additionalData.path("formdata");

            boolean isVoluntarySubmission = null == applicationRequest.getApplication().getReferenceId();

            String messageCode = updatedState != null ? getMessageCode(applicationType, updatedState, isVoluntarySubmission) : null;
            assert messageCode != null;
            log.info("Message code: {}", messageCode);
            String[] smsTopics = messageCode.split(",");

            for(String smsTopic: smsTopics) {

                String receiver = getReceiverParty(smsTopic, party);

                Set<String> individualIds = extractIndividualIds(caseDetails, receiver);

                Set<String> phoneNumbers = callIndividualService(applicationRequest.getRequestInfo(), individualIds);

                SmsTemplateData smsTemplateData = SmsTemplateData.builder()
                        .courtCaseNumber(caseDetails.has("courtCaseNumber") ? caseDetails.get("courtCaseNumber").asText() : "")
                        .cmpNumber(caseDetails.has("cmpNumber") ? caseDetails.get("cmpNumber").asText() : "")
                        .applicationType(applicationType)
                        .originalHearingDate(formData.has("initialHearingDate") ? formData.get("initialHearingDate").asText() : "")
                        .reScheduledHearingDate(formData.has("changedHearingDate") ? formData.get("changedHearingDate").asText() : "")
                        .tenantId(applicationRequest.getApplication().getTenantId()).build();

                for (String number : phoneNumbers) {
                    notificationService.sendNotification(applicationRequest.getRequestInfo(), smsTemplateData, smsTopic, number);
                }
            }
        }
        catch (Exception e) {
            // Log the exception and continue the execution without throwing
            log.error("Error occurred while sending notification: {}", e.toString());
        }
    }

    public static String getPartyTypeByName(JsonNode litigants, String name) {
        for (JsonNode litigant : litigants) {
            JsonNode additionalDetails = litigant.get("additionalDetails");
            if (additionalDetails != null && additionalDetails.has("fullName")) {
                String fullName = additionalDetails.get("fullName").asText();
                if (name.equals(fullName)) {
                    return litigant.get("partyType").asText();
                }
            }
        }
        return null;
    }

    private CaseSearchRequest createCaseSearchRequest(RequestInfo requestInfo, String fillingNUmber) {
        CaseSearchRequest caseSearchRequest = new CaseSearchRequest();
        caseSearchRequest.setRequestInfo(requestInfo);
        CaseCriteria caseCriteria = CaseCriteria.builder().filingNumber(fillingNUmber).defaultFields(false).build();
        caseSearchRequest.addCriteriaItem(caseCriteria);
        return caseSearchRequest;
    }

    private String getMessageCode(String applicationType, String updatedStatus, boolean isVoluntarySubmission) {

        log.info("operation: getMessageCode, Application Type: {}, Updated Status: {}, Is Voluntary Submission: {}", applicationType, updatedStatus, isVoluntarySubmission);
        if(applicationType.equalsIgnoreCase(RE_SCHEDULE) && updatedStatus.equalsIgnoreCase(PENDINGREVIEW)){
            return RESCHEDULE_REQUEST_SUBMITTED;
        }
        if(applicationType.equalsIgnoreCase(RE_SCHEDULE) && updatedStatus.equalsIgnoreCase(REJECTED)){
            return RESCHEDULE_REQUEST_REJECTED;
        }
        if(applicationType.equalsIgnoreCase(RE_SCHEDULE) && updatedStatus.equalsIgnoreCase(COMPLETED)){
            return RESCHEDULE_REQUEST_ACCEPTED;
        }
        if(applicationType.equalsIgnoreCase(EXTENSION_SUBMISSION_DEADLINE) && updatedStatus.equalsIgnoreCase(PENDINGREVIEW)){
            return EXTENSION_SUBMISSION_DEADLINE_SUBMITTED;
        }
        if(applicationType.equalsIgnoreCase(EXTENSION_SUBMISSION_DEADLINE) && updatedStatus.equalsIgnoreCase(REJECTED)){
            return EXTENSION_SUBMISSION_DEADLINE_REJECTED;
        }
        if(applicationType.equalsIgnoreCase(EXTENSION_SUBMISSION_DEADLINE) && updatedStatus.equalsIgnoreCase(COMPLETED)){
            return EXTENSION_SUBMISSION_DEADLINE_ACCEPTED;
        }
        if(applicationType.equalsIgnoreCase(CHECKOUT_REQUEST) && updatedStatus.equalsIgnoreCase(REJECTED)){
            return CHECKOUT_REQUEST_REJECTED;
        }
        if(applicationType.equalsIgnoreCase(CHECKOUT_REQUEST) && updatedStatus.equalsIgnoreCase(COMPLETED)){
            return CHECKOUT_REQUEST_ACCEPTED;
        }
        if(!isVoluntarySubmission && applicationType.equalsIgnoreCase(PRODUCTION_DOCUMENTS) && updatedStatus.equalsIgnoreCase(COMPLETED)){
            return EVIDENCE_SUBMITTED;
        }
        if(!isVoluntarySubmission && applicationType.equalsIgnoreCase(PRODUCTION_DOCUMENTS) && updatedStatus.equalsIgnoreCase(PENDINGRESPONSE)){
            return RESPONSE_REQUIRED;
        }
        if(isVoluntarySubmission && !DEFINED_VOLUNTARY_SUBMISSIONS.contains(applicationType) && (updatedStatus.equalsIgnoreCase(PENDINGREVIEW) || updatedStatus.equalsIgnoreCase(PENDINGAPPROVAL))){
            if(applicationType.equalsIgnoreCase(OTHERS)) {
                return VOLUNTARY_SUBMISSION_SUBMITTED;
            } else {
                return VARIABLE_SUBMISSION_SUBMITTED;
            }
        }
        if(isVoluntarySubmission && !DEFINED_VOLUNTARY_SUBMISSIONS.contains(applicationType) && updatedStatus.equalsIgnoreCase(REJECTED)){
            if(applicationType.equalsIgnoreCase(OTHERS)) {
                return VOLUNTARY_SUBMISSION_REJECTED;
            } else {
                return VARIABLE_SUBMISSION_REJECTED;
            }
        }
        if(isVoluntarySubmission && !DEFINED_VOLUNTARY_SUBMISSIONS.contains(applicationType) && updatedStatus.equalsIgnoreCase(COMPLETED)){
            if(applicationType.equalsIgnoreCase(OTHERS)) {
                return VOLUNTARY_SUBMISSION_ACCEPTED;
            } else {
                return VARIABLE_SUBMISSION_ACCEPTED;
            }
        }
        return null;
    }

    private static String getReceiverParty(String messageCode, String party) {
        return switch (messageCode.toUpperCase()) {
            case RESCHEDULE_REQUEST_REJECTED_REQUESTING_PARTY, RESCHEDULE_REQUEST_ACCEPTED_REQUESTING_PARTY,
                 CHECKOUT_REQUEST_REJECTED, CHECKOUT_REQUEST_ACCEPTED ->
                    party.contains("respondent") ? RESPONDENT : COMPLAINANT;
            case RESCHEDULE_REQUEST_REJECTED_OPPONENT_PARTY, RESCHEDULE_REQUEST_ACCEPTED_OPPONENT_PARTY,
                 EVIDENCE_SUBMITTED, RESPONSE_REQUIRED ->
                    party.contains("respondent") ? COMPLAINANT : RESPONDENT;
            default -> null;
        };
    }

    private Set<String> callIndividualService(RequestInfo requestInfo, Set<String> ids) {

        Set<String> mobileNumber = new HashSet<>();

        List<Individual> individuals = individualService.getIndividualsBylId(requestInfo, new ArrayList<>(ids));
        for(Individual individual : individuals) {
            if (individual.getMobileNumber() != null) {
                mobileNumber.add(individual.getMobileNumber());
            }
        }

        return mobileNumber;
    }

    public  Set<String> extractIndividualIds(JsonNode caseDetails, String receiver) {
        Set<String> uuids = new HashSet<>();
        String partyTypeToMatch = (receiver != null) ? receiver.toLowerCase() : "";

        JsonNode litigantNode = caseDetails.get("litigants");
        if (litigantNode.isArray()) {
            for (JsonNode node : litigantNode) {
                String partyType = node.get("partyType").asText().toLowerCase();
                if (partyType.contains(partyTypeToMatch)) {
                    String uuid = node.path("additionalDetails").get("uuid").asText();
                    if (!uuid.isEmpty()) {
                        uuids.add(uuid);
                    }
                }
            }
        }
        JsonNode representativeNode = caseDetails.get("representatives");
        if (representativeNode.isArray()) {
            for (JsonNode advocateNode : representativeNode) {
                JsonNode representingNode = advocateNode.get("representing");
                if (representingNode.isArray()) {
                    String partyType = representingNode.get(0).get("partyType").asText().toLowerCase();
                    if (partyType.contains(partyTypeToMatch)) {
                        String uuid = advocateNode.path("additionalDetails").get("uuid").asText();
                        if (!uuid.isEmpty()) {
                            uuids.add(uuid);
                        }
                    }
                }
            }
        }
        return uuids;
    }
}
