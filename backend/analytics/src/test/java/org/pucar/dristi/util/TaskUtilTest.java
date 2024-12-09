package org.pucar.dristi.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.util.TaskUtil;
import org.pucar.dristi.util.Util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.pucar.dristi.config.ServiceConstants.TASK_PATH;

@ExtendWith(MockitoExtension.class)
class TaskUtilTest {

    @Mock
    private Configuration config;

    @Mock
    private ServiceRequestRepository repository;

    @Mock
    private Util util;

    @InjectMocks
    private TaskUtil taskUtil;

    private JSONObject request;
    private String tenantId;
    private String taskNumber;
    private String url;

    @BeforeEach
    void setUp() {
        request = new JSONObject();
        tenantId = "tenant1";
        taskNumber = "task123";
        url = "http://example.com/task/search";

        when(config.getTaskHost()).thenReturn("http://example.com");
        when(config.getTaskSearchPath()).thenReturn("/task/search");
    }

    @Test
    void testGetTask_Success() throws Exception {
        // Prepare test data
        String response = "{ \"task\": [ { \"id\": \"task123\" } ] }";
        JSONArray taskArray = new JSONArray();
        taskArray.put(new JSONObject().put("id", "task123"));

        // Mocking
        when(repository.fetchResult(any(StringBuilder.class), any(JSONObject.class))).thenReturn(response);
        when(util.constructArray(response, TASK_PATH)).thenReturn(taskArray);

        // Call the method
        Object result = taskUtil.getTask(request, tenantId, taskNumber, null, null);

        // Verify
        assertEquals(taskArray.get(0), result);

        // Verify the URL and request
        ArgumentCaptor<StringBuilder> urlCaptor = ArgumentCaptor.forClass(StringBuilder.class);
        ArgumentCaptor<JSONObject> requestCaptor = ArgumentCaptor.forClass(JSONObject.class);
        verify(repository).fetchResult(urlCaptor.capture(), requestCaptor.capture());

        assertEquals(url, urlCaptor.getValue().toString());

        JSONObject expectedRequest = new JSONObject();
        expectedRequest.put("tenantId", tenantId);
        JSONObject criteria = new JSONObject();
        criteria.put("taskNumber", taskNumber);
        criteria.put("tenantId", tenantId);
        expectedRequest.put("criteria", criteria);

        assertEquals(expectedRequest.toString(), requestCaptor.getValue().toString());
    }

    @Test
    void testGetTask_NoTaskFound() throws Exception {
        // Prepare test data
        String response = "{ \"task\": [] }";
        JSONArray taskArray = new JSONArray();

        // Mocking
        when(repository.fetchResult(any(StringBuilder.class), any(JSONObject.class))).thenReturn(response);
        when(util.constructArray(response, TASK_PATH)).thenReturn(taskArray);

        // Call the method
        Object result = taskUtil.getTask(request, tenantId, taskNumber, null, null);

        // Verify
        assertNull(result);
    }

    @Test
    void testGetTask_Exception() {

        // Mocking
        when(repository.fetchResult(any(StringBuilder.class), any(JSONObject.class))).thenThrow(new RuntimeException("Error while fetching or processing the task response"));

        // Call the method and expect exception
        try {
            taskUtil.getTask(request, tenantId, taskNumber, null, null);
        } catch (RuntimeException e) {
            assertEquals("Error while fetching or processing the task response", e.getMessage());
        }
    }
}
