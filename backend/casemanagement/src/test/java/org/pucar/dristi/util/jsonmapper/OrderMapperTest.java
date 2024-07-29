package org.pucar.dristi.util.jsonmapper;

import org.egov.common.contract.models.AuditDetails;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.Order;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderMapperTest {

	@Mock
	private JsonMapperUtil jsonMapperUtil;

	@InjectMocks
	private OrderMapper orderMapper;

	private JSONObject jsonObject;
	private JSONObject dataObject;
	private JSONObject orderDetailsObject;
	private Order order;
	private AuditDetails auditDetails;

	@BeforeEach
	void setUp() throws JSONException {
		jsonObject = new JSONObject();
		dataObject = new JSONObject();
		orderDetailsObject = new JSONObject();

		jsonObject.put("Data", dataObject);
		dataObject.put("orderDetails", orderDetailsObject);

		order = new Order();
		auditDetails = new AuditDetails();
	}

	@Test
	void testGetOrderSuccess() {
		when(jsonMapperUtil.map(orderDetailsObject, Order.class)).thenReturn(order);
		when(jsonMapperUtil.map(orderDetailsObject, AuditDetails.class)).thenReturn(auditDetails);

		Order result = orderMapper.getOrder(jsonObject);

		assertNotNull(result);
		assertEquals(order, result);
		assertEquals(auditDetails, result.getAuditDetails());
		verify(jsonMapperUtil).map(orderDetailsObject, Order.class);
		verify(jsonMapperUtil).map(orderDetailsObject, AuditDetails.class);
	}

	@Test
	void testGetOrderDataObjectNull() {
		JSONObject jsonObjectWithoutData = new JSONObject();

		Order result = orderMapper.getOrder(jsonObjectWithoutData);

		assertNull(result);
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(Order.class));
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(AuditDetails.class));
	}

	@Test
	void testGetOrderDetailsNull() {
		dataObject.remove("orderDetails");
		when(jsonMapperUtil.map(null, Order.class)).thenReturn(null);

		Order result = orderMapper.getOrder(jsonObject);

		assertNull(result);
		verify(jsonMapperUtil).map(null, Order.class);
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(AuditDetails.class));
	}

	@Test
	void testGetOrderOrderDetailsNotNullAuditDetailsNull() {
		when(jsonMapperUtil.map(orderDetailsObject, Order.class)).thenReturn(order);
		when(jsonMapperUtil.map(orderDetailsObject, AuditDetails.class)).thenReturn(null);

		Order result = orderMapper.getOrder(jsonObject);

		assertNotNull(result);
		assertEquals(order, result);
		assertNull(result.getAuditDetails());
		verify(jsonMapperUtil).map(orderDetailsObject, Order.class);
		verify(jsonMapperUtil).map(orderDetailsObject, AuditDetails.class);
	}
}
