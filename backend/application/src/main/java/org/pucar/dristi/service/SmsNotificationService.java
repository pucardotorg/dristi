package org.pucar.dristi.service;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.models.RequestInfoWrapper;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.SMSRequest;
import org.pucar.dristi.web.models.SmsTemplateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.pucar.dristi.config.ServiceConstants.*;

@Service
@Slf4j
public class SmsNotificationService {

    private final Configuration config;

    private final Producer producer;

    private final ServiceRequestRepository repository;

    @Autowired
    public SmsNotificationService(Configuration config, Producer producer, ServiceRequestRepository repository) {
        this.config = config;
        this.producer = producer;
        this.repository = repository;
    }

    public void sendNotification(RequestInfo requestInfo, SmsTemplateData smsTemplateData, String notificationStatus, String mobileNumber) {
        try {

            String message = getMessage(requestInfo,smsTemplateData, notificationStatus);
            if (StringUtils.isEmpty(message)) {
                log.info("SMS content has not been configured for this case");
                return;
            }
            pushNotificationBasedOnNotificationStatus(smsTemplateData, notificationStatus, message, mobileNumber);

        } catch (Exception e){
            log.error("Error in Sending Message To Notification Service: " , e);
        }

    }

    private void pushNotificationBasedOnNotificationStatus(SmsTemplateData templateData, String messageCode, String message, String mobileNumber) {
        Map<String, String> messageCodeToTemplateIdMap = new HashMap<>() {{
            put(RESCHEDULE_REQUEST_SUBMITTED, config.getSmsNotificationRescheduleRequestSubmittedTemplateId());
            put(RESCHEDULE_REQUEST_REJECTED_REQUESTING_PARTY, config.getSmsNotificationRescheduleRequestRejectedTemplateId());
            put(RESCHEDULE_REQUEST_ACCEPTED_REQUESTING_PARTY, config.getSmsNotificationRescheduleRequestAcceptedTemplateId());
            put(RESCHEDULE_REQUEST_ACCEPTED_OPPONENT_PARTY, config.getSmsNotificationRescheduleRequestAcceptedOpponentTemplateId());
            put(RESCHEDULE_REQUEST_REJECTED_OPPONENT_PARTY, config.getSmsNotificationRescheduleRequestRejectedOpponentTemplateId());
            put(CHECKOUT_REQUEST_REJECTED, config.getSmsNotificationCheckoutRequestRejectedTemplateId());
            put(CHECKOUT_REQUEST_ACCEPTED, config.getSmsNotificationCheckoutRequestAcceptedTemplateId());
            put(EXTENSION_SUBMISSION_DEADLINE_SUBMITTED, config.getSmsNotificationExtensionApplicationSubmittedTemplateId());
            put(EXTENSION_SUBMISSION_DEADLINE_ACCEPTED, config.getSmsNotificationExtensionApplicationAcceptedTemplateId());
            put(EXTENSION_SUBMISSION_DEADLINE_REJECTED, config.getSmsNotificationExtensionApplicationRejectedTemplateId());
            put(VOLUNTARY_SUBMISSION_SUBMITTED, config.getSmsNotificationVoluntarySubmissionSubmittedTemplateId());
            put(VOLUNTARY_SUBMISSION_REJECTED, config.getSmsNotificationVoluntarySubmissionRejectedTemplateId());
            put(VOLUNTARY_SUBMISSION_ACCEPTED, config.getSmsNotificationVoluntarySubmissionAcceptedTemplateId());
            put(VARIABLE_SUBMISSION_SUBMITTED, config.getSmsNotificationVariableSubmissionSubmittedTemplateId());
            put(VARIABLE_SUBMISSION_REJECTED, config.getSmsNotificationVariableSubmissionRejectedTemplateId());
            put(VARIABLE_SUBMISSION_ACCEPTED, config.getSmsNotificationVariableSubmissionAcceptedTemplateId());
            put(EVIDENCE_SUBMITTED, config.getSmsNotificationEvidenceSubmittedTemplateId());
            put(RESPONSE_REQUIRED, config.getSmsNotificationResponseRequiredTemplateId());
        }};
        String templateId = messageCodeToTemplateIdMap.get(messageCode);
        if (templateId != null) {
            pushNotification(templateData, message, mobileNumber, templateId);
        } else {
            log.warn("No template ID found for message code: {}", messageCode);
        }
    }

