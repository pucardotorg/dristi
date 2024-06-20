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
        assertFalse(artifact.getIsActive()); // Assuming set to false by default
        // Add more assertions as needed for other properties

        // If file is not null, ensure its properties are set correctly
        if (artifact.getFile() != null) {
            assertNotNull(artifact.getFile().getId());
            assertEquals(artifact.getFile().getId(), artifact.getFile().getDocumentUid());
        }
    }

    @Test
    public void testGetIdgenByArtifactType() {
        assertEquals("document.evidence_complainant", evidenceEnrichment.getIdgenByArtifactType("complainant"));
        assertEquals("document.evidence_accused", evidenceEnrichment.getIdgenByArtifactType("accused"));
        assertEquals("document.evidence_court", evidenceEnrichment.getIdgenByArtifactType("court"));
        assertEquals("document.witness_complainant", evidenceEnrichment.getIdgenByArtifactType("witness_complainant"));
        assertEquals("document.witness_accused", evidenceEnrichment.getIdgenByArtifactType("witness_accused"));
        assertEquals("document.witness_court", evidenceEnrichment.getIdgenByArtifactType("witness_court"));
        assertThrows(CustomException.class, () -> evidenceEnrichment.getIdgenByArtifactType("invalidType"));
    }

    @Test
    void testEnrichEvidenceRegistrationUponUpdate() {
        AuditDetails auditDetails = AuditDetails.builder().lastModifiedTime(0L).lastModifiedBy("oldUuid").build();
        evidenceRequest.getArtifact().setAuditdetails(auditDetails);

        evidenceEnrichment.enrichEvidenceRegistrationUponUpdate(evidenceRequest);

        assert evidenceRequest.getArtifact().getAuditdetails().getLastModifiedTime() > 0;
        assert evidenceRequest.getArtifact().getAuditdetails().getLastModifiedBy().equals(evidenceRequest.getRequestInfo().getUserInfo().getUuid());
    }

}
