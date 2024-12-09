package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.models.individual.Individual;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.IndividualSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.INDIVIDUAL_UTILITY_EXCEPTION;

@Component
@Slf4j
public class IndividualUtil {

    private final ServiceRequestRepository serviceRequestRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public IndividualUtil(ServiceRequestRepository serviceRequestRepository, ObjectMapper objectMapper) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.objectMapper = objectMapper;
    }

    /** To call the individual service for searching individualId exists or not
     * @param individualRequest
     * @param uri
     * @param individualUserUUID
     * @return
     */
    public Boolean individualCall(IndividualSearchRequest individualRequest, StringBuilder uri, Map<String, String> individualUserUUID) {
        try{
            Object responseMap = serviceRequestRepository.fetchResult(uri, individualRequest);
            if(responseMap!=null){
                Gson gson= new Gson();
                String jsonString=gson.toJson(responseMap);
                log.info("Response :: {}", jsonString);
                JsonObject response = JsonParser.parseString(jsonString).getAsJsonObject();
                JsonArray individualObject=response.getAsJsonArray("Individual");
                if(!individualObject.isEmpty()) {
                    String userUUID = individualObject.get(0).getAsJsonObject().get("userUuid").getAsString();
                    individualUserUUID.put("userUuid", userUUID);
                }
                return !individualObject.isEmpty() && individualObject.get(0).getAsJsonObject().get("individualId") != null;
            }
            return false;
        }
        catch (CustomException e) {
            log.error("Custom Exception occurred in Individual Utility :: {}", e.toString());
            throw e;
        }
        catch (Exception e){
            throw new CustomException(INDIVIDUAL_UTILITY_EXCEPTION,"Error in individual utility service: "+e.getMessage());
        }

    }

    public List<Individual> getIndividualByIndividualId(IndividualSearchRequest individualRequest, StringBuilder uri) {
        List<Individual> individuals = new ArrayList<>();
        try {
            Object responseMap = serviceRequestRepository.fetchResult(uri, individualRequest);
            if (responseMap != null) {
                String jsonString = objectMapper.writeValueAsString(responseMap);
                log.info("Response :: {}", jsonString);
                JsonNode rootNode = objectMapper.readTree(jsonString);

                JsonNode individualNode = rootNode.path("Individual");

                if (individualNode.isArray()) {
                    for (JsonNode node : individualNode) {
                        Individual individual = objectMapper.treeToValue(node, Individual.class);
                        individuals.add(individual);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error occurred in individual utility", e);
        }

        return individuals;
    }
}
