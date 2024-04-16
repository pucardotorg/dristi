
package org.pucar.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.egov.common.models.individual.IndividualRequest;
import org.egov.common.models.individual.IndividualResponse;
import org.pucar.config.Configuration;
import org.pucar.util.IndividualUtil;
import org.pucar.util.UserUtil;
import org.pucar.web.models.AdvocateClerkRequest;
import org.pucar.web.models.IndividualSearch;
import org.pucar.web.models.IndividualSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class IndividualService {
    @Autowired
    private  IndividualUtil individualUtils;
    @Autowired
    private  Configuration config;

    @Autowired
    public IndividualService(Configuration config) {
//        this.individualUtils = individualUtils;
        this.config = config;
    }
    public Boolean searchIndividual(AdvocateClerkRequest advocateClerkRequest){
        IndividualSearchRequest individualSearchRequest = new IndividualSearchRequest();
        individualSearchRequest.setRequestInfo(advocateClerkRequest.getRequestInfo());
        IndividualSearch Individual = new IndividualSearch();
        Individual.setIndividualId(advocateClerkRequest.getClerks().get(0).getIndividualId());
        individualSearchRequest.setIndividual(Individual);
        StringBuilder uri = new StringBuilder(config.getIndividualHost()).append(config.getIndividualSearchEndpoint());
        uri.append("?limit=1000").append("&offset=0").append("&tenantId=").append(advocateClerkRequest.getClerks().get(0).getTenantId()).append("&includeDeleted=true");
        Boolean individualResponse = individualUtils.individualCall(individualSearchRequest, uri);

        return individualResponse;
    }
}
