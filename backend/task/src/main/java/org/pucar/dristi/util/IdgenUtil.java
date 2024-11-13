package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.IdGenerationRequest;
import org.pucar.dristi.web.models.IdGenerationResponse;
import org.pucar.dristi.web.models.IdRequest;
import org.pucar.dristi.web.models.IdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.IDGEN_ERROR;
import static org.pucar.dristi.config.ServiceConstants.NO_IDS_FOUND_ERROR;

@Component
@Slf4j
public class IdgenUtil {

    private final ObjectMapper mapper;
    private final ServiceRequestRepository restRepo;
    private final Configuration configs;

    @Autowired
    public IdgenUtil(ObjectMapper mapper, ServiceRequestRepository restRepo, Configuration configs) {
        this.mapper = mapper;
        this.restRepo = restRepo;
        this.configs = configs;
    }

    /** To generate IDs based on idName and format
     * @param requestInfo
     * @param tenantId
     * @param idName
     * @param idformat
     * @param count
     * @return
     */
    public List<String> getIdList(RequestInfo requestInfo, String tenantId, String idName, String idformat,
                                  Integer count, Boolean isSequencePadded) {
        try {
            List<IdRequest> reqList = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                reqList.add(IdRequest.builder().idName(idName).isSequencePadded(isSequencePadded).format(idformat).tenantId(tenantId).build());
            }

            IdGenerationRequest request = IdGenerationRequest.builder().idRequests(reqList).requestInfo(requestInfo)
                    .build();
            StringBuilder uri = new StringBuilder(configs.getIdGenHost()).append(configs.getIdGenPath());

            IdGenerationResponse response = fetchIdGenerationResponse(uri, request);

            List<IdResponse> idResponses = response.getIdResponses();

            if (CollectionUtils.isEmpty(idResponses))
                throw new CustomException(IDGEN_ERROR, NO_IDS_FOUND_ERROR);

            return List.copyOf(idResponses.stream().map(IdResponse::getId).toList());
        } catch (CustomException e) {
            log.error("Custom Exception occurred in calling Idgen :: {}", e.toString());
            throw e;
        } catch (Exception e) {
            throw new CustomException(IDGEN_ERROR, "ERROR in IDGEN Service");
        }
    }

    private IdGenerationResponse fetchIdGenerationResponse(StringBuilder uri, IdGenerationRequest request) {
        try {
            return mapper.convertValue(restRepo.fetchResult(uri, request), IdGenerationResponse.class);
        } catch (CustomException e) {
            log.error("Custom Exception occurred in Idgen Utility :: {}", e.toString());
            throw e;
        } catch (Exception e) {
            log.error("Error fetching ID from ID generation service :: {}", e.toString());
            throw new CustomException(IDGEN_ERROR, "Error fetching ID from ID generation service");
        }
    }


}