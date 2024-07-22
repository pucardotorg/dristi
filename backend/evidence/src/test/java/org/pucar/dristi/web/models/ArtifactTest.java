package org.pucar.dristi.web.models;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.models.Workflow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArtifactTest {

    private Artifact artifact;

    @Mock
    private Comment comment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        artifact = new Artifact();
    }

    @Test
    public void testAddApplicableToItem() {
        artifact.addApplicableToItem("testApplicableToItem");
        List<String> applicableTo = artifact.getApplicableTo();
        Assertions.assertNotNull(applicableTo);
        Assertions.assertEquals(1, applicableTo.size());
        Assertions.assertEquals("testApplicableToItem", applicableTo.get(0));
    }

    @Test
    public void testAddCommentsItem() {
        artifact.addCommentsItem(comment);
        List<Comment> comments = artifact.getComments();
        Assertions.assertNotNull(comments);
        Assertions.assertEquals(1, comments.size());
        Assertions.assertEquals(comment, comments.get(0));
    }

    @Test
    public void testGettersAndSetters() {
        UUID id = UUID.randomUUID();
        String tenantId = "testTenantId";
        String artifactNumber = "testArtifactNumber";
        String evidenceNumber = "testEvidenceNumber";
        String externalRefNumber = "testExternalRefNumber";
        String caseId = "testCaseId";
        String application = "testApplication";
        String hearing = "testHearing";
        String order = "testOrder";
        String mediaType = "testMediaType";
        String artifactType = "testArtifactType";
        String sourceID = "testSourceID";
        String sourceName = "testSourceName";
        List<String> applicableTo = new ArrayList<>();
        applicableTo.add("testApplicableTo");
        Integer createdDate = 1234567890;
        Boolean isActive = true;
        String status = "testStatus";
        Document file = new Document();
        String description = "testDescription";
        Object artifactDetails = new Object();
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        Object additionalDetails = "testAdditionalDetails";
        AuditDetails auditDetails = new AuditDetails();
        Workflow workflow = new Workflow();

        artifact.setId(id);
        artifact.setTenantId(tenantId);
        artifact.setArtifactNumber(artifactNumber);
        artifact.setEvidenceNumber(evidenceNumber);
        artifact.setExternalRefNumber(externalRefNumber);
        artifact.setCaseId(caseId);
        artifact.setApplication(application);
        artifact.setHearing(hearing);
        artifact.setOrder(order);
        artifact.setMediaType(mediaType);
        artifact.setArtifactType(artifactType);
        artifact.setSourceID(sourceID);
        artifact.setSourceName(sourceName);
        artifact.setApplicableTo(applicableTo);
        artifact.setCreatedDate(createdDate);
        artifact.setIsActive(isActive);
        artifact.setStatus(status);
        artifact.setFile(file);
        artifact.setDescription(description);
        artifact.setArtifactDetails(artifactDetails);
        artifact.setComments(comments);
        artifact.setAdditionalDetails(additionalDetails);
        artifact.setAuditdetails(auditDetails);
        artifact.setWorkflow(workflow);

        Assertions.assertEquals(id, artifact.getId());
        Assertions.assertEquals(tenantId, artifact.getTenantId());
        Assertions.assertEquals(artifactNumber, artifact.getArtifactNumber());
        Assertions.assertEquals(evidenceNumber, artifact.getEvidenceNumber());
        Assertions.assertEquals(externalRefNumber, artifact.getExternalRefNumber());
        Assertions.assertEquals(caseId, artifact.getCaseId());
        Assertions.assertEquals(application, artifact.getApplication());
        Assertions.assertEquals(hearing, artifact.getHearing());
        Assertions.assertEquals(order, artifact.getOrder());
        Assertions.assertEquals(mediaType, artifact.getMediaType());
        Assertions.assertEquals(artifactType, artifact.getArtifactType());
        Assertions.assertEquals(sourceID, artifact.getSourceID());
        Assertions.assertEquals(sourceName, artifact.getSourceName());
        Assertions.assertEquals(applicableTo, artifact.getApplicableTo());
        Assertions.assertEquals(createdDate, artifact.getCreatedDate());
        Assertions.assertEquals(isActive, artifact.getIsActive());
        Assertions.assertEquals(status, artifact.getStatus());
        Assertions.assertEquals(file, artifact.getFile());
        Assertions.assertEquals(description, artifact.getDescription());
        Assertions.assertEquals(artifactDetails, artifact.getArtifactDetails());
        Assertions.assertEquals(comments, artifact.getComments());
        Assertions.assertEquals(additionalDetails, artifact.getAdditionalDetails());
        Assertions.assertEquals(auditDetails, artifact.getAuditdetails());
        Assertions.assertEquals(workflow, artifact.getWorkflow());
    }

    // Add more test cases to achieve higher coverage and edge cases

}
