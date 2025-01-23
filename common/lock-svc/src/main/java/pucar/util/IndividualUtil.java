package pucar.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import pucar.config.Configuration;
import pucar.repository.ServiceRequestRepository;
import pucar.web.models.individual.IndividualSearch;
import pucar.web.models.individual.IndividualSearchRequest;

import java.util.Collections;

import static pucar.config.ServiceConstants.INDIVIDUAL_SERVICE_EXCEPTION;
import static pucar.config.ServiceConstants.INDIVIDUAL_UTILITY_EXCEPTION;

@Component
@Slf4j
public class IndividualUtil {

    private final ServiceRequestRepository serviceRequestRepository;
    private final Configuration config;

    @Autowired
    public IndividualUtil(ServiceRequestRepository serviceRequestRepository, Configuration config) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.config = config;
    }

    public String getIndividualId(RequestInfo requestInfo) {
        try {
            IndividualSearchRequest individualSearchRequest = new IndividualSearchRequest();
            individualSearchRequest.setRequestInfo(requestInfo);
            IndividualSearch individualSearch = new IndividualSearch();
            log.info("UUID :: {}", requestInfo.getUserInfo().getUuid());
            individualSearch.setUserUuid(Collections.singletonList(requestInfo.getUserInfo().getUuid()));
            individualSearchRequest.setIndividual(individualSearch);

            StringBuilder uri = new StringBuilder(config.getIndividualHost()).append(config.getIndividualSearchEndpoint());
            uri.append("?limit=1").append("&offset=0").append("&tenantId=").append(requestInfo.getUserInfo().getTenantId()).append("&includeDeleted=true");
            return getIndividualId(individualSearchRequest, uri);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error in search individual service :: {}", e);
            throw new CustomException(INDIVIDUAL_SERVICE_EXCEPTION, "Error in search individual service" + e.getMessage());
        }
    }

    public String getIndividualId(IndividualSearchRequest individualRequest, StringBuilder uri) {
        String individualId = "";
        JsonObject individual = getIndividual(individualRequest, uri);
        if (!ObjectUtils.isEmpty(individual) && individual.get("individualId") != null) {
            individualId = individual.get("individualId").getAsString();
        }
        return individualId;
    }

    public JsonObject getIndividual(IndividualSearchRequest individualRequest, StringBuilder uri) {
        try {
            JsonObject individual = new JsonObject();
            Object responseMap = serviceRequestRepository.fetchResult(uri, individualRequest);
            if (responseMap != null) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(responseMap);
                log.info("Individual Response :: {}", jsonString);
                JsonObject response = JsonParser.parseString(jsonString).getAsJsonObject();
                JsonArray individualObject = response.getAsJsonArray("Individual");
                if (!individualObject.isEmpty() && individualObject.get(0).getAsJsonObject() != null) {
                    individual = individualObject.get(0).getAsJsonObject();
                }
            }
            return individual;
        } catch (CustomException e) {
            log.error("Custom Exception occurred in individual Utility :: {}", e.toString());
            throw e;
        } catch (Exception e) {
            throw new CustomException(INDIVIDUAL_UTILITY_EXCEPTION, "Exception in individual utility service: " + e.getMessage());
        }
    }

}
