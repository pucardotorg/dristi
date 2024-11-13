package com.pucar.drishti.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pucar.drishti.config.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.pucar.drishti.config.ServiceConstants.INVALID_TRANSACTION;
import static com.pucar.drishti.config.ServiceConstants.INVALID_TRANSACTION_EXCEPTION;

@Component
@Slf4j
public class FileStoreUtil {

    private final RestTemplate restTemplate;

    private final Configuration configs;

    @Autowired
    public FileStoreUtil(RestTemplate restTemplate, Configuration configs) {
        this.restTemplate = restTemplate;
        this.configs = configs;
    }


    public Resource fetchFileStoreObjectById(String fileStoreId, String tenantId) {

        StringBuilder uri = new StringBuilder();
        uri.append(configs.getFilestoreHost()).append(configs.getFilestoreEndPoint());
        uri = appendQueryParams(uri,"fileStoreId", fileStoreId,"tenantId", tenantId);
        Resource object;
        try {
            object = restTemplate.getForObject(uri.toString(), Resource.class);
            return object;

        } catch (Exception e) {
            log.error(INVALID_TRANSACTION,e);
            throw new CustomException(INVALID_TRANSACTION_EXCEPTION, e.getMessage());

        }


    }

    public StringBuilder appendQueryParams(StringBuilder uri, String paramName1, String paramValue1, String paramName2, String paramValue2) {
        if (uri.indexOf("?") == -1) {
            uri.append("?");
        } else {
            uri.append("&");
        }
        uri.append(paramName1).append("=").append(paramValue1).append("&");
        uri.append(paramName2).append("=").append(paramValue2);
        return uri;
    }
}
