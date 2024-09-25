package org.pucar.dristi.enrichment;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.Amount;
import org.pucar.dristi.web.models.Task;
import org.pucar.dristi.web.models.TaskRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.ENRICHMENT_EXCEPTION;

@ExtendWith(MockitoExtension.class)
public class TaskRegistrationEnrichmentTest {

    @Mock
    private IdgenUtil idgenUtil;

    @Mock
    private Configuration config;

    @InjectMocks
    private TaskRegistrationEnrichment taskRegistrationEnrichment;

    private TaskRequest taskRequest;
    private Task task;
    private RequestInfo requestInfo;
    private User userInfo;

    @BeforeEach
    void setUp() {
        userInfo = User.builder().uuid("user-uuid").build();
        requestInfo = RequestInfo.builder().userInfo(userInfo).build();
        task = new Task();
        task.setTenantId("tenant-id");
        task.setAmount(new Amount());
        taskRequest = new TaskRequest();
        taskRequest.setRequestInfo(requestInfo);
        taskRequest.setTask(task);
    }

//    @Test
//    void testEnrichTaskRegistrationSuccess() {
//        when(idgenUtil.getIdList(any(), any(), any(), any(), anyInt(),any())).thenReturn(Collections.singletonList("task-id"));
//        when(config.getTaskNumber()).thenReturn("task-number");
//
//        taskRegistrationEnrichment.enrichTaskRegistration(taskRequest);
//
//        assertNotNull(task.getId());
//        assertNotNull(task.getAuditDetails());
//        assertEquals("task-id", task.getTaskNumber());
//    }

    @Test
    void testEnrichTaskRegistrationException() {
        when(idgenUtil.getIdList(any(), any(), any(), any(), anyInt(),any())).thenThrow(new RuntimeException("Error"));

        CustomException exception = assertThrows(CustomException.class, () -> taskRegistrationEnrichment.enrichTaskRegistration(taskRequest));
        assertEquals(ENRICHMENT_EXCEPTION, exception.getCode());
        assertEquals("Error", exception.getMessage());
        verify(idgenUtil, times(1)).getIdList(any(), any(), any(), any(), anyInt(),any());
    }

    @Test
    void testEnrichCaseApplicationUponUpdateSuccess() {
        AuditDetails auditDetails = AuditDetails.builder().build();
        task.setAuditDetails(auditDetails);

        taskRegistrationEnrichment.enrichCaseApplicationUponUpdate(taskRequest);

        assertEquals("user-uuid", task.getAuditDetails().getLastModifiedBy());
        assertNotNull(task.getAuditDetails().getLastModifiedTime());
    }

    @Test
    void testEnrichCaseApplicationUponUpdateException() {
        task.setAuditDetails(null); // This will cause NullPointerException

        CustomException exception = assertThrows(CustomException.class, () -> taskRegistrationEnrichment.enrichCaseApplicationUponUpdate(taskRequest));
        assertEquals(ENRICHMENT_EXCEPTION, exception.getCode());
        assertTrue(exception.getMessage().contains("Exception in task enrichment service during task update process"));
    }
}
