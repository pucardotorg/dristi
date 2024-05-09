package org.pucar.validators;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.repository.AdvocateClerkRepository;
import org.pucar.service.IndividualService;
import org.pucar.web.models.*;

import org.egov.tracer.model.CustomException;
import org.egov.common.contract.request.RequestInfo;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AdvocateClerkRegistrationValidatorTest {
    @Mock
    private IndividualService individualService;

    @Mock
    private AdvocateClerkRepository repository;

    @InjectMocks
    private AdvocateClerkRegistrationValidator validator;

    @BeforeEach
    void setUp() {
        // Setup necessary for each test can go here
    }

    @Test
    void testValidateAdvocateClerkRegistration_ThrowsExceptionForNullUserInfo() {
        AdvocateClerkRequest request = new AdvocateClerkRequest();
        request.setRequestInfo(new RequestInfo());
        // User info is null, should throw exception
        assertThrows(CustomException.class, () -> validator.validateAdvocateClerkRegistration(request));
    }

    @Test
    void testValidateAdvocateClerkRegistration_ValidRequest() {
        // Setting up a valid request scenario
        AdvocateClerk clerk = new AdvocateClerk();
        clerk.setTenantId("someTenantId");
        clerk.setIndividualId("someIndividualId");
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(new User());
        AdvocateClerkRequest request = new AdvocateClerkRequest(requestInfo, List.of(clerk));

        when(individualService.searchIndividual(any(RequestInfo.class), anyString(), anyMap()))
                .thenReturn(true);

        assertDoesNotThrow(() -> validator.validateAdvocateClerkRegistration(request));
    }

    @Test
    void testValidateApplicationExistence_NonExistentApplication() {
        // Setup
        AdvocateClerk advocateClerk = new AdvocateClerk();
        advocateClerk.setApplicationNumber("12345");
        when(repository.getApplications(any(), any(), anyString(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList()); // Mock to return empty list, simulating no applications found

        assertThrows(CustomException.class, () -> validator.validateApplicationExistence(advocateClerk));
    }
}
