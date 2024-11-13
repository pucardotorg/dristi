package org.egov.web.notification.mail.utils;

import com.jayway.jsonpath.JsonPath;
import digit.models.coremodels.RequestInfoWrapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.web.notification.mail.config.ApplicationConfiguration;
import org.egov.web.notification.mail.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.egov.web.notification.mail.utils.Constants.ERROR_WHILE_FETCHING_MESSAGES;

@Component
@Slf4j
public class LocalizationUtil {

    @Autowired
    private ApplicationConfiguration config;

    @Autowired
    private ServiceRequestRepository repository;

    public Map<String, Map<String, String>> getLocalisedMessages(RequestInfo requestInfo, String rootTenantId, String locale, String module, Set<String> messageCode) {
        Map<String, Map<String, String>> localizedMessageMap = new HashMap<>();
        Map<String, String> mapOfCodesAndMessages = new HashMap<>();
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);

        StringBuilder uri = new StringBuilder();
        uri.append(config.getLocalizationHost()).append(config.getLocalizationContextPath())
                .append(config.getLocalizationSearchEndpoint()).append("?tenantId=" + rootTenantId)
                .append("&module=" + module).append("&locale=" + locale);
        StringJoiner codesJoiner = new StringJoiner(",");
        messageCode.forEach(codesJoiner::add);
        uri.append("&codes=" + codesJoiner.toString());

        List<String> codes = null;
        List<String> messages = null;
        Object result = null;
        try {
            result = repository.fetchResult(uri, requestInfoWrapper);
            codes = JsonPath.read(result, Constants.LOCALIZATION_CODES_JSONPATH);
            messages = JsonPath.read(result, Constants.LOCALIZATION_MSGS_JSONPATH);
        } catch (Exception e) {
            log.error(ERROR_WHILE_FETCHING_MESSAGES + e.getMessage());
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
