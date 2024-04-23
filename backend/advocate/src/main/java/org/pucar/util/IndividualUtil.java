package org.pucar.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.egov.tracer.model.CustomException;
import org.pucar.config.Configuration;
import org.pucar.repository.ServiceRequestRepository;
import org.pucar.web.models.IndividualSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IndividualUtil {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private Configuration configs;

    @Autowired
    public IndividualUtil(ObjectMapper mapper, ServiceRequestRepository serviceRequestRepository) {
        this.mapper = mapper;
        this.serviceRequestRepository = serviceRequestRepository;
    }

    public Boolean individualCall(IndividualSearchRequest individualRequest, StringBuilder uri) {
        try{
            Object responseMap = serviceRequestRepository.fetchResult(uri, individualRequest);
            if(responseMap!=null){
                Gson gson= new Gson();
                String jsonString=gson.toJson(responseMap);
                JsonObject response = JsonParser.parseString(jsonString).getAsJsonObject();
                JsonArray individualObject=response.getAsJsonArray("Individual");
                return !individualObject.isEmpty() && individualObject.get(0).getAsJsonObject().get("individualId") != null;
            }
            return false;
        }catch (Exception e){
            throw new CustomException(e.getMessage(),"Error while calling external individual service");
        }

    }
}
