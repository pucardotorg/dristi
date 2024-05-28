package org.pucar.dristi.web.controllers;
import jakarta.servlet.http.HttpServletRequest;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.service.EvidenceService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.EvidenceRequest;
import org.pucar.dristi.web.models.EvidenceResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = {"spring.main.allow-bean-definition-overriding=true"})
class ArtifactsApiControllerTest {

    @InjectMocks
    private ArtifactsApiController artifactsApiController;

    @Mock
    private EvidenceService evidenceService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void artifactsV1CreatePost_Success() {
        // Mock request
        HttpServletRequest request = mock(HttpServletRequest.class);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).thenReturn("/artifacts/v1/_create");

        // Mock service response
        Artifact artifact = new Artifact();
        when(evidenceService.createEvidence(any())).thenReturn(artifact);

        // Mock response info
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), anyBoolean())).thenReturn(new ResponseInfo());

        // Create request body
        EvidenceRequest requestBody = new EvidenceRequest();

        // Execute the method
        ResponseEntity<EvidenceResponse> responseEntity = artifactsApiController.artifactsV1CreatePost(requestBody);

        // Assertions
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(artifact, responseEntity.getBody().getArtifact());
    }

    @Test
    void artifactsV1UpdatePost_Success() {
        // Mock request
        HttpServletRequest request = mock(HttpServletRequest.class);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).thenReturn("/artifacts/v1/_update");

        // Mock service response
        Artifact artifact = new Artifact();
        when(evidenceService.updateEvidence(any())).thenReturn(artifact);

        // Mock response info
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), anyBoolean())).thenReturn(new ResponseInfo());

        // Create request body
        EvidenceRequest requestBody = new EvidenceRequest();

        // Execute the method
        ResponseEntity<EvidenceResponse> responseEntity = artifactsApiController.artifactsV1UpdatePost(requestBody);

        // Assertions
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(artifact, responseEntity.getBody().getArtifact());
    }
}

