package org.pucar.dristi.web.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.egov.common.contract.response.ResponseInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.pucar.dristi.service.EvidenceService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.EvidenceRequest;
import org.pucar.dristi.web.models.EvidenceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RunWith(MockitoJUnitRunner.class)
public class ArtifactsApiControllerTest {

    @Mock
    private EvidenceService evidenceService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @InjectMocks
    private ArtifactsApiController artifactsApiController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testArtifactsV1CreatePost_ValidInput() {
        // Prepare valid input
        EvidenceRequest request = new EvidenceRequest(/* construct a valid request */);
        Artifact expectedArtifact = new Artifact(/* construct expected artifact */);

        // Mock service response
        when(evidenceService.createEvidence(request)).thenReturn(expectedArtifact);

        // Mock response info
        ResponseInfo mockResponseInfo = new ResponseInfo(/* construct mock response info */);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), eq(true))).thenReturn(mockResponseInfo);

        // Call the method
        ResponseEntity<EvidenceResponse> responseEntity = artifactsApiController.artifactsV1CreatePost(request);

        // Verify service and factory calls
        verify(evidenceService).createEvidence(request);
        verify(responseInfoFactory).createResponseInfoFromRequestInfo(eq(request.getRequestInfo()), eq(true));

        // Verify response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        EvidenceResponse response = responseEntity.getBody();
        assertNotNull(response.getArtifact());
        assertEquals(expectedArtifact, response.getArtifact());
        assertNotNull(response.getResponseInfo()); // Verify response info is not null
        assertEquals(mockResponseInfo, response.getResponseInfo()); // Verify response info content
    }



    @Test
    public void testArtifactsV1UpdatePost_ValidInput() {
        // Prepare valid input
        EvidenceRequest request = new EvidenceRequest(/* construct a valid request */);
        Artifact expectedArtifact = new Artifact(/* construct expected artifact */);

        // Mock service response
        when(evidenceService.updateEvidence(request)).thenReturn(expectedArtifact);

        // Mock response info
        ResponseInfo mockResponseInfo = new ResponseInfo(/* construct mock response info */);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), eq(true))).thenReturn(mockResponseInfo);

        // Call the method
        ResponseEntity<EvidenceResponse> responseEntity = artifactsApiController.artifactsV1UpdatePost(request);

        // Verify service and factory calls
        verify(evidenceService).updateEvidence(request);
        verify(responseInfoFactory).createResponseInfoFromRequestInfo(eq(request.getRequestInfo()), eq(true));

        // Verify response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        EvidenceResponse response = responseEntity.getBody();
        assertNotNull(response.getArtifact());
        assertEquals(expectedArtifact, response.getArtifact());
        assertNotNull(response.getResponseInfo()); // Verify response info is not null
        assertEquals(mockResponseInfo, response.getResponseInfo()); // Verify response info content
    }


}
