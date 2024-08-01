package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.Mdms;
import org.pucar.dristi.web.models.MdmsCriteriaReqV2;
import org.pucar.dristi.web.models.MdmsCriteriaV2;
import org.pucar.dristi.web.models.MdmsResponseV2;
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

	@Autowired
	public MdmsV2Util(RestTemplate restTemplate,
					  ObjectMapper mapper,
					  Configuration configs) {
		this.restTemplate = restTemplate;
		this.mapper = mapper;
		this.configs = configs;

	}


	public List<Mdms> fetchMdmsV2Data(RequestInfo requestInfo, String tenantId, Set<String> ids, Set<String> uniqueIdentifiers, String schemaCode, Boolean isActive) {
		StringBuilder uri = new StringBuilder();
		uri.append(configs.getMdmsHost()).append(configs.getMdmsEndPoint());
		MdmsCriteriaReqV2 mdmsCriteriaReqV2 = getMdmsV2Request(requestInfo, tenantId, ids, uniqueIdentifiers, schemaCode, isActive);
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

	public MdmsCriteriaReqV2 getMdmsV2Request(RequestInfo requestInfo, String tenantId, Set<String> ids, Set<String> uniqueIdentifiers, String schemaCode, Boolean isActive) {
		MdmsCriteriaV2 mdmsCriteriaV2 = new MdmsCriteriaV2();

		if (tenantId != null) mdmsCriteriaV2.setTenantId(tenantId);
		if (ids != null && !ids.isEmpty()) mdmsCriteriaV2.setIds(ids);
		if (uniqueIdentifiers != null && !uniqueIdentifiers.isEmpty())
			mdmsCriteriaV2.setUniqueIdentifiers(uniqueIdentifiers);
		if (schemaCode != null) mdmsCriteriaV2.setSchemaCode(schemaCode);
		if (isActive != null) mdmsCriteriaV2.setIsActive(isActive);

		return MdmsCriteriaReqV2.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteriaV2).build();
	}
}
