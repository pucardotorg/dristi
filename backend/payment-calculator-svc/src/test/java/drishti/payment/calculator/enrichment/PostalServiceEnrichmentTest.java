package drishti.payment.calculator.enrichment;

import digit.models.coremodels.AuditDetails;
import drishti.payment.calculator.web.models.PostalServiceRequest;
import drishti.payment.calculator.web.models.PostalService;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PostalServiceEnrichmentTest {

    private PostalServiceEnrichment postalServiceEnrichment;
    private PostalServiceRequest request;
    private RequestInfo requestInfo;
    private User user;
    private PostalService postalService;

    @BeforeEach
    void setUp() {
        postalServiceEnrichment = new PostalServiceEnrichment();

        user = User.builder().uuid(UUID.randomUUID().toString()).build();
        requestInfo = RequestInfo.builder().userInfo(user).build();

        postalService = new PostalService();

        request = new PostalServiceRequest();
        request.setRequestInfo(requestInfo);
        request.setPostalServices(Arrays.asList(postalService));
    }

    @Test
    void testEnrichPostalServiceRequest() {
        postalServiceEnrichment.enrichPostalServiceRequest(request);

        assertNotNull(postalService.getPostalServiceId());
        assertEquals(1, postalService.getRowVersion());

        AuditDetails auditDetails = postalService.getAuditDetails();
        assertNotNull(auditDetails);
        assertEquals(user.getUuid(), auditDetails.getCreatedBy());
        assertEquals(user.getUuid(), auditDetails.getLastModifiedBy());
        assertNotNull(auditDetails.getCreatedTime());
        assertNotNull(auditDetails.getLastModifiedTime());
    }

    @Test
    void testEnrichExistingPostalServiceRequest() {
        AuditDetails auditDetails = AuditDetails.builder()
                .createdBy("oldUser")
                .createdTime(System.currentTimeMillis() - 10000)
                .lastModifiedBy("oldUser")
                .lastModifiedTime(System.currentTimeMillis() - 10000)
                .build();

        postalService.setAuditDetails(auditDetails);
        postalService.setRowVersion(1);

        postalServiceEnrichment.enrichExistingPostalServiceRequest(request);

        assertEquals(2, postalService.getRowVersion());

        AuditDetails updatedAuditDetails = postalService.getAuditDetails();
        assertEquals(user.getUuid(), updatedAuditDetails.getLastModifiedBy());
        assertNotNull(updatedAuditDetails.getLastModifiedTime());
    }
}
