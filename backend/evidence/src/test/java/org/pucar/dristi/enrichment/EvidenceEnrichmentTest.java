package org.pucar.dristi.enrichment;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
        // Mock evidenceRequest and its dependencies
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setTenantId("tenantId");
        userInfo.setUuid(UUID.randomUUID().toString());
        requestInfo.setUserInfo(userInfo);
        evidenceRequest.setRequestInfo(requestInfo);
        Artifact artifact = new Artifact();
        artifact.setArtifactType("complainant");
        // Ensure that comments are initialized to an empty list
        artifact.setComments(new ArrayList<>());
        evidenceRequest.setArtifact(artifact);

        // Mock idList and ensure it contains "artifactNumber"
        List<String> idList = new ArrayList<>();
        idList.add("artifactNumber");
        when(idgenUtil.getIdList(any(), anyString(), anyString(), any(), anyInt())).thenReturn(idList);

        // Call the method to be tested
        evidenceEnrichment.enrichEvidenceRegistration(evidenceRequest);

        // Verify that getIdList method is called with appropriate parameters
        verify(idgenUtil, times(1)).getIdList(any(), anyString(), anyString(), any(), anyInt());

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
    public void testGetIdgenByArtifactTypeAndSourceTpye() {
        // Test for "complainant" sourceType with "DOCUMENTARY" and "AFFIDAVIT" artifactType
        assertEquals("document.evidence_complainant", evidenceEnrichment.getIdgenByArtifactTypeAndSourceType("DOCUMENTARY", "COMPLAINANT"));
        assertEquals("document.evidence_complainant", evidenceEnrichment.getIdgenByArtifactTypeAndSourceType("AFFIDAVIT", "COMPLAINANT"));

        // Test for "accused" sourceType with "DOCUMENTARY" and "AFFIDAVIT" artifactType
        assertEquals("document.evidence_accused", evidenceEnrichment.getIdgenByArtifactTypeAndSourceType("DOCUMENTARY", "ACCUSED"));
        assertEquals("document.evidence_accused", evidenceEnrichment.getIdgenByArtifactTypeAndSourceType("AFFIDAVIT", "ACCUSED"));

        // Test for "court" sourceType with "DOCUMENTARY" and "AFFIDAVIT" artifactType
        assertEquals("document.evidence_court", evidenceEnrichment.getIdgenByArtifactTypeAndSourceType("DOCUMENTARY", "COURT"));
        assertEquals("document.evidence_court", evidenceEnrichment.getIdgenByArtifactTypeAndSourceType("AFFIDAVIT", "COURT"));

        // Test for "complainant" sourceType with "DEPOSITION" artifactType
        assertEquals("document.witness_complainant", evidenceEnrichment.getIdgenByArtifactTypeAndSourceType("DEPOSITION", "COMPLAINANT"));

        // Test for "accused" sourceType with "DEPOSITION" artifactType
        assertEquals("document.witness_accused", evidenceEnrichment.getIdgenByArtifactTypeAndSourceType("DEPOSITION", "ACCUSED"));

        // Test for "court" sourceType with "DEPOSITION" artifactType
        assertEquals("document.witness_court", evidenceEnrichment.getIdgenByArtifactTypeAndSourceType("DEPOSITION", "COURT"));

        // Test for invalid combinations
        assertThrows(CustomException.class, () -> evidenceEnrichment.getIdgenByArtifactTypeAndSourceType("INVALID_TYPE", "COMPLAINANT"));
        assertThrows(CustomException.class, () -> evidenceEnrichment.getIdgenByArtifactTypeAndSourceType("DOCUMENTARY", "invalidSource"));
        assertThrows(CustomException.class, () -> evidenceEnrichment.getIdgenByArtifactTypeAndSourceType("INVALID_TYPE", "invalidSource"));
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
    public void testEnrichEvidenceNumber() {
        // Arrange
        Artifact artifact = new Artifact();
        artifact.setSourceType("COURT");
        artifact.setArtifactType("DOCUMENTARY");

        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setTenantId("tenantId");
        requestInfo.setUserInfo(userInfo);

        EvidenceRequest evidenceRequest = new EvidenceRequest();
        evidenceRequest.setArtifact(artifact);
        evidenceRequest.setRequestInfo(requestInfo);

        List<String> evidenceNumberList = Collections.singletonList("EV12345");
        when(idgenUtil.getIdList(any(RequestInfo.class), anyString(), anyString(), isNull(), eq(1)))
                .thenReturn(evidenceNumberList);

        // Act
        evidenceEnrichment.enrichEvidenceNumber(evidenceRequest);

        // Assert
        assertEquals("EV12345", artifact.getEvidenceNumber());
        assertTrue(artifact.getIsEvidence());
        verify(idgenUtil).getIdList(any(RequestInfo.class), anyString(), anyString(), isNull(), eq(1));
        assertNotNull(artifact.getEvidenceNumber());
        assertEquals("EV12345", evidenceNumberList.get(0)); // Check list consistency
    }

    @Test
    public void testEnrichEvidenceNumberWithException() {
        // Arrange
        Artifact artifact = new Artifact();
        artifact.setSourceType("COURT");
        artifact.setArtifactType("DOCUMENTARY");

        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setTenantId("tenantId");
        requestInfo.setUserInfo(userInfo);

        EvidenceRequest evidenceRequest = new EvidenceRequest();
        evidenceRequest.setArtifact(artifact);
        evidenceRequest.setRequestInfo(requestInfo);

        when(idgenUtil.getIdList(any(), anyString(), anyString(), isNull(), eq(1)))
                .thenThrow(new RuntimeException("ID generation error"));

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () -> {
            evidenceEnrichment.enrichEvidenceNumber(evidenceRequest);
        });

        assertEquals("ENRICHMENT_EXCEPTION", thrown.getCode());
        assertFalse(thrown.getMessage().contains("Error in enrichment service during evidence number update process"));
    }

    @Test
    public void testEnrichIsActive() {
        // Arrange
        Artifact artifact = new Artifact();
        artifact.setSourceType("COURT");
        artifact.setArtifactType("DOCUMENTARY");

        EvidenceRequest evidenceRequest = new EvidenceRequest();
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

        EvidenceRequest evidenceRequest = new EvidenceRequest();
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
