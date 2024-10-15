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
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.Comment;
import org.pucar.dristi.web.models.EvidenceRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EvidenceEnrichmentTest {

    @Mock
    private IdgenUtil idgenUtil;

    @Mock
    private Configuration configuration;

    @InjectMocks
    private EvidenceEnrichment evidenceEnrichment;

    private EvidenceRequest evidenceRequest;

    @BeforeEach
    void setUp() {
        evidenceRequest = new EvidenceRequest();
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid(UUID.randomUUID().toString());
        userInfo.setTenantId("tenantId");
        requestInfo.setUserInfo(userInfo);
        evidenceRequest.setRequestInfo(requestInfo);

        Artifact artifact = new Artifact();
        artifact.setComments(Collections.singletonList(new Comment()));
        evidenceRequest.setArtifact(artifact);
    }

    @Test
    void testEnrichEvidenceRegistration() {
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setTenantId("tenantId");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);
        evidenceRequest.setRequestInfo(requestInfo);
        Artifact artifact = new Artifact();
        artifact.setSourceType("COURT");
        artifact.setArtifactType("DOCUMENTARY");
        artifact.setFilingNumber("filing-number");
        // Ensure that comments are initialized to an empty list
        artifact.setComments(new ArrayList<>());
        evidenceRequest.setArtifact(artifact);

        // Mock idList and ensure it contains "artifactNumber"
        List<String> idList = new ArrayList<>();
        idList.add("artifactNumber");
        when(idgenUtil.getIdList(any(), any(), any(), any(), any(),any())).thenReturn(idList);
        when(configuration.getArtifactConfig()).thenReturn("config");
        when(configuration.getArtifactFormat()).thenReturn("testformat");

        // Call the method to be tested
        evidenceEnrichment.enrichEvidenceRegistration(evidenceRequest);

        // Verify that getIdList method is called with appropriate parameters
        verify(idgenUtil, times(1)).getIdList(any(), any(), any(), any(), any(),any());

        // Assert that the Artifact object and its attributes are modified as expected
        assertNotNull(artifact.getAuditdetails());
        assertNotNull(artifact.getId());
        assertNotNull(artifact.getArtifactNumber());
        assertTrue(artifact.getIsActive()); // Assuming set to false by default
        // Add more assertions as needed for other properties

        // If file is not null, ensure its properties are set correctly
        if (artifact.getFile() != null) {
            assertNotNull(artifact.getFile().getId());
            assertEquals(artifact.getFile().getId(), artifact.getFile().getDocumentUid());
        }
    }


    @Test
    void testEnrichEvidenceRegistrationUponUpdate() {
        // Arrange
        AuditDetails auditDetails = AuditDetails.builder().lastModifiedTime(0L).lastModifiedBy("oldUuid").build();
        evidenceRequest.getArtifact().setAuditdetails(auditDetails);

        // Act
        evidenceEnrichment.enrichEvidenceRegistrationUponUpdate(evidenceRequest);

        // Assert
        assertNotNull(evidenceRequest.getArtifact().getAuditdetails());
        assertTrue(evidenceRequest.getArtifact().getAuditdetails().getLastModifiedTime() > 0);
        assertEquals(evidenceRequest.getRequestInfo().getUserInfo().getUuid(), evidenceRequest.getArtifact().getAuditdetails().getLastModifiedBy());
    }

    @Test
    public void testEnrichIsActive() {
        // Arrange
        Artifact artifact = new Artifact();
        artifact.setSourceType("COURT");
        artifact.setArtifactType("DOCUMENTARY");

        evidenceRequest.setArtifact(artifact);

        // Act
        evidenceEnrichment.enrichIsActive(evidenceRequest);

        // Assert
        assertFalse(artifact.getIsActive());
        assertNotNull(artifact);
        assertNull(artifact.getEvidenceNumber()); // Check that unrelated fields are unchanged
    }

    @Test
    public void testEnrichIsActiveWithException() {
        // Arrange
        Artifact artifact = new Artifact();
        artifact.setSourceType("COURT");
        artifact.setArtifactType("DOCUMENTARY");

        Artifact artifactSpy = spy(artifact);
        doThrow(new RuntimeException("Update error")).when(artifactSpy).setIsActive(false);

        evidenceRequest.setArtifact(artifactSpy);

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () -> {
            evidenceEnrichment.enrichIsActive(evidenceRequest);
        });

        assertEquals("ENRICHMENT_EXCEPTION", thrown.getCode());
        assertTrue(thrown.getMessage().contains("Error in enrichment service during isActive status update process"));
        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getCause() == null || "Update error".equals(thrown.getCause().getMessage())); // Handle potential null
    }

}
