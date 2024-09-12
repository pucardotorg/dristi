package org.egov.web.notification.sms.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.egov.web.notification.sms.config.SMSProperties;
import org.egov.web.notification.sms.models.Category;
import org.egov.web.notification.sms.models.Sms;
import org.egov.web.notification.sms.service.BaseSMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
@Slf4j
@ConditionalOnProperty(value = "sms.provider.class", matchIfMissing = true, havingValue = "CDAC")
public class CdacSmsServiceImpl extends BaseSMSService {

    @Value("${sms.url.dont_encode_url:true}")
    private boolean dontEncodeURL;

    @Autowired
    private SMSProperties smsProperties;

    @Autowired
    private CdacSmsClient cdacSmsClient;


    protected void submitToExternalSmsService(Sms sms) {
        try {
            if (smsProperties.requestType.equals("POST"))
            {
                // TODO: remove default mobile number for production
                if (smsProperties.isDefaultMobileNumber()) sms.setMobileNumber(smsProperties.getDefaultMobileNumber());

                if (sms.getCategory() == Category.OTP) cdacSmsClient.sendOtpSMS(sms, smsProperties);
                if (sms.getCategory() == Category.NOTIFICATION)
                {
                    switch (sms.getContentType())
                    {
                        case TEXT:
                            cdacSmsClient.sendSingleSMS(sms, smsProperties);
                            break;
                        case UNICODE:
                            cdacSmsClient.sendUnicodeSMS(sms, smsProperties);
                            break;
                        default:
                            break;
                    }
                }
            }
            else {
                log.error("Invalid API method");
            }

        } catch (RestClientException e) {
            log.error("Error occurred while sending SMS to " + sms.getMobileNumber(), e);
            throw e;
        }
    }


}
