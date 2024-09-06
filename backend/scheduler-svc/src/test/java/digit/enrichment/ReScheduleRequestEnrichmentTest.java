package digit.enrichment;

import digit.config.Configuration;
import digit.models.coremodels.AuditDetails;
import digit.util.IdgenUtil;
import digit.web.models.ReScheduleHearing;
import digit.web.models.ReScheduleHearingRequest;
import digit.web.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReScheduleRequestEnrichmentTest {

    @InjectMocks
    private ReScheduleRequestEnrichment reScheduleRequestEnrichment;

    @Mock
    private IdgenUtil idgenUtil;

    @Mock
    private Configuration configuration;

    private RequestInfo requestInfo;
    private ReScheduleHearingRequest reScheduleHearingRequest;
    private List<ReScheduleHearing> reScheduleHearings;

    @BeforeEach
    public void setUp() {
        User user = mock(User.class);
        Mockito.lenient().when(user.getUuid()).thenReturn("test-uuid");

        requestInfo = mock(RequestInfo.class);
        Mockito.lenient().when(requestInfo.getUserInfo()).thenReturn(user);

        reScheduleHearings = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ReScheduleHearing hearing = new ReScheduleHearing();
            hearing.setTenantId("test-tenant");
            reScheduleHearings.add(hearing);
        }

        reScheduleHearingRequest = new ReScheduleHearingRequest();
        reScheduleHearingRequest.setRequestInfo(requestInfo);
        reScheduleHearingRequest.setReScheduleHearing(reScheduleHearings);
    }

    @Test
    public void testEnrichRescheduleRequest() {


        reScheduleRequestEnrichment.enrichRescheduleRequest(reScheduleHearingRequest);

        assertNotNull(reScheduleHearings);
        assertEquals(3, reScheduleHearings.size());

        for (int i = 0; i < reScheduleHearings.size(); i++) {
            ReScheduleHearing hearing = reScheduleHearings.get(i);
            assertNotNull(hearing.getAuditDetails());
            assertEquals("test-uuid", hearing.getAuditDetails().getCreatedBy());
            assertEquals("test-uuid", hearing.getAuditDetails().getLastModifiedBy());
            assertEquals(1, hearing.getRowVersion());
        }
    }

    @Test
    public void testEnrichRequestOnUpdate() {
        List<ReScheduleHearing> existingReScheduleHearings = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ReScheduleHearing hearing = new ReScheduleHearing();
            hearing.setRescheduledRequestId("ID" + (i + 1));
            hearing.setRowVersion(i);
            hearing.setAuditDetails(new AuditDetails("old-uuid", "admin", 2L, 0L));
            existingReScheduleHearings.add(hearing);
        }

        reScheduleHearings.get(0).setAvailableAfter(LocalDate.now().toEpochDay());
        reScheduleHearings.get(0).setRowVersion(1);

        reScheduleHearings.get(1).setAvailableAfter(LocalDate.now().toEpochDay());
        reScheduleHearings.get(1).setRowVersion(1);

        reScheduleHearings.get(2).setAvailableAfter(LocalDate.now().toEpochDay());
        reScheduleHearings.get(2).setRowVersion(1);

        reScheduleRequestEnrichment.enrichRescheduleRequest(reScheduleHearingRequest);

        for (int i = 0; i < existingReScheduleHearings.size(); i++) {
            ReScheduleHearing hearing = existingReScheduleHearings.get(i);
            assertEquals("admin", hearing.getAuditDetails().getLastModifiedBy());
            assertTrue(hearing.getAuditDetails().getLastModifiedTime() <= System.currentTimeMillis());
        }
    }

    @Test
    public void testEnrichRescheduleRequest_WithNullRequestInfo() {
        reScheduleHearingRequest.setRequestInfo(null);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            reScheduleRequestEnrichment.enrichRescheduleRequest(reScheduleHearingRequest);
        });

        assertNotNull(exception);
    }

    @Test
    public void testEnrichRequestOnUpdate_WithNullExistingHearingList() {
        reScheduleHearingRequest.setReScheduleHearing(null);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            reScheduleRequestEnrichment.enrichRescheduleRequest(reScheduleHearingRequest);
        });

        assertNotNull(exception);
    }
}

