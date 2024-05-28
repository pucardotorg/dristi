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
import org.pucar.dristi.web.models.RequestInfoWrapper;

import java.util.ArrayList;
import java.util.List;

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
    void validateApplicationExistence_ExistingApplication() {
        // Mock repository to return existing application
        List<Artifact> existingApplications = new ArrayList<>();
        existingApplications.add(new Artifact());
        when(repository.getArtifacts(any(), any(), any(), any(), any(), any())).thenReturn(existingApplications);

        // Execute the method
        Artifact result = evidenceValidator.validateApplicationExistence(evidenceRequest);

        // Assertions
        assertNotNull(result);
    }

    @Test
    void validateApplicationExistence_NonExistingApplication() {
        // Mock repository to return empty list (no existing application)
        when(repository.getArtifacts(any(), any(), any(), any(), any(), any())).thenReturn(new ArrayList<>());

        // Execute the method and assert CustomException
        CustomException exception = assertThrows(CustomException.class, () -> evidenceValidator.validateApplicationExistence(evidenceRequest));
        assertEquals("Evidence does not exist", exception.getMessage());
    }
}

