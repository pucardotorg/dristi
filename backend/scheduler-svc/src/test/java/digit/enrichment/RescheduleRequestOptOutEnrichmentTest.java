package digit.enrichment;

import digit.models.coremodels.AuditDetails;
import digit.web.models.OptOut;
import digit.web.models.OptOutRequest;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RescheduleRequestOptOutEnrichmentTest {

    @InjectMocks
    private RescheduleRequestOptOutEnrichment rescheduleRequestOptOutEnrichment;

    @Mock
    private RequestInfo requestInfo;

    @Mock
    private User user;

    private OptOutRequest optOutRequest;
    private OptOut optOutApplications;

    @BeforeEach
    public void setUp() {
        Mockito.lenient().when(user.getUuid()).thenReturn("test-uuid");
        Mockito.lenient().when(requestInfo.getUserInfo()).thenReturn(user);

        optOutApplications = new OptOut();

        optOutRequest = new OptOutRequest();
        optOutRequest.setRequestInfo(requestInfo);
        optOutRequest.setOptOut(optOutApplications);
    }

    @Test
    public void testEnrichCreateRequest() {
        rescheduleRequestOptOutEnrichment.enrichCreateRequest(optOutRequest);

        assertNotNull(optOutApplications);
            assertNotNull(optOutApplications.getAuditDetails());
            assertEquals("test-uuid", optOutApplications.getAuditDetails().getCreatedBy());
            assertEquals("test-uuid", optOutApplications.getAuditDetails().getLastModifiedBy());
            assertEquals(1, optOutApplications.getRowVersion());
    }

    @Test
    public void testenrichCreateRequest() {
            AuditDetails auditDetails = new AuditDetails("old-uuid", "admin", 1L, 1L);
        optOutApplications.setAuditDetails(auditDetails);
        optOutApplications.setRowVersion(1);

        rescheduleRequestOptOutEnrichment.enrichCreateRequest(optOutRequest);

            assertNotNull(optOutApplications.getAuditDetails());
            assertEquals("test-uuid", optOutApplications.getAuditDetails().getLastModifiedBy());
            assertTrue(optOutApplications.getAuditDetails().getLastModifiedTime() <= System.currentTimeMillis());
            assertEquals(1, optOutApplications.getRowVersion());
    }

    @Test
    public void testEnrichCreateRequest_WithNullRequestInfo() {
        optOutRequest.setRequestInfo(null);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            rescheduleRequestOptOutEnrichment.enrichCreateRequest(optOutRequest);
        });

        assertNotNull(exception);
    }

    @Test
    public void testEnrichCreateRequest_WithEmptyOptOuts() {

        rescheduleRequestOptOutEnrichment.enrichCreateRequest(optOutRequest);
        optOutRequest.setOptOut(new OptOut());

        assertEquals(optOutRequest.getOptOut(), new OptOut());
    }

    @Test
    public void testenrichCreateRequest_WithNullRequestInfo() {
        optOutRequest.setRequestInfo(null);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            rescheduleRequestOptOutEnrichment.enrichCreateRequest(optOutRequest);
        });

        assertNotNull(exception);
    }

}
