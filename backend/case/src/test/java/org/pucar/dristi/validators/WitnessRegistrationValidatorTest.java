package org.pucar.dristi.validators;

import org.egov.common.contract.request.RequestInfo;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WitnessRegistrationValidatorTest {

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
    public void testValidateCaseRegistration() {
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
    public void testValidateApplicationExistence() {
        Witness witness = new Witness();
        witness.setCaseId("existingCaseId");

        when(witnessRepository.getApplications(any())).thenReturn(List.of(witness));

        Witness result = validator.validateApplicationExistence(witness);

        assert result == witness : "Returned witness should be the same as input";

        verify(witnessRepository, times(1)).getApplications(any());
    }
}

