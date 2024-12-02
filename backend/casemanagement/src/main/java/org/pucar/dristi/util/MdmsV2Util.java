package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.pucar.dristi.config.ServiceConstants.ERROR_WHILE_FETCHING_FROM_MDMS;

@Component
@Slf4j
public class MdmsV2Util {

	private final RestTemplate restTemplate;

	private final ObjectMapper mapper;

	private final Configuration configs;

	private final ObjectMapper objectMapper;

	@Autowired
	public MdmsV2Util(RestTemplate restTemplate,
					  ObjectMapper mapper,
					  Configuration configs, ObjectMapper objectMapper) {
		this.restTemplate = restTemplate;
		this.mapper = mapper;
		this.configs = configs;
		this.objectMapper = objectMapper;
	}


	public List<Mdms> fetchMdmsV2Data(RequestInfo requestInfo, String tenantId, Set<String> ids, Set<String> uniqueIdentifiers, String schemaCode, Boolean isActive,Map<String, String> filters) {
		StringBuilder uri = new StringBuilder();
		uri.append(configs.getMdmsHost()).append(configs.getMdmsEndPoint());
		MdmsCriteriaReqV2 mdmsCriteriaReqV2 = getMdmsV2Request(requestInfo, tenantId, ids, uniqueIdentifiers, schemaCode, isActive,filters);
		Object response = new HashMap<>();
		MdmsResponseV2 mdmsResponseV2 = new MdmsResponseV2();
		try {
			response = restTemplate.postForObject(uri.toString(), mdmsCriteriaReqV2, Map.class);
			mdmsResponseV2 = mapper.convertValue(response, MdmsResponseV2.class);
		} catch (Exception e) {
			log.error(ERROR_WHILE_FETCHING_FROM_MDMS, e);
		}

		return mdmsResponseV2.getMdms();
	}

	public JsonNode fetchMdmsV2Schema(RequestInfo requestInfo, String tenantId, Set<String> ids, Set<String> uniqueIdentifiers, String schemaCode, Boolean isActive) {
		StringBuilder uri = new StringBuilder();
		uri.append(configs.getMdmsHost()).append(configs.getMdmsSchemaEndPoint());
		SchemaDefCriteria schemaDefinitionSearch= SchemaDefCriteria.builder().
				tenantId(tenantId)
				.codes(uniqueIdentifiers)
				.build();

		MdmsSearch mdmsSearchObject= MdmsSearch.builder()
				.requestInfo(requestInfo)
				.schemaDefCriteria(schemaDefinitionSearch)
				.build();
		Object response = new HashMap<>();
		JsonNode jsonNode = null;
		try {
			response = restTemplate.postForObject(uri.toString(), mdmsSearchObject, Map.class);
			String jsonString = objectMapper.writeValueAsString(response);

			JsonNode rootNode = objectMapper.readTree(jsonString);
			jsonNode = rootNode.path("SchemaDefinitions");
		} catch (Exception e) {
			log.error(ERROR_WHILE_FETCHING_FROM_MDMS, e);
		}

		return jsonNode;
	}

	public MdmsCriteriaReqV2 getMdmsV2Request(RequestInfo requestInfo, String tenantId, Set<String> ids, Set<String> uniqueIdentifiers, String schemaCode, Boolean isActive,Map<String, String> filters) {
		MdmsCriteriaV2 mdmsCriteriaV2 = new MdmsCriteriaV2();

		if (tenantId != null) mdmsCriteriaV2.setTenantId(tenantId);
		if (ids != null && !ids.isEmpty()) mdmsCriteriaV2.setIds(ids);
		if (uniqueIdentifiers != null && !uniqueIdentifiers.isEmpty())
			mdmsCriteriaV2.setUniqueIdentifiers(uniqueIdentifiers);
		if (schemaCode != null) mdmsCriteriaV2.setSchemaCode(schemaCode);
		if (isActive != null) mdmsCriteriaV2.setIsActive(isActive);
		if (filters!=null) mdmsCriteriaV2.setFilterMap(filters);

		return MdmsCriteriaReqV2.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteriaV2).build();
	}
}
