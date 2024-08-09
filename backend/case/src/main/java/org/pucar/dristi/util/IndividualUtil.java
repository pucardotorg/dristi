package org.pucar.dristi.util;

import static org.pucar.dristi.config.ServiceConstants.INDIVIDUAL_UTILITY_EXCEPTION;

import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.IndividualSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class IndividualUtil {


    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    public IndividualUtil(ServiceRequestRepository serviceRequestRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
    }

    public Boolean individualExists(IndividualSearchRequest individualRequest, StringBuilder uri) {

        return !getIndividualId(individualRequest,uri).isEmpty();

    }

    public String getIndividualId(IndividualSearchRequest individualRequest, StringBuilder uri){
        String individualId = new String();
        JsonObject individual =  getIndividual(individualRequest, uri);
        if (!ObjectUtils.isEmpty(individual) && individual.get("individualId") != null)    {
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
