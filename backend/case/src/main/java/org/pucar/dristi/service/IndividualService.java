package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.models.individual.IndividualSearch;
import org.egov.common.models.individual.IndividualSearchRequest;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IndividualUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IndividualService {
    @Autowired
    private IndividualUtil individualUtils;
    @Autowired
    private Configuration config;

    public Boolean searchIndividual(RequestInfo requestInfo , String individualId ){
        IndividualSearchRequest individualSearchRequest = new IndividualSearchRequest();
        individualSearchRequest.setRequestInfo(requestInfo);
        IndividualSearch individualSearch = new IndividualSearch();
        individualSearch.setIndividualId(individualId);
        individualSearchRequest.setIndividual(individualSearch);

        StringBuilder uri = new StringBuilder(config.getIndividualHost()).append(config.getIndividualSearchEndpoint());
        uri.append("?limit=1000").append("&offset=0").append("&tenantId=").append(requestInfo.getUserInfo().getTenantId()).append("&includeDeleted=true");
        Boolean isIndividualValid = individualUtils.individualCall(individualSearchRequest, uri);
        return isIndividualValid;
    }
}
