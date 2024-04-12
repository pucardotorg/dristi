package org.pucar.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.idgen.IdGenerationRequest;
import org.egov.common.contract.idgen.IdGenerationResponse;
import org.egov.common.contract.idgen.IdRequest;
import org.egov.common.contract.idgen.IdResponse;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.models.individual.IndividualResponse;
import org.egov.tracer.model.CustomException;
import org.pucar.config.Configuration;
import org.pucar.repository.ServiceRequestRepository;
import org.pucar.web.models.IndividualSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.pucar.config.ServiceConstants.IDGEN_ERROR;
import static org.pucar.config.ServiceConstants.NO_IDS_FOUND_ERROR;
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

    public IndividualResponse individualCall(IndividualSearchRequest individualRequest, StringBuilder uri) {
        String dobFormat = null;
        if(uri.toString().contains(configs.getIndividualSearchEndpoint())  || uri.toString().contains(configs.getIndividualUpdateEndpoint()))
            dobFormat="yyyy-MM-dd";
        else if(uri.toString().contains(configs.getIndividualUpdateEndpoint()))
            dobFormat = "dd/MM/yyyy";
        try{
            LinkedHashMap responseMap = (LinkedHashMap)serviceRequestRepository.fetchResult(uri, individualRequest);
            IndividualResponse individualDetailResponse = mapper.convertValue(responseMap,IndividualResponse.class);
            return individualDetailResponse;
        }
        catch(IllegalArgumentException  e)
        {
            throw new CustomException("IllegalArgumentException","ObjectMapper not able to convertValue in userCall");
        }
    }
}
