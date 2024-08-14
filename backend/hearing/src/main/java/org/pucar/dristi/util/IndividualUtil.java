package org.pucar.dristi.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.IndividualSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.INDIVIDUAL_UTILITY_EXCEPTION;

@Component
@Slf4j
public class IndividualUtil {

    private final ServiceRequestRepository serviceRequestRepository;

    @Autowired
    public IndividualUtil(ServiceRequestRepository serviceRequestRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
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
            log.error("Custom Exception occurred in Individual Utility");
            throw e;
        }
        catch (Exception e){
            throw new CustomException(INDIVIDUAL_UTILITY_EXCEPTION,"Error in individual utility service: "+e.getMessage());
        }

    }
}
