package org.pucar.dristi.service;


import com.jayway.jsonpath.JsonPath;
import digit.models.coremodels.RequestInfoWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.models.individual.Individual;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.SMSRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.pucar.dristi.config.ServiceConstants.*;

@Service
@Slf4j
public class NotificationService {

    private final Configuration config;

    private final Producer producer;

    private final ServiceRequestRepository repository;

    private final IndividualService individualService;

    @Autowired
    public NotificationService(Configuration config, Producer producer, ServiceRequestRepository repository, IndividualService individualService) {
        this.config = config;
        this.producer = producer;
        this.repository = repository;
        this.individualService = individualService;
    }

    public void sendNotification(RequestInfo requestInfo, CourtCase courtCase, String notificationStatus, String uuid) {
        try {
            List<Individual> individuals = individualService.getIndividuals(requestInfo, Collections.singletonList(uuid));
            if (individuals == null) {
                log.info("No individual found with UUID: {}", courtCase.getAuditdetails().getCreatedBy());
                return;
            }
            String message = getMessage(requestInfo,courtCase, notificationStatus);
            if (StringUtils.isEmpty(message)) {
                log.info("SMS content has not been configured for this case");
                return;
            }
            pushNotificationBasedOnNotificationStatus(courtCase, notificationStatus, message, individuals.get(0));

        } catch (Exception e){
            log.error("Error in Sending Message To Notification Service: " , e);
        }

    }

    private void pushNotificationBasedOnNotificationStatus(CourtCase courtCase, String notificationStatus, String message, Individual individual) {
        if(notificationStatus.equalsIgnoreCase(PAYMENT_PENDING)){
            pushNotification(courtCase, message, individual, config.getSmsNotificationPaymentPendingTemplateId());
        }
        else if(notificationStatus.equalsIgnoreCase(ESIGN_PENDING)){
            pushNotification(courtCase, message, individual, config.getSmsNotificationEsignPendingTemplateId());
        }
        else if(notificationStatus.equalsIgnoreCase(ADVOCATE_ESIGN_PENDING)){
            pushNotification(courtCase, message, individual, config.getSmsNotificationAdvocateEsignPendingTemplateId());
        }
        else {
            pushNotification(courtCase, message, individual, config.getSmsNotificationTemplateId());
        }
    }

    private void pushNotification(CourtCase courtCase, String message, Individual individual, String templateId) {
       //get individual name, id, mobileNumber
        log.info("get case e filing number, id, cnr");
        Map<String, String> smsDetails = getDetailsForSMS(courtCase, individual);

        log.info("building Notification Request for case filing number {}", courtCase.getFilingNumber());
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

    private Map<String, String> getDetailsForSMS(CourtCase courtCase, Individual individual) {
        Map<String, String> smsDetails = new HashMap<>();

        smsDetails.put("caseId", courtCase.getCaseNumber());
        smsDetails.put("efilingNumber", courtCase.getFilingNumber());
        smsDetails.put("cnr", courtCase.getCnrNumber());
        smsDetails.put("date", "");
        smsDetails.put("link", "");
        smsDetails.put("tenantId", courtCase.getTenantId().split("\\.")[0]);
        smsDetails.put("mobileNumber", individual.getMobileNumber());

        return smsDetails;
    }


    /**
     * Gets the message from localization
     *
     * @param requestInfo
     * @param courtCase
     * @param msgCode
     * @return
     */

    public String getMessage(RequestInfo requestInfo, CourtCase courtCase, String msgCode) {
        String rootTenantId = courtCase.getTenantId().split("\\.")[0];
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
                .replace("{{date}}", Optional.ofNullable(userDetailsForSMS.get("date")).orElse(""));
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
        uri.append(config.getLocalizationHost()).append(config.getLocalizationSearchEndpoint())
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