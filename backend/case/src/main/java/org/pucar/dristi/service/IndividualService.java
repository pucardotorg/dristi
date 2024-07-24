package org.pucar.dristi.service;

import static org.pucar.dristi.config.ServiceConstants.INDIVIDUAL_SERVICE_EXCEPTION;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IndividualUtil;
import org.pucar.dristi.web.models.IndividualSearch;
import org.pucar.dristi.web.models.IndividualSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IndividualService {

    private IndividualUtil individualUtils;

    private Configuration config;

    @Autowired
    public IndividualService(IndividualUtil individualUtils, Configuration config) {
        this.individualUtils = individualUtils;
        this.config = config;
    }

    public Boolean searchIndividual(RequestInfo requestInfo, String individualId) {
        try {
            IndividualSearchRequest individualSearchRequest = new IndividualSearchRequest();
            individualSearchRequest.setRequestInfo(requestInfo);
            IndividualSearch individualSearch = new IndividualSearch();
            log.info("Individual Id :: {}", individualId);
            individualSearch.setIndividualId(individualId);
            individualSearchRequest.setIndividual(individualSearch);

            StringBuilder uri = new StringBuilder(config.getIndividualHost()).append(config.getIndividualSearchEndpoint());
            uri.append("?limit=1").append("&offset=0").append("&tenantId=").append(requestInfo.getUserInfo().getTenantId()).append("&includeDeleted=true");
            Boolean isIndividualValid = individualUtils.individualCall(individualSearchRequest, uri);
            return isIndividualValid;
        } catch(CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Error in search individual service :: {}",e.toString());
            throw new CustomException(INDIVIDUAL_SERVICE_EXCEPTION, "Error in search individual service" + e.getMessage());
        }
    }
}
