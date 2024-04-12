
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
    public IndividualResponse searchIndividual(AdvocateClerkRequest advocateClerkRequest){
        IndividualSearchRequest individualSearchRequest = new IndividualSearchRequest();
        individualSearchRequest.setRequestInfo(advocateClerkRequest.getRequestInfo());
        IndividualSearch individual = new IndividualSearch();
        individual.setIndividualId(advocateClerkRequest.getClerks().get(0).getIndividualId());
        individualSearchRequest.setIndividual(individual);
        StringBuilder uri = new StringBuilder(config.getIndividualHost()).append(config.getIndividualSearchEndpoint());
        IndividualResponse individualResponse = individualUtils.individualCall(individualSearchRequest, uri);

        return individualResponse;
    }
}
