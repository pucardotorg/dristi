package digit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import digit.config.Configuration;
import digit.repository.ServiceRequestRepository;
import digit.web.models.PendingTask;
import digit.web.models.PendingTaskRequest;
import digit.web.models.ReScheduleHearing;
import digit.web.models.hearing.Hearing;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.ServiceCallException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PendingTaskUtilTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Configuration configuration;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private DateUtil dateUtil;

    @InjectMocks
    private PendingTaskUtil pendingTaskUtil;

    @Test
    public void testCreatePendingTask() {
        ReScheduleHearing reScheduleHearing = new ReScheduleHearing();
        reScheduleHearing.setTenantId("tenantId");
        reScheduleHearing.setRescheduledRequestId("rescheduledRequestId");
        reScheduleHearing.setCaseId("caseId");

        PendingTask pendingTask = pendingTaskUtil.createPendingTask(reScheduleHearing);

        assertEquals("MANUAL_"+reScheduleHearing.getRescheduledRequestId(), pendingTask.getReferenceId());
        assertEquals(reScheduleHearing.getCaseId(), pendingTask.getFilingNumber());
    }

    @Test
    public void testCallAnalytics_Success() {
        PendingTaskRequest request = new PendingTaskRequest();
        PendingTask pendingTask = new PendingTask();
        pendingTask.setTenantId("tenantId");
        pendingTask.setReferenceId("referenceId");
        pendingTask.setFilingNumber("filingNumber");

        request.setPendingTask(pendingTask);
        request.setRequestInfo(new RequestInfo());

        when(configuration.getAnalyticsHost()).thenReturn("http://analytics-host");
        when(configuration.getAnalyticsEndpoint()).thenReturn("/createTask");
        when(serviceRequestRepository.fetchResult(any(), any())).thenReturn(null);

        pendingTaskUtil.callAnalytics(request);

        verify(objectMapper).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        verify(serviceRequestRepository, times(1)).fetchResult(any(), any());
    }

    @Test
    public void testCallAnalytics_Failure() {
        PendingTaskRequest request = new PendingTaskRequest();
        PendingTask pendingTask = new PendingTask();
        request.setRequestInfo(new RequestInfo());
        request.setPendingTask(pendingTask);

        when(configuration.getAnalyticsHost()).thenReturn("http://hearing-host");
        when(configuration.getAnalyticsEndpoint()).thenReturn("/hearing/_update");
        when(serviceRequestRepository.fetchResult(any(), any())).thenThrow(new ServiceCallException("ServiceCallException"));

        pendingTaskUtil.callAnalytics(request);
    }
}
