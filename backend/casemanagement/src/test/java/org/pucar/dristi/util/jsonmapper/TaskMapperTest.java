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
import org.pucar.dristi.web.models.Task;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskMapperTest {

	@Mock
	private JsonMapperUtil jsonMapperUtil;

	@InjectMocks
	private TaskMapper taskMapper;

	private JSONObject jsonObject;
	private JSONObject dataObject;
	private JSONObject taskDetailsObject;
	private Task task;
	private AuditDetails auditDetails;

	@BeforeEach
	void setUp() throws JSONException {
		jsonObject = new JSONObject();
		dataObject = new JSONObject();
		taskDetailsObject = new JSONObject();

		jsonObject.put("Data", dataObject);
		dataObject.put("taskDetails", taskDetailsObject);

		task = new Task();
		auditDetails = new AuditDetails();
	}

	@Test
	void testGetTaskSuccess() {
		when(jsonMapperUtil.map(taskDetailsObject, Task.class)).thenReturn(task);
		when(jsonMapperUtil.map(taskDetailsObject, AuditDetails.class)).thenReturn(auditDetails);

		Task result = taskMapper.getTask(jsonObject);

		assertNotNull(result);
		assertEquals(task, result);
		assertEquals(auditDetails, result.getAuditDetails());
		verify(jsonMapperUtil).map(taskDetailsObject, Task.class);
		verify(jsonMapperUtil).map(taskDetailsObject, AuditDetails.class);
	}

	@Test
	void testGetTaskDataObjectNull() {
		JSONObject jsonObjectWithoutData = new JSONObject();

		Task result = taskMapper.getTask(jsonObjectWithoutData);

		assertNull(result);
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(Task.class));
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(AuditDetails.class));
	}

	@Test
	void testGetTaskDetailsNull() {
		dataObject.remove("taskDetails");
		when(jsonMapperUtil.map(null, Task.class)).thenReturn(null);

		Task result = taskMapper.getTask(jsonObject);

		assertNull(result);
		verify(jsonMapperUtil).map(null, Task.class);
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(AuditDetails.class));
	}

	@Test
	void testGetTaskTaskDetailsNotNullAuditDetailsNull() {
		when(jsonMapperUtil.map(taskDetailsObject, Task.class)).thenReturn(task);
		when(jsonMapperUtil.map(taskDetailsObject, AuditDetails.class)).thenReturn(null);

		Task result = taskMapper.getTask(jsonObject);

		assertNotNull(result);
		assertEquals(task, result);
		assertNull(result.getAuditDetails());
		verify(jsonMapperUtil).map(taskDetailsObject, Task.class);
		verify(jsonMapperUtil).map(taskDetailsObject, AuditDetails.class);
	}
}
