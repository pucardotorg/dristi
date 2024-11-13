package org.pucar.dristi.validators;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.repository.AdvocateClerkRepository;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.web.models.AdvocateClerk;
import org.pucar.dristi.web.models.AdvocateClerkRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class AdvocateClerkRegistrationValidatorTest {

    @InjectMocks
    private AdvocateClerkRegistrationValidator validator;

    @Mock
    private IndividualService individualService;

    @Mock
    private AdvocateClerkRepository repository;

    private AdvocateClerkRequest advocateClerkRequest;
    private RequestInfo requestInfo;

    @BeforeEach
    void setUp() {
        advocateClerkRequest = new AdvocateClerkRequest();
        requestInfo = new RequestInfo();
        advocateClerkRequest.setRequestInfo(requestInfo);
    }

    @Test
    void validateAdvocateRegistration_ValidAdvocate_NoExceptionThrown() {
        AdvocateClerk advocate = new AdvocateClerk();
        advocate.setIndividualId("validIndividualId");
        advocate.setTenantId("validTenantId");
        advocateClerkRequest.setClerk(advocate);

        when(individualService.searchIndividual(requestInfo, "validIndividualId", new HashMap<>())).thenReturn(true);

        assertDoesNotThrow(() -> validator.validateAdvocateClerkRegistration(advocateClerkRequest));
    }

    @Test
    void validateAdvocateRegistration_InvalidIndividualId_ThrowsIllegalArgumentException() {
        AdvocateClerk advocate = new AdvocateClerk();
        advocate.setIndividualId("invalidIndividualId");
        advocate.setTenantId("validTenantId");
        advocateClerkRequest.setClerk(advocate);

        when(individualService.searchIndividual(requestInfo, "invalidIndividualId", new HashMap<>())).thenReturn(false);

        assertThrows(CustomException.class, () -> validator.validateAdvocateClerkRegistration(advocateClerkRequest));
    }

    @Test
    void validateAdvocateRegistration_InvalidTenantId_ThrowsIllegalArgumentException() {
        AdvocateClerk advocate = new AdvocateClerk();
        advocate.setIndividualId("invalidIndividualId");
        advocate.setTenantId(null);
        advocateClerkRequest.setClerk(advocate);

        assertThrows(CustomException.class, () -> validator.validateAdvocateClerkRegistration(advocateClerkRequest));
    }

    @Test
    void validateApplicationExistence_ApplicationExists() {
        // Arrange
        AdvocateClerk advocateClerk = new AdvocateClerk();
        advocateClerk.setTenantId("pg");
        advocateClerk.setApplicationNumber("testAppNumber");
        List<AdvocateClerk> existingApplications = new ArrayList<>();
        existingApplications.add(advocateClerk);
        when(repository.getClerks(anyList(), anyString(), anyInt(), anyInt())).thenReturn(existingApplications);

        // Act
        AdvocateClerk result = validator.validateApplicationExistence(advocateClerk);

        // Assert
        assertNotNull(result);
        assertEquals(advocateClerk, result);
    }

    @Test
    void validateApplicationExistence_ApplicationDoesNotExist() {
        // Arrange
        AdvocateClerk advocateClerk = new AdvocateClerk();
        advocateClerk.setTenantId("pg");
        advocateClerk.setApplicationNumber("nonExistingAppNumber");
        when(repository.getClerks(anyList(), anyString(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        // Act + Assert
        assertThrows(CustomException.class, () -> validator.validateApplicationExistence(advocateClerk));
    }

}
