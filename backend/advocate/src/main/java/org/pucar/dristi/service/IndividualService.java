package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IndividualUtil;
import org.pucar.dristi.web.models.IndividualSearch;
import org.pucar.dristi.web.models.IndividualSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.INDIVIDUAL_SERVICE_EXCEPTION;

@Service
@Slf4j
public class IndividualService {
    @Autowired
    private  IndividualUtil individualUtils;
    @Autowired
    private  Configuration config;

    public Boolean searchIndividual(RequestInfo requestInfo , String individualId, Map<String, String> individualUserUUID ){
        try {
            IndividualSearchRequest individualSearchRequest = new IndividualSearchRequest();
            individualSearchRequest.setRequestInfo(requestInfo);
            IndividualSearch individualSearch = new IndividualSearch();
            log.info("Individual Id :: {}", individualId);
            individualSearch.setIndividualId(individualId);
            individualSearchRequest.setIndividual(individualSearch);
            StringBuilder uri = new StringBuilder(config.getIndividualHost()).append(config.getIndividualSearchEndpoint());
            uri.append("?limit=1000").append("&offset=0").append("&tenantId=").append(requestInfo.getUserInfo().getTenantId()).append("&includeDeleted=true");
            Boolean isIndividualValid = individualUtils.individualCall(individualSearchRequest, uri, individualUserUUID);
            return isIndividualValid;

        } catch(CustomException e){
            throw e;
        }
        catch (Exception e){
            log.error("Error in search individual service :: {}", e.toString());
            throw new CustomException(INDIVIDUAL_SERVICE_EXCEPTION,"Error in search individual service"+e.getMessage());
        }
    }
}
