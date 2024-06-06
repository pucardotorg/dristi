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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
        List<String> idList = new ArrayList<>();
        idList.add("artifactNumber");
        Mockito.when(idgenUtil.getIdList(any(), anyString(), anyString(), any(), anyInt())).thenReturn(idList);

        evidenceEnrichment.enrichEvidenceRegistration(evidenceRequest);

        verify(idgenUtil, times(1)).getIdList(any(), anyString(), anyString(), any(), anyInt());
        Artifact artifact = evidenceRequest.getArtifact();
        assert artifact.getId() != null;
        assert artifact.getAuditdetails() != null;
        assert artifact.getArtifactNumber().equals("artifactNumber");
    }

    @Test
    void testEnrichEvidenceRegistration_UserInfoNotFound() {
        evidenceRequest.getRequestInfo().setUserInfo(null);
        CustomException thrown = assertThrows(CustomException.class, () -> evidenceEnrichment.enrichEvidenceRegistration(evidenceRequest));
        assert thrown.getCode().equals("ENRICHMENT_EXCEPTION");
        assert thrown.getMessage().contains("User info not found!!!");
    }
    @Test
    void testEnrichEvidenceRegistration_WithFile() {
        List<String> idList = new ArrayList<>();
        idList.add("artifactNumber");
        Mockito.when(idgenUtil.getIdList(any(), anyString(), anyString(), any(), anyInt())).thenReturn(idList);

        // Set up the file in the artifact
        Document document = new Document();
        Artifact artifact = evidenceRequest.getArtifact();
        artifact.setFile(document);

        evidenceEnrichment.enrichEvidenceRegistration(evidenceRequest);

        verify(idgenUtil, times(1)).getIdList(any(), anyString(), anyString(), any(), anyInt());
        assert artifact.getId() != null;
        assert artifact.getAuditdetails() != null;
        assert artifact.getArtifactNumber().equals("artifactNumber");
        assert artifact.getFile().getId() != null;
        assert artifact.getFile().getDocumentUid().equals(artifact.getFile().getId());
    }

    @Test
    void testEnrichEvidenceRegistration_Exception() {
        List<String> idList = new ArrayList<>();
        idList.add("artifactNumber");
        Mockito.when(idgenUtil.getIdList(any(), anyString(), anyString(), any(), anyInt())).thenThrow(new RuntimeException("ID generation failed"));

        Exception exception = assertThrows(CustomException.class, () -> {
            evidenceEnrichment.enrichEvidenceRegistration(evidenceRequest);
        });

        assertTrue(exception.getMessage().contains("Error evidence in enrichment service: ID generation failed"));
        verify(idgenUtil, times(1)).getIdList(any(), anyString(), anyString(), any(), anyInt());
    }

    @Test
    void testEnrichEvidenceRegistrationUponUpdate() {
        AuditDetails auditDetails = AuditDetails.builder().lastModifiedTime(0L).lastModifiedBy("oldUuid").build();
        evidenceRequest.getArtifact().setAuditdetails(auditDetails);

        evidenceEnrichment.enrichEvidenceRegistrationUponUpdate(evidenceRequest);

        assert evidenceRequest.getArtifact().getAuditdetails().getLastModifiedTime() > 0;
        assert evidenceRequest.getArtifact().getAuditdetails().getLastModifiedBy().equals(evidenceRequest.getRequestInfo().getUserInfo().getUuid());
    }
    @Test
    void testEnrichEvidenceRegistrationUponUpdate_Exception() {
        // Create a scenario where getAuditdetails() will throw a NullPointerException
        evidenceRequest.getArtifact().setAuditdetails(null);

        Exception exception = assertThrows(CustomException.class, () -> {
            evidenceEnrichment.enrichEvidenceRegistrationUponUpdate(evidenceRequest);
        });

        assertTrue(exception.getMessage().contains("Error in order enrichment service during order update process"));
    }

}
