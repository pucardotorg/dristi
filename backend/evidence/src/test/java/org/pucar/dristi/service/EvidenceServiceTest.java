package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.workflow.ProcessInstance;
import org.egov.common.contract.workflow.State;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.EvidenceEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.EvidenceRepository;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.validators.EvidenceValidator;
import org.pucar.dristi.web.models.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.COMMENT_ADD_ERR;

@ExtendWith(MockitoExtension.class)
class EvidenceServiceTest {

    @Mock
    private EvidenceValidator validator;

    @Mock
    private EvidenceEnrichment evidenceEnrichment;

    @Mock
    private WorkflowService workflowService;

    @Mock
    private EvidenceRepository repository;

    @Mock
    private Producer producer;

    @Mock
    private Configuration config;

    @Mock
    private MdmsUtil mdmsUtil;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EvidenceService evidenceService;

    private EvidenceRequest evidenceRequest;
    private Artifact artifact;
    Map<String, Map<String, JSONArray>> mockMdmsData = new HashMap<>();

    @BeforeEach
    void setUp() {
        artifact = new Artifact();
        artifact.setArtifactType("DEPOSITION");
        artifact.setIsEvidence(true);
        artifact.setFilingType("CASE_FILING");
        evidenceRequest = new EvidenceRequest();
        evidenceRequest.setArtifact(artifact);

        lenient().when(config.getFilingTypeModule()).thenReturn("FilingTypeModule");
        lenient().when(config.getFilingTypeMaster()).thenReturn("FilingTypeMaster");
        JSONArray filingTypeArray = new JSONArray();
        JSONObject filingType1 = new JSONObject();
        filingType1.put("displayName", "caseFiling");
        filingType1.put("code", "CASE_FILING");

        filingTypeArray.add(filingType1);

        Map<String, JSONArray> innerMap = new HashMap<>();
        innerMap.put("FilingTypeMaster", filingTypeArray);
        mockMdmsData.put("FilingTypeModule", innerMap);
    }

    @Test
    void testCreateEvidence_Deposition() {
        when(mdmsUtil.fetchMdmsData(any(), any(), any(), any())).thenReturn(mockMdmsData);
        when(objectMapper.convertValue(any(), eq(JSONObject.class))).thenReturn((JSONObject) mockMdmsData.get("FilingTypeModule").get("FilingTypeMaster").get(0));
        Artifact result = evidenceService.createEvidence(evidenceRequest);

        verify(validator).validateEvidenceRegistration(evidenceRequest);
        verify(evidenceEnrichment).enrichEvidenceRegistration(evidenceRequest);

        assertEquals(artifact, result);
    }

    @Test
    void testCreateEvidence_Other() {
        artifact.setArtifactType("OTHER");
        when(mdmsUtil.fetchMdmsData(any(), any(), any(), any())).thenReturn(mockMdmsData);
        when(objectMapper.convertValue(any(), eq(JSONObject.class))).thenReturn((JSONObject) mockMdmsData.get("FilingTypeModule").get("FilingTypeMaster").get(0));
        when(config.getEvidenceCreateWithoutWorkflowTopic()).thenReturn("evidence-create-without-workflow-topic");;
        Artifact result = evidenceService.createEvidence(evidenceRequest);

        verify(validator).validateEvidenceRegistration(evidenceRequest);
        verify(evidenceEnrichment).enrichEvidenceRegistration(evidenceRequest);
        verify(producer).push(config.getEvidenceCreateWithoutWorkflowTopic(), evidenceRequest);

        assertEquals(artifact, result);
    }

