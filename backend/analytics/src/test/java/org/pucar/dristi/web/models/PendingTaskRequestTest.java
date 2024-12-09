package org.pucar.dristi.web.models;

import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.Test;
import org.pucar.dristi.web.models.PendingTask;
import org.pucar.dristi.web.models.PendingTaskRequest;

import static org.junit.jupiter.api.Assertions.*;

class PendingTaskRequestTest {

    @Test
    void testNoArgsConstructor() {
        PendingTaskRequest request = new PendingTaskRequest();
        assertNull(request.getRequestInfo());
        assertNotNull(request.getPendingTask());
    }

    @Test
    void testAllArgsConstructor() {
        RequestInfo requestInfo = new RequestInfo();
        PendingTask pendingTask = new PendingTask();
        PendingTaskRequest request = new PendingTaskRequest(requestInfo, pendingTask);

        assertEquals(requestInfo, request.getRequestInfo());
        assertEquals(pendingTask, request.getPendingTask());
    }

    @Test
    void testBuilder() {
        RequestInfo requestInfo = new RequestInfo();
        PendingTask pendingTask = new PendingTask();
        PendingTaskRequest request = PendingTaskRequest.builder()
                .requestInfo(requestInfo)
                .pendingTask(pendingTask)
                .build();

        assertEquals(requestInfo, request.getRequestInfo());
        assertEquals(pendingTask, request.getPendingTask());
    }

    @Test
    void testSettersAndGetters() {
        PendingTaskRequest request = new PendingTaskRequest();
        RequestInfo requestInfo = new RequestInfo();
        PendingTask pendingTask = new PendingTask();

        request.setRequestInfo(requestInfo);
        request.setPendingTask(pendingTask);

        assertEquals(requestInfo, request.getRequestInfo());
        assertEquals(pendingTask, request.getPendingTask());
    }

    @Test
    void testToString() {
        RequestInfo requestInfo = new RequestInfo();
        PendingTask pendingTask = new PendingTask();
        PendingTaskRequest request = PendingTaskRequest.builder()
                .requestInfo(requestInfo)
                .pendingTask(pendingTask)
                .build();

        String expected = "PendingTaskRequest(requestInfo=" + requestInfo + ", pendingTask=" + pendingTask + ")";
        assertEquals(expected, request.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        RequestInfo requestInfo1 = new RequestInfo();
        PendingTask pendingTask1 = new PendingTask();
        PendingTaskRequest request1 = new PendingTaskRequest(requestInfo1, pendingTask1);

        RequestInfo requestInfo2 = new RequestInfo();
        PendingTask pendingTask2 = new PendingTask();
        PendingTaskRequest request2 = new PendingTaskRequest(requestInfo2, pendingTask2);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());

        request2.setPendingTask(null);
        assertNotEquals(request1, request2);
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }
}