    private void pushNotification(SmsTemplateData templateData, String message, String mobileNumber, String templateId) {
        //get individual name, id, mobileNumber
        log.info("get case e filing number, id, cnr");
        Map<String, String> smsDetails = getDetailsForSMS(templateData, mobileNumber);

        log.info("building Notification Request for case number {}", templateData.getCourtCaseNumber());
        message = buildMessage(smsDetails, message);
        SMSRequest smsRequest = SMSRequest.builder()
                .mobileNumber(smsDetails.get("mobileNumber"))
                .tenantId(smsDetails.get("tenantId"))
                .templateId(templateId)
                .contentType("TEXT")
                .category("NOTIFICATION")
                .locale(NOTIFICATION_ENG_LOCALE_CODE)
                .expiryTime(System.currentTimeMillis() + 60 * 60 * 1000)
                .message(message).build();
        log.info("push message {}", smsRequest);

        producer.push(config.getSmsNotificationTopic(), smsRequest);
    }

    private Map<String, String> getDetailsForSMS(SmsTemplateData smsTemplateData, String mobileNumber) {
        Map<String, String> smsDetails = new HashMap<>();

        smsDetails.put("courtCaseNumber", smsTemplateData.getCourtCaseNumber());
        smsDetails.put("cmpNumber", smsTemplateData.getCmpNumber());
        smsDetails.put("hearingDate", smsTemplateData.getOriginalHearingDate());
        smsDetails.put("reScheduledHearingDate", smsTemplateData.getReScheduledHearingDate());
        smsDetails.put("tenantId", smsTemplateData.getTenantId());
        smsDetails.put("mobileNumber", mobileNumber);

        return smsDetails;
    }


    /**
     * Gets the message from localization
     *
     * @param requestInfo
     * @param templateData
     * @param msgCode
     * @return
     */

    public String getMessage(RequestInfo requestInfo, SmsTemplateData templateData, String msgCode) {
        String rootTenantId = templateData.getTenantId();
        Map<String, Map<String, String>> localizedMessageMap = getLocalisedMessages(requestInfo, rootTenantId,
                NOTIFICATION_ENG_LOCALE_CODE, NOTIFICATION_MODULE_CODE);
        if (localizedMessageMap.isEmpty()) {
            return null;
        }
        return localizedMessageMap.get(NOTIFICATION_ENG_LOCALE_CODE + "|" + rootTenantId).get(msgCode);
    }

    /**
     * Builds msg based on the format
     *
     * @param message
     * @param userDetailsForSMS
     * @return
     */
    public String buildMessage(Map<String, String> userDetailsForSMS, String message) {
        message = message.replace("{{caseId}}", Optional.ofNullable(userDetailsForSMS.get("caseId")).orElse(""))
                .replace("{{efilingNumber}}", Optional.ofNullable(userDetailsForSMS.get("efilingNumber")).orElse(""))
                .replace("{{cnr}}", Optional.ofNullable(userDetailsForSMS.get("cnr")).orElse(""))
                .replace("{{link}}", Optional.ofNullable(userDetailsForSMS.get("link")).orElse(""))
                .replace("{{date}}", Optional.ofNullable(userDetailsForSMS.get("date")).orElse(""))
                .replace("{{cmpNumber}}", Optional.ofNullable(userDetailsForSMS.get("cmpNumber")).orElse(""))
                .replace("{{hearingDate}}", Optional.ofNullable(userDetailsForSMS.get("hearingDate")).orElse(""));
        return message;
    }

    /**
     * Creates a cache for localization that gets refreshed at every call.
     *
     * @param requestInfo
     * @param rootTenantId
     * @param locale
     * @param module
     * @return
     */
    public Map<String, Map<String, String>> getLocalisedMessages(RequestInfo requestInfo, String rootTenantId, String locale, String module) {
        Map<String, Map<String, String>> localizedMessageMap = new HashMap<>();
        Map<String, String> mapOfCodesAndMessages = new HashMap<>();
        StringBuilder uri = new StringBuilder();
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        uri.append(config.getLocalizationHost()).append(config.getLocalizationContextPath()).append(config.getLocalizationSearchEndpoint())
                .append("?tenantId=" + rootTenantId).append("&module=" + module).append("&locale=" + locale);
        List<String> codes = null;
        List<String> messages = null;
        Object result = null;
        try {
            result = repository.fetchResult(uri, requestInfoWrapper);
            codes = JsonPath.read(result, NOTIFICATION_LOCALIZATION_CODES_JSONPATH);
            messages = JsonPath.read(result, NOTIFICATION_LOCALIZATION_MSGS_JSONPATH);
        } catch (Exception e) {
            log.error("Exception while fetching from localization: " + e);
        }
        if (null != result) {
            for (int i = 0; i < codes.size(); i++) {
                mapOfCodesAndMessages.put(codes.get(i), messages.get(i));
            }
            localizedMessageMap.put(locale + "|" + rootTenantId, mapOfCodesAndMessages);
        }

        return localizedMessageMap;
    }
}
