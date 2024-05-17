package org.pucar.dristi.validators;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.repository.WitnessRepository;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.web.models.Witness;
import org.pucar.dristi.web.models.WitnessRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

 class WitnessRegistrationValidatorTest {

    @Mock
    private IndividualService individualService;

    @Mock
    private CaseRepository caseRepository;

    @Mock
    private WitnessRepository witnessRepository;

    @InjectMocks
    private WitnessRegistrationValidator validator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testValidateCaseRegistration() {
        WitnessRequest request = new WitnessRequest();
        request.setRequestInfo(new RequestInfo());
        Witness witnessWithNullCaseId = new Witness();
        Witness witnessWithValidCaseId = new Witness();
        witnessWithValidCaseId.setCaseId("validCaseId");
        request.setWitnesses(List.of(witnessWithNullCaseId, witnessWithValidCaseId));

        // Ensure validation fails for the witness with null caseId
        Exception exception = assertThrows(Exception.class, () -> validator.validateCaseRegistration(request),
                "Validation should fail when caseId is null");

        // Verify that the correct exception is thrown
        assertTrue(exception.getMessage().contains("caseId is mandatory for creating witness"),
                "Exception message should indicate missing caseId");

        // Ensure individualService.searchIndividual() is not called
        verify(individualService, never()).searchIndividual(any(), anyString());
    }


    @Test
     void testValidateApplicationExistence_NonExistingWitness() {
        // Mock data
        RequestInfo requestInfo = new RequestInfo();

        Witness witness = new Witness();
        witness.setCaseId("nonExistingCaseId");
        witness.setIndividualId("nonExistingIndividualId");

        // Mock repository behavior
        when(witnessRepository.getApplications(any())).thenReturn(Collections.emptyList());

        // Call the method under test and expect CustomException
        assertThrows(CustomException.class, () -> {
            validator.validateApplicationExistence(requestInfo, witness);
        });

        // Verify repository method is called
        verify(witnessRepository, times(1)).getApplications(any());
        verify(individualService, never()).searchIndividual(any(), any()); // Ensure individual service is not called
    }
}

