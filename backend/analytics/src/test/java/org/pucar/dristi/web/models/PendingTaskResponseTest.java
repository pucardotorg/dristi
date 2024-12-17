package org.pucar.dristi.web.models;

import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.Test;
import org.pucar.dristi.web.models.PendingTask;
import org.pucar.dristi.web.models.PendingTaskResponse;

import static org.junit.jupiter.api.Assertions.*;

class PendingTaskResponseTest {

    @Test
    void testNoArgsConstructor() {
        PendingTaskResponse response = new PendingTaskResponse();
        assertNull(response.getResponseInfo());
        assertNull(response.getPendingTask());
    }

    @Test
    void testAllArgsConstructor() {
        ResponseInfo responseInfo = new ResponseInfo();
        PendingTask pendingTask = new PendingTask();
        PendingTaskResponse response = new PendingTaskResponse(responseInfo, pendingTask);

        assertEquals(responseInfo, response.getResponseInfo());
        assertEquals(pendingTask, response.getPendingTask());
    }

    @Test
    void testBuilder() {
        ResponseInfo responseInfo = new ResponseInfo();
        PendingTask pendingTask = new PendingTask();
        PendingTaskResponse response = PendingTaskResponse.builder()
                .responseInfo(responseInfo)
                .pendingTask(pendingTask)
                .build();

        assertEquals(responseInfo, response.getResponseInfo());
        assertEquals(pendingTask, response.getPendingTask());
    }

    @Test
    void testSettersAndGetters() {
        PendingTaskResponse response = new PendingTaskResponse();
        ResponseInfo responseInfo = new ResponseInfo();
        PendingTask pendingTask = new PendingTask();

        response.setResponseInfo(responseInfo);
        response.setPendingTask(pendingTask);

        assertEquals(responseInfo, response.getResponseInfo());
        assertEquals(pendingTask, response.getPendingTask());
    }

    @Test
    void testToString() {
        ResponseInfo responseInfo = new ResponseInfo();
        PendingTask pendingTask = new PendingTask();
        PendingTaskResponse response = PendingTaskResponse.builder()
                .responseInfo(responseInfo)
                .pendingTask(pendingTask)
                .build();

        String expected = "PendingTaskResponse(responseInfo=" + responseInfo + ", pendingTask=" + pendingTask + ")";
        assertEquals(expected, response.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        ResponseInfo responseInfo1 = new ResponseInfo();
        PendingTask pendingTask1 = new PendingTask();
        PendingTaskResponse response1 = new PendingTaskResponse(responseInfo1, pendingTask1);

        ResponseInfo responseInfo2 = new ResponseInfo();
        PendingTask pendingTask2 = new PendingTask();
        PendingTaskResponse response2 = new PendingTaskResponse(responseInfo2, pendingTask2);

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());

        response2.setPendingTask(null);
        assertNotEquals(response1, response2);
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }
}
