package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IndividualUtil;
import org.pucar.dristi.web.models.Individual;
import org.pucar.dristi.web.models.IndividualSearch;
import org.pucar.dristi.web.models.IndividualSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


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

    public List<Individual> getIndividualsByIndividualId(RequestInfo requestInfo, String individualId) throws CustomException {
        try {
            IndividualSearchRequest individualSearchRequest = new IndividualSearchRequest();
            individualSearchRequest.setRequestInfo(requestInfo);
            IndividualSearch individualSearch = new IndividualSearch();
            individualSearch.setIndividualId(individualId);
            individualSearchRequest.setIndividual(individualSearch);
            StringBuilder uri = buildIndividualSearchUri(requestInfo, Collections.singletonList(individualId));
            List<Individual> individual = individualUtils.getIndividualByIndividualId(individualSearchRequest, uri);
            if (individual != null) {
                return individual;
            } else {
                log.error("No individuals found");
                return Collections.emptyList();
            }
        } catch (Exception e) {
            log.error("Error in search individual service: ", e);
            return Collections.emptyList();
        }
    }

    private StringBuilder buildIndividualSearchUri(RequestInfo requestInfo, List<String> individualId) {
        return new StringBuilder(config.getIndividualHost())
                .append(config.getIndividualSearchEndpoint())
                .append("?limit=").append(individualId.size())
                .append("&offset=0")
                .append("&tenantId=").append(requestInfo.getUserInfo().getTenantId())
                .append("&includeDeleted=true");
    }

}
