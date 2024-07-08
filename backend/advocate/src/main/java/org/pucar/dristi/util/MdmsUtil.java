package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.*;

@Slf4j
@Component
public class MdmsUtil {

	private final RestTemplate restTemplate;
	private final ObjectMapper mapper;
	private final Configuration configs;

	@Autowired
	public MdmsUtil(RestTemplate restTemplate, ObjectMapper mapper, Configuration configs) {
		this.restTemplate = restTemplate;
		this.mapper = mapper;
		this.configs = configs;
	}

	public Map<String, Map<String, JSONArray>> fetchMdmsData(RequestInfo requestInfo, String tenantId,
			String moduleName, List<String> masterNameList) {
		StringBuilder uri = new StringBuilder();
		uri.append(configs.getMdmsHost()).append(configs.getMdmsEndPoint());
		MdmsCriteriaReq mdmsCriteriaReq = getMdmsRequest(requestInfo, tenantId, moduleName, masterNameList);
		log.info("MDMS Criteria :: {}",mdmsCriteriaReq);
		Object response = new HashMap<>();
		MdmsResponse mdmsResponse = new MdmsResponse();
		try {
			response = restTemplate.postForObject(uri.toString(), mdmsCriteriaReq, Map.class);
			mdmsResponse = mapper.convertValue(response, MdmsResponse.class);
		}
		catch (CustomException e) {
			log.error("Custom Exception occurred in MDMS Utility :: {}", e.toString());
			throw e;
		}
		catch (Exception e) {
			log.error(ERROR_WHILE_FETCHING_FROM_MDMS, e);
			throw new CustomException(ERROR_WHILE_FETCHING_FROM_MDMS,e.getMessage());
		}

		return mdmsResponse.getMdmsRes();
	}

	private MdmsCriteriaReq getMdmsRequest(RequestInfo requestInfo, String tenantId, String moduleName,
			List<String> masterNameList) {
		List<MasterDetail> masterDetailList = new ArrayList<>();
		for (String masterName : masterNameList) {
			MasterDetail masterDetail = new MasterDetail();
			masterDetail.setName(masterName);
			masterDetailList.add(masterDetail);
		}

		ModuleDetail moduleDetail = new ModuleDetail();
		moduleDetail.setMasterDetails(masterDetailList);
		moduleDetail.setModuleName(moduleName);
		List<ModuleDetail> moduleDetailList = new ArrayList<>();
		moduleDetailList.add(moduleDetail);

		MdmsCriteria mdmsCriteria = new MdmsCriteria();
		mdmsCriteria.setTenantId(tenantId.split("\\.")[0]);
		mdmsCriteria.setModuleDetails(moduleDetailList);

		MdmsCriteriaReq mdmsCriteriaReq = new MdmsCriteriaReq();
		mdmsCriteriaReq.setMdmsCriteria(mdmsCriteria);
		mdmsCriteriaReq.setRequestInfo(requestInfo);

		return mdmsCriteriaReq;
	}
}