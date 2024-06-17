package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.pucar.dristi.config.ServiceConstants.*;

@Slf4j
@Component
public class BillingUtil {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private Configuration configs;

	public void createDemand(CaseRequest caseRequest) {
		StringBuilder uri = new StringBuilder();
		uri.append(configs.getBillingHost()).append(configs.getDemandCreateEndPoint());

		DemandRequest demandRequest = new DemandRequest();
		demandRequest.setRequestInfo(caseRequest.getRequestInfo());
		Demand demand = new Demand();
		demand.setTenantId(caseRequest.getCases().getTenantId());
		demand.setConsumerCode(caseRequest.getCases().getFilingNumber());
		demand.setPayer(caseRequest.getRequestInfo().getUserInfo());
		demand.setTaxPeriodFrom(TAX_PERIOD_FROM);
		demand.setTaxPeriodTo(TAX_PERIOD_TO);

		DemandDetail demandDetail = new DemandDetail();
		demandDetail.setTaxAmount(TAX_AMOUNT);
		demandDetail.setTaxHeadMasterCode(TAX_HEADMASTER_CODE);
		demand.addDemandDetailsItem(demandDetail);
		demand.setBusinessService(configs.getCaseBusinessServiceName());
		demand.setConsumerType(configs.getCaseBusinessServiceName());
		Object response = new HashMap<>();
		try {
			response = restTemplate.postForObject(uri.toString(), demandRequest, Map.class);
			log.info("Demand response :: {}", response);
		} catch (Exception e) {
			log.error(ERROR_WHILE_CREATING_DEMAND_FOR_CASE, e);
			throw new CustomException(ERROR_WHILE_CREATING_DEMAND_FOR_CASE, e.getMessage());
		}
	}
}