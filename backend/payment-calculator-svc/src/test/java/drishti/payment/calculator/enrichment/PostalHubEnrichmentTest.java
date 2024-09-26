package drishti.payment.calculator.enrichment;

import digit.models.coremodels.AuditDetails;
import drishti.payment.calculator.web.models.PostalHub;
import drishti.payment.calculator.web.models.PostalHubRequest;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostalHubEnrichmentTest {

    @InjectMocks
    private PostalHubEnrichment postalHubEnrichment;

    private PostalHubRequest request;
    private RequestInfo requestInfo;
    private User user;
    private PostalHub postalHub;

    @BeforeEach
    void setUp() {
        postalHubEnrichment = new PostalHubEnrichment();

        user = User.builder().uuid(UUID.randomUUID().toString()).build();
        requestInfo = RequestInfo.builder().userInfo(user).build();

        postalHub = new PostalHub();

        request = new PostalHubRequest();
        request.setRequestInfo(requestInfo);
        request.setPostalHubs(Collections.singletonList(postalHub));
    }

    @Test
    void testEnrichPostalHubRequest() {
        postalHubEnrichment.enrichPostalHubRequest(request);

        assertNotNull(postalHub.getHubId());
        assertEquals(1, postalHub.getRowVersion());

        AuditDetails auditDetails = postalHub.getAuditDetails();
        assertNotNull(auditDetails);
        assertEquals(user.getUuid(), auditDetails.getCreatedBy());
        assertEquals(user.getUuid(), auditDetails.getLastModifiedBy());
        assertNotNull(auditDetails.getCreatedTime());
        assertNotNull(auditDetails.getLastModifiedTime());
    }

    @Test
    void testEnrichExistingPostalHubRequest() {
        AuditDetails auditDetails = AuditDetails.builder()
                .createdBy("oldUser")
                .createdTime(System.currentTimeMillis() - 10000)
                .lastModifiedBy("oldUser")
                .lastModifiedTime(System.currentTimeMillis() - 10000)
                .build();

        postalHub.setAuditDetails(auditDetails);
        postalHub.setRowVersion(1);

        postalHubEnrichment.enrichExistingPostalHubRequest(request);

        assertEquals(2, postalHub.getRowVersion());

        AuditDetails updatedAuditDetails = postalHub.getAuditDetails();
        assertEquals(user.getUuid(), updatedAuditDetails.getLastModifiedBy());
        assertNotNull(updatedAuditDetails.getLastModifiedTime());
    }
}
