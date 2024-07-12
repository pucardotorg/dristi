package org.pucar.dristi.validators;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.ServiceConstants;
import org.pucar.dristi.repository.EvidenceRepository;
import org.pucar.dristi.util.*;
import org.pucar.dristi.web.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EvidenceValidatorTest {

    @Mock
    private EvidenceRepository repository;

    @Mock
    private CaseUtil caseUtil;

    @Mock
    private ApplicationUtil applicationUtil;

    @Mock
    private OrderUtil orderUtil;

    @Mock
    private HearingUtil hearingUtil;

    @InjectMocks
    private EvidenceValidator evidenceValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateEvidenceRegistration_ShouldThrowException_WhenTenantIdOrCaseIdIsEmpty() {
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        Artifact artifact = new Artifact();
        artifact.setTenantId("");
        artifact.setCaseId("");
        evidenceRequest.setArtifact(artifact);
        evidenceRequest.setRequestInfo(new RequestInfo());

        CustomException exception = assertThrows(CustomException.class, () -> {
            evidenceValidator.validateEvidenceRegistration(evidenceRequest);
        });

        assertEquals(ServiceConstants.ILLEGAL_ARGUMENT_EXCEPTION_CODE, exception.getCode());
        assertEquals("tenantId and caseId are mandatory for creating advocate", exception.getMessage());
    }

    @Test
    void validateEvidenceRegistration_ShouldThrowException_WhenUserInfoIsNull() {
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        Artifact artifact = new Artifact();
        artifact.setTenantId("tenant");
        artifact.setCaseId("caseId");
        evidenceRequest.setArtifact(artifact);
        evidenceRequest.setRequestInfo(new RequestInfo());

        CustomException exception = assertThrows(CustomException.class, () -> {
            evidenceValidator.validateEvidenceRegistration(evidenceRequest);
        });

        assertEquals(ServiceConstants.ENRICHMENT_EXCEPTION, exception.getCode());
        assertEquals("User info not found!!!", exception.getMessage());
    }

    @Test
    void validateEvidenceRegistration_ShouldThrowException_WhenCaseDoesNotExist() {
        EvidenceRequest evidenceRequest = createValidEvidenceRequest();
        when(caseUtil.fetchCaseDetails(any(CaseExistsRequest.class))).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> {
            evidenceValidator.validateEvidenceRegistration(evidenceRequest);
        });

        assertEquals(ServiceConstants.CASE_EXCEPTION, exception.getCode());
        assertEquals("case does not exist", exception.getMessage());
    }

    @Test
    void validateEvidenceRegistration_ShouldThrowException_WhenApplicationDoesNotExist() {
        EvidenceRequest evidenceRequest = createValidEvidenceRequest();
        evidenceRequest.getArtifact().setApplication("applicationId");
        when(caseUtil.fetchCaseDetails(any(CaseExistsRequest.class))).thenReturn(true);
        when(applicationUtil.fetchApplicationDetails(any(ApplicationExistsRequest.class))).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> {
            evidenceValidator.validateEvidenceRegistration(evidenceRequest);
        });

        assertEquals(ServiceConstants.APPLICATION_EXCEPTION, exception.getCode());
        assertEquals("application does not exist", exception.getMessage());
    }

    @Test
    void validateEvidenceRegistration_ShouldThrowException_WhenOrderDoesNotExist() {
        EvidenceRequest evidenceRequest = createValidEvidenceRequest();
        evidenceRequest.getArtifact().setOrder("orderId");
        when(caseUtil.fetchCaseDetails(any(CaseExistsRequest.class))).thenReturn(true);
        when(orderUtil.fetchOrderDetails(any(OrderExistsRequest.class))).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> {
            evidenceValidator.validateEvidenceRegistration(evidenceRequest);
        });

        assertEquals(ServiceConstants.APPLICATION_EXCEPTION, exception.getCode());
        assertEquals("application does not exist", exception.getMessage());
    }

    @Test
    void validateEvidenceRegistration_ShouldThrowException_WhenHearingDoesNotExist() {
        EvidenceRequest evidenceRequest = createValidEvidenceRequest();
        evidenceRequest.getArtifact().setHearing("hearingId");
        when(caseUtil.fetchCaseDetails(any(CaseExistsRequest.class))).thenReturn(true);
        when(hearingUtil.fetchHearingDetails(any(HearingExistsRequest.class))).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> {
            evidenceValidator.validateEvidenceRegistration(evidenceRequest);
        });

        assertEquals(ServiceConstants.APPLICATION_EXCEPTION, exception.getCode());
        assertEquals("application does not exist", exception.getMessage());
    }

    @Test
    void validateEvidenceRegistration_ShouldPass_WhenAllValidationsPass() {
        EvidenceRequest evidenceRequest = createValidEvidenceRequest();
        evidenceRequest.getArtifact().setApplication("applicationId");
        evidenceRequest.getArtifact().setOrder("8c11c5ca-03bd-11e7-93ae-92361f002671");
        evidenceRequest.getArtifact().setHearing("hearingId");

        when(caseUtil.fetchCaseDetails(any(CaseExistsRequest.class))).thenReturn(true);
        when(applicationUtil.fetchApplicationDetails(any(ApplicationExistsRequest.class))).thenReturn(true);
        when(orderUtil.fetchOrderDetails(any(OrderExistsRequest.class))).thenReturn(true);
        when(hearingUtil.fetchHearingDetails(any(HearingExistsRequest.class))).thenReturn(true);

        assertDoesNotThrow(() -> {
            evidenceValidator.validateEvidenceRegistration(evidenceRequest);
        });
    }

    @Test
    void validateEvidenceExistence_ShouldThrowException_WhenNoExistingApplicationsFound() {
        EvidenceRequest evidenceRequest = createValidEvidenceRequest();
        when(repository.getArtifacts(any(EvidenceSearchCriteria.class), isNull())).thenReturn(new ArrayList<>());

        CustomException exception = assertThrows(CustomException.class, () -> {
            evidenceValidator.validateEvidenceExistence(evidenceRequest);
        });

        assertEquals("EVIDENCE_UPDATE_EXCEPTION", exception.getCode());
        assertEquals("Error occurred while updating evidence: org.egov.tracer.model.CustomException: case does not exist", exception.getMessage());
    }

    private EvidenceRequest createValidEvidenceRequest() {
        EvidenceRequest evidenceRequest = new EvidenceRequest();
        Artifact artifact = new Artifact();
        artifact.setTenantId("tenant");
        artifact.setCaseId("caseId");
        artifact.setApplication("applicationId");
        artifact.setOrder("orderId");
        artifact.setHearing("hearingId");
        artifact.setSourceID("sourceId");
        artifact.setSourceName("sourceName");
        evidenceRequest.setArtifact(artifact);

        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        requestInfo.setUserInfo(userInfo);
        evidenceRequest.setRequestInfo(requestInfo);

        return evidenceRequest;
    }
}
