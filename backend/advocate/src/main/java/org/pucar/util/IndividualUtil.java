package org.pucar.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.repository.ServiceRequestRepository;
import org.pucar.web.models.IndividualSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.pucar.config.ServiceConstants.INDIVIDUAL_UTILITY_EXCEPTION;

@Component
@Slf4j
public class IndividualUtil {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;


    @Autowired
    public IndividualUtil(ObjectMapper mapper, ServiceRequestRepository serviceRequestRepository) {
        this.mapper = mapper;
        this.serviceRequestRepository = serviceRequestRepository;
    }

    public Boolean individualCall(IndividualSearchRequest individualRequest, StringBuilder uri, Map<String, String> individualUserUUID) {
        try{
            Object responseMap = serviceRequestRepository.fetchResult(uri, individualRequest);
            if(responseMap!=null){
                Gson gson= new Gson();
                String jsonString=gson.toJson(responseMap);
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
