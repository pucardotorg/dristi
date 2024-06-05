package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.config.Configuration;
import org.egov.common.contract.idgen.IdGenerationRequest;
import org.egov.common.contract.idgen.IdGenerationResponse;
import org.egov.common.contract.idgen.IdRequest;
import org.egov.common.contract.idgen.IdResponse;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class IdgenUtil {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private ServiceRequestRepository restRepo;

	@Autowired
	private Configuration configs;

	/** To generate IDs based on idName and format
	 * @param requestInfo
	 * @param tenantId
	 * @param idName
	 * @param idformat
	 * @param count
	 * @return
	 */
	public List<String> getIdList(RequestInfo requestInfo, String tenantId, String idName, String idformat,
								  Integer count) {
		try {
			List<IdRequest> reqList = new ArrayList<>();
			for (int i = 0; i < count; i++) {
				reqList.add(IdRequest.builder().idName(idName).format(idformat).tenantId(tenantId).build());
			}

			IdGenerationRequest request = IdGenerationRequest.builder().idRequests(reqList).requestInfo(requestInfo)
					.build();
			StringBuilder uri = new StringBuilder(configs.getIdGenHost()).append(configs.getIdGenPath());
			IdGenerationResponse response;
			try {
				response = mapper.convertValue(restRepo.fetchResult(uri, request),
						IdGenerationResponse.class);
			}
			catch (CustomException e) {
				log.error("Custom Exception occurred in Idgen Utility :: {}", e.toString());
				throw e;
			}
			catch (Exception e) {
				log.error("Error fetching ID from ID generation service :: {}", e.toString());
				throw new CustomException(IDGEN_ERROR, "Error fetching ID from ID generation service");
			}

			List<IdResponse> idResponses = response.getIdResponses();

			if (CollectionUtils.isEmpty(idResponses))
				throw new CustomException(IDGEN_ERROR, NO_IDS_FOUND_ERROR);

			return idResponses.stream().map(IdResponse::getId).collect(Collectors.toList());
		} catch (CustomException e){
			log.error("Custom Exception occurred in calling Idgen :: {}", e.toString());
			throw e;
		} catch (Exception e){
			throw new CustomException(IDGEN_ERROR,"ERROR in IDGEN Service");
		}
	}
}