package org.pucar.dristi.validators;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CourtCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CaseRegistrationValidatorTest {

    @Mock
    private IndividualService individualService;
    @Mock
    private CaseRepository caseRepository;

    @InjectMocks
    private CaseRegistrationValidator validator;

    @BeforeEach
    void setUp() {
        // Setup done before each test
    }

    @Test
    void testValidateCaseRegistration_WithValidData() {
        CaseRequest request = new CaseRequest();
        request.setRequestInfo(new RequestInfo());
        CourtCase courtCase = new CourtCase();
        courtCase.setTenantId("validTenantId");
        request.setCases(new ArrayList<>(Collections.singletonList(courtCase)));

        assertDoesNotThrow(() -> validator.validateCaseRegistration(request));
    }

    @Test
    void testValidateCaseRegistration_WithMissingTenantId() {
        CaseRequest request = new CaseRequest();
        request.setRequestInfo(new RequestInfo());
        CourtCase courtCase = new CourtCase();
        request.setCases(new ArrayList<>(Collections.singletonList(courtCase)));

        Exception exception = assertThrows(CustomException.class, () -> validator.validateCaseRegistration(request));
    }

    @Test
    void testValidateApplicationExistence_ExistingApplication() {
        CourtCase courtCase = new CourtCase();
        courtCase.setFilingNumber("Filing123");
        List<CourtCase> foundCases = new ArrayList<>();
        foundCases.add(new CourtCase());

        when(caseRepository.getApplications(any())).thenReturn(foundCases);

        CourtCase result = validator.validateApplicationExistence(courtCase);
        assertNotNull(result);
    }

    @Test
    void testValidateApplicationExistence_NoExistingApplication() {
        CourtCase courtCase = new CourtCase();
        courtCase.setFilingNumber("Filing123");

        when(caseRepository.getApplications(any())).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(CustomException.class, () -> validator.validateApplicationExistence(courtCase));
    }
}

