package org.pucar.dristi.validators;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.repository.EvidenceRepository;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.EvidenceRequest;
import org.pucar.dristi.web.models.EvidenceSearchCriteria;
import org.pucar.dristi.web.models.RequestInfoWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EvidenceValidatorTest {

    @InjectMocks
    private EvidenceValidator evidenceValidator;

    @Mock
    private EvidenceRepository repository;

    @Mock
    private MdmsUtil mdmsUtil;

    private EvidenceRequest evidenceRequest;

    @BeforeEach
    void setUp() {
        // Initialize the evidenceRequest object before each test
        evidenceRequest = new EvidenceRequest();
        evidenceRequest.setRequestInfo(new RequestInfo());
        Artifact artifact = new Artifact();
        evidenceRequest.setArtifact(artifact);
    }


    @Test
    void validateEvidenceRegistration_MissingTenantId() {
        // Set up invalid request with missing tenantId
        evidenceRequest.getArtifact().setCaseId("case1");

        // Execute the method and assert CustomException
        CustomException exception = assertThrows(CustomException.class, () -> evidenceValidator.validateEvidenceRegistration(evidenceRequest));
        assertEquals("tenantId and caseId are mandatory for creating advocate", exception.getMessage());
    }

    @Test
    void validateEvidenceRegistration_MissingCaseId() {
        // Set up invalid request with missing caseId
        evidenceRequest.getArtifact().setTenantId("tenant1");

        // Execute the method and assert CustomException
        CustomException exception = assertThrows(CustomException.class, () -> evidenceValidator.validateEvidenceRegistration(evidenceRequest));
        assertEquals("tenantId and caseId are mandatory for creating advocate", exception.getMessage());
    }

    @Test
    void testValidateApplicationExistence_Success() {
        // Create test data
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        RequestInfo requestInfo = new RequestInfo();
        evidenceRequest.setRequestInfo(requestInfo);

        Artifact artifact = new Artifact();
        artifact.setCaseId("testCaseId");
        artifact.setApplication("testApplication");
        artifact.setHearing("testHearing");
        artifact.setOrder("testOrder");
        artifact.setSourceID("testSourceId");
        artifact.setSourceName("testSourceName");
        evidenceRequest.setArtifact(artifact);

        List<Artifact> existingApplications = Collections.singletonList(new Artifact());

        // Mock repository response
        when(repository.getArtifacts(any(EvidenceSearchCriteria.class))).thenReturn(existingApplications);

        // Execute the method under test
        Artifact result = evidenceValidator.validateApplicationExistence(evidenceRequest);

        // Verify and assert
        assertNotNull(result);
        verify(repository, times(1)).getArtifacts(any(EvidenceSearchCriteria.class));
    }

    @Test
    void testValidateApplicationExistence_NoExistingApplication() {
        // Create test data
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        RequestInfo requestInfo = new RequestInfo();
        evidenceRequest.setRequestInfo(requestInfo);

        Artifact artifact = new Artifact();
        artifact.setCaseId("testCaseId");
        artifact.setApplication("testApplication");
        artifact.setHearing("testHearing");
        artifact.setOrder("testOrder");
        artifact.setSourceID("testSourceId");
        artifact.setSourceName("testSourceName");
        evidenceRequest.setArtifact(artifact);

        // Mock repository response
        when(repository.getArtifacts(any(EvidenceSearchCriteria.class))).thenReturn(new ArrayList<>());

        // Execute the method under test and assert exception
        CustomException exception = assertThrows(CustomException.class, () ->
                evidenceValidator.validateApplicationExistence(evidenceRequest));

        assertEquals("VALIDATION EXCEPTION", exception.getCode());
        assertEquals("Evidence does not exist", exception.getMessage());
        verify(repository, times(1)).getArtifacts(any(EvidenceSearchCriteria.class));
    }
}

