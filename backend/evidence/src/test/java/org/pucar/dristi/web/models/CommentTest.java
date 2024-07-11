package org.pucar.dristi.web.models;

import org.egov.common.contract.models.AuditDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.UUID;

public class CommentTest {

    private Comment comment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        comment = new Comment();
    }

    @Test
    public void testGettersAndSetters() {
        UUID id = UUID.randomUUID();
        String tenantId = "testTenantId";
        String artifactId = "testArtifactId";
        String individualId = "testIndividualId";
        String commentText = "testComment";
        Boolean isActive = true;
        Object additionalDetails = "testAdditionalDetails";
        AuditDetails auditDetails = new AuditDetails();

        comment.setId(id);
        comment.setTenantId(tenantId);
        comment.setArtifactId(artifactId);
        comment.setIndividualId(individualId);
        comment.setComment(commentText);
        comment.setIsActive(isActive);
        comment.setAdditionalDetails(additionalDetails);
        comment.setAuditdetails(auditDetails);

        Assertions.assertEquals(id, comment.getId());
        Assertions.assertEquals(tenantId, comment.getTenantId());
        Assertions.assertEquals(artifactId, comment.getArtifactId());
        Assertions.assertEquals(individualId, comment.getIndividualId());
        Assertions.assertEquals(commentText, comment.getComment());
        Assertions.assertEquals(isActive, comment.getIsActive());
        Assertions.assertEquals(additionalDetails, comment.getAdditionalDetails());
        Assertions.assertEquals(auditDetails, comment.getAuditdetails());
    }

    // Add more test cases to achieve higher coverage and edge cases

}
