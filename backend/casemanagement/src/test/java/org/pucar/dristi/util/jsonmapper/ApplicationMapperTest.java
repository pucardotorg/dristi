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
import org.pucar.dristi.web.models.Application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationMapperTest {

	@Mock
	private JsonMapperUtil jsonMapperUtil;

	@InjectMocks
	private ApplicationMapper applicationMapper;

	private JSONObject jsonObject;
	private JSONObject dataObject;
	private JSONObject applicationDetails;
	private JSONObject auditDetails;
	private Application application;
	private AuditDetails auditDetailsObj;

	@BeforeEach
	void setUp() throws JSONException {
		jsonObject = new JSONObject();
		dataObject = new JSONObject();
		applicationDetails = new JSONObject();
		auditDetails = new JSONObject();

		jsonObject.put("Data", dataObject);
		dataObject.put("applicationDetails", applicationDetails);
		dataObject.put("auditDetails", auditDetails);

		application = new Application();
		auditDetailsObj = new AuditDetails();
	}

	@Test
	void testGetApplicationSuccess() {
		when(jsonMapperUtil.map(applicationDetails, Application.class)).thenReturn(application);
		when(jsonMapperUtil.map(auditDetails, AuditDetails.class)).thenReturn(auditDetailsObj);

		Application result = applicationMapper.getApplication(jsonObject);

		assertNotNull(result);
		assertEquals(application, result);
		assertEquals(auditDetailsObj, result.getAuditDetails());
		verify(jsonMapperUtil).map(applicationDetails, Application.class);
		verify(jsonMapperUtil).map(auditDetails, AuditDetails.class);
	}

	@Test
	void testGetApplicationDataObjectNull() {
		JSONObject jsonObjectWithoutData = new JSONObject();

		Application result = applicationMapper.getApplication(jsonObjectWithoutData);

		assertNull(result);
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(Application.class));
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(AuditDetails.class));
	}

	@Test
	void testGetApplicationDetailsNull() {
		dataObject.remove("applicationDetails");
		when(jsonMapperUtil.map(null, Application.class)).thenReturn(null);

		Application result = applicationMapper.getApplication(jsonObject);

		assertNull(result);
		verify(jsonMapperUtil).map(null, Application.class);
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(AuditDetails.class));
	}

	@Test
	void testGetAuditDetailsNull() {
		dataObject.remove("auditDetails");
		when(jsonMapperUtil.map(applicationDetails, Application.class)).thenReturn(application);
		when(jsonMapperUtil.map(null, AuditDetails.class)).thenReturn(null);

		Application result = applicationMapper.getApplication(jsonObject);

		assertNotNull(result);
		assertEquals(application, result);
		assertNull(result.getAuditDetails());
		verify(jsonMapperUtil).map(applicationDetails, Application.class);
		verify(jsonMapperUtil).map(null, AuditDetails.class);
	}
}