    @Test
    void testSearchEvidence_NoArtifacts() {
        RequestInfo requestInfo = new RequestInfo();
        EvidenceSearchCriteria criteria = new EvidenceSearchCriteria();
        when(repository.getArtifacts(criteria,null)).thenReturn(Collections.emptyList());

        List<Artifact> result = evidenceService.searchEvidence(requestInfo, criteria,null);

        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchEvidence_WithArtifacts() {
        RequestInfo requestInfo = new RequestInfo();
        EvidenceSearchCriteria criteria = new EvidenceSearchCriteria();
        when(repository.getArtifacts(criteria,null)).thenReturn(List.of(artifact));

        // Mocking the ProcessInstance and Workflow
        List<Artifact> result = evidenceService.searchEvidence(requestInfo, criteria,null);

        assertFalse(result.isEmpty());
    }


    @Test
    void testUpdateEvidence() {
        when(mdmsUtil.fetchMdmsData(any(), any(), any(), any())).thenReturn(mockMdmsData);
        when(objectMapper.convertValue(any(), eq(JSONObject.class))).thenReturn((JSONObject) mockMdmsData.get("FilingTypeModule").get("FilingTypeMaster").get(0));
        when(validator.validateEvidenceExistence(evidenceRequest)).thenReturn(artifact);
        when(config.getUpdateEvidenceKafkaTopic()).thenReturn("update-evidence-topic");

        Artifact result = evidenceService.updateEvidence(evidenceRequest);

        verify(evidenceEnrichment).enrichEvidenceRegistrationUponUpdate(evidenceRequest);
        verify(producer).push(config.getUpdateEvidenceKafkaTopic(), evidenceRequest);

        assertEquals(artifact, result);
    }

    @Test
    void testValidateExistingArtifact() {
        when(validator.validateEvidenceExistence(evidenceRequest)).thenReturn(artifact);

        Artifact result = evidenceService.validateExistingEvidence(evidenceRequest);

        assertEquals(artifact, result);
    }

    @Test
    void testEnrichBasedOnStatus_Published() {
        artifact.setStatus("PUBLISHED");

        evidenceService.enrichBasedOnStatus(evidenceRequest);

        verify(evidenceEnrichment).enrichEvidenceNumber(evidenceRequest);
    }

    @Test
    void testEnrichBasedOnStatus_Abated() {
        artifact.setStatus("ABATED");

        evidenceService.enrichBasedOnStatus(evidenceRequest);

        verify(evidenceEnrichment).enrichIsActive(evidenceRequest);
    }

    @Test
    void addComments_Success() {
        EvidenceAddCommentRequest request = new EvidenceAddCommentRequest();
        EvidenceAddComment EvidenceAddComment = new EvidenceAddComment();
        EvidenceAddComment.setArtifactNumber("app123");
        EvidenceAddComment.setTenantId("tenant1");
        Comment comment = new Comment();
        EvidenceAddComment.setComment(Collections.singletonList(comment));
        User userInfo = User.builder().uuid("user-uuid").tenantId("tenant-id").build();
        RequestInfo requestInfoLocal = RequestInfo.builder().userInfo(userInfo).build();
        request.setRequestInfo(requestInfoLocal);
        request.setEvidenceAddComment(EvidenceAddComment);

        Artifact Artifact = new Artifact();
        Artifact.setArtifactNumber("app123");
        Artifact.setTenantId("tenant1");
        Artifact.setComments(new ArrayList<>());
        AuditDetails auditDetails = AuditDetails.builder().build();
        Artifact.setAuditdetails(auditDetails);

        when(repository.getArtifacts(any(),any())).thenReturn(Collections.singletonList(Artifact));
        when(config.getEvidenceUpdateCommentsTopic()).thenReturn("update-comments");
        doNothing().when(producer).push(anyString(), any());

        evidenceService.addComments(request);

        verify(repository).getArtifacts(any(),any());
        verify(producer).push(anyString(), any());
    }

    @Test
    void addComments_ArtifactNotFound() {
        EvidenceAddCommentRequest request = new EvidenceAddCommentRequest();
        EvidenceAddComment EvidenceAddComment = new EvidenceAddComment();
        EvidenceAddComment.setArtifactNumber("app123");
        EvidenceAddComment.setTenantId("tenant1");
        request.setEvidenceAddComment(EvidenceAddComment);
        request.setRequestInfo(new RequestInfo());

        when(repository.getArtifacts(any(),any())).thenReturn(Collections.emptyList());

        CustomException exception = assertThrows(CustomException.class, () -> {
            evidenceService.addComments(request);
        });

        assertEquals("Evidence not found", exception.getMessage());
        verify(repository).getArtifacts(any(),any());
        verify(producer, never()).push(anyString(), any());
    }

    @Test
    void addComments_EnrichmentFailure() {
        EvidenceAddCommentRequest request = new EvidenceAddCommentRequest();
        EvidenceAddComment EvidenceAddComment = new EvidenceAddComment();
        EvidenceAddComment.setArtifactNumber("app123");
        EvidenceAddComment.setTenantId("tenant1");
        Comment comment = new Comment();
        EvidenceAddComment.setComment(Collections.singletonList(comment));
        request.setEvidenceAddComment(EvidenceAddComment);
        User userInfo = User.builder().uuid("user-uuid").tenantId("tenant-id").build();
        RequestInfo requestInfoLocal = RequestInfo.builder().userInfo(userInfo).build();
        request.setRequestInfo(requestInfoLocal);
        request.setEvidenceAddComment(EvidenceAddComment);

        Artifact Artifact = new Artifact();
        Artifact.setArtifactNumber("app123");
        Artifact.setTenantId("tenant1");
        Artifact.setComments(new ArrayList<>());

        when(repository.getArtifacts(any(),any())).thenReturn(Collections.singletonList(Artifact));
        doThrow(new RuntimeException("Enrichment failed")).when(evidenceEnrichment).enrichCommentUponCreate(any(), any());

        CustomException exception = assertThrows(CustomException.class, () -> {
            evidenceService.addComments(request);
        });

        assertEquals(COMMENT_ADD_ERR, exception.getCode());
        assertEquals("Enrichment failed", exception.getMessage());
        verify(repository).getArtifacts(any(),any());
        verify(producer, never()).push(anyString(), any());
    }
}
