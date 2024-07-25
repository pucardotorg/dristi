package org.pucar.dristi.validators;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

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
        request.setWitness(witnessWithNullCaseId);

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
    public void testValidateCaseRegistration_MissingIndividualId() {
        WitnessRequest request = new WitnessRequest();
        request.setRequestInfo(new RequestInfo());
        Witness witnessWithValidCaseId = new Witness();
        witnessWithValidCaseId.setCaseId("validCaseId");
        request.setWitness(witnessWithValidCaseId);

        // Ensure validation fails for the witness with null caseId
         assertThrows(Exception.class, () -> validator.validateCaseRegistration(request),
                "Validation should fail when caseId is null");
    }

    @Test
    public void testValidateCaseRegistration_INDIVIDUAL_NOT_FOUND() {
        WitnessRequest request = new WitnessRequest();
        request.setRequestInfo(new RequestInfo());
        Witness witnessWithValidCaseId = new Witness();
        witnessWithValidCaseId.setCaseId("validCaseId");
        witnessWithValidCaseId.setIndividualId("individualID");
        request.setWitness(witnessWithValidCaseId);

        when(individualService.searchIndividual(any(), any())).thenReturn(false);

        // Ensure validation fails for the witness with null caseId
       assertThrows(Exception.class, () -> validator.validateCaseRegistration(request),
                "Validation should fail when caseId is null");
    }

    @Test
    public void testValidateApplicationExistence_ExistingWitness() {
        // Mock data
        RequestInfo requestInfo = new RequestInfo();

        Witness witness = new Witness();
        witness.setCaseId("nonExistingCaseId");
        witness.setIndividualId("nonExistingIndividualId");

        Witness witnessBis = new Witness();

        // Mock repository behavior
        when(witnessRepository.getApplications(any())).thenReturn(List.of(witness));

        // Call the method under test and expect CustomException
        assertThrows(CustomException.class, () -> {
            validator.validateApplicationExistence(requestInfo, witnessBis);
        });

        // Verify repository method is called
        verify(witnessRepository, times(1)).getApplications(any());
        verify(individualService, never()).searchIndividual(any(), any()); // Ensure individual service is not called
    }

    @Test
    public void testValidateApplicationExistence_NonExistingWitness() {
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

    @Test
    public void testValidateApplicationExistence() {
        // Mock data
        RequestInfo requestInfo = new RequestInfo();

        Witness witness = new Witness();
        witness.setCaseId("nonExistingCaseId");
        witness.setIndividualId("nonExistingIndividualId");

        // Mock repository behavior
        when(witnessRepository.getApplications(any())).thenReturn(List.of(witness));

        // Call the method under test and expect CustomException
        assertThrows(CustomException.class, () -> {
            validator.validateApplicationExistence(requestInfo, witness);
        });
    }

    @Test
    public void testValidateApplicationExistence_MissingIndividualId() {
        // Mock data
        RequestInfo requestInfo = new RequestInfo();

        Witness witness = new Witness();
        witness.setCaseId("nonExistingCaseId");

        // Mock repository behavior
        when(witnessRepository.getApplications(any())).thenReturn(List.of(witness));

        // Call the method under test and expect CustomException
        assertThrows(Exception.class, () -> {
            validator.validateApplicationExistence(requestInfo, witness);
        });
    }

    @Test
    public void testValidateApplicationExistence_INDIVIDUAL_NOT_FOUND() {

        Witness witnessWithValidCaseId = new Witness();
        witnessWithValidCaseId.setCaseId("validCaseId");
        witnessWithValidCaseId.setIndividualId("individualID");

        when(individualService.searchIndividual(any(), any())).thenReturn(false);

        // Ensure validation fails for the witness with null caseId
       assertThrows(Exception.class, () -> validator.validateApplicationExistence(new RequestInfo(), witnessWithValidCaseId),
                "Validation should fail when caseId is null");
    }
}

