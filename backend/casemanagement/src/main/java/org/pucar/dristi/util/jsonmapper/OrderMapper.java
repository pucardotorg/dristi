package org.pucar.dristi.util.jsonmapper;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Workflow;
import org.json.JSONObject;
import org.pucar.dristi.web.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

	private final JsonMapperUtil jsonMapperUtil;

	@Autowired
	public OrderMapper(JsonMapperUtil jsonMapperUtil) {
		this.jsonMapperUtil = jsonMapperUtil;
	}

	public Order getOrder(JSONObject jsonObject) {
		JSONObject orderDetails = jsonObject.optJSONObject("Data").optJSONObject("orderDetails");
		JSONObject currentProcessInstance = jsonObject.optJSONObject("Data").optJSONObject("currentProcessInstance");

		Order order = jsonMapperUtil.map(orderDetails, Order.class);

		if (order != null) {
			order.setAuditDetails(jsonMapperUtil.map(orderDetails, AuditDetails.class));
			order.setWorkflow(jsonMapperUtil.map(currentProcessInstance, Workflow.class));
		}
		return order;
	}
}
