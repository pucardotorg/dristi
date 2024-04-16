package org.pucar.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.models.individual.IndividualResponse;
import org.egov.common.models.individual.IndividualSearch;
import org.pucar.config.Configuration;
import org.pucar.util.IndividualUtil;
import org.pucar.web.models.AdvocateRequest;
import org.pucar.web.models.IndividualSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IndividualService {
    @Autowired
    private  IndividualUtil individualUtils;
    @Autowired
    private  Configuration config;

    public Boolean searchIndividual(AdvocateRequest request){
        IndividualSearchRequest individualSearchRequest = new IndividualSearchRequest();
        individualSearchRequest.setRequestInfo(request.getRequestInfo());
        IndividualSearch individualSearch = new IndividualSearch();
        individualSearch.setIndividualId(request.getAdvocates().get(0).getIndividualId());
        individualSearchRequest.setIndividual(individualSearch);

        StringBuilder uri = new StringBuilder(config.getIndividualHost()).append(config.getIndividualSearchEndpoint());
        uri.append("?limit=1000").append("&offset=0").append("&tenantId=").append(request.getAdvocates().get(0).getTenantId()).append("&includeDeleted=true");
        Boolean isIndividualValid = individualUtils.individualCall(individualSearchRequest, uri);
        return isIndividualValid;
    }
}
