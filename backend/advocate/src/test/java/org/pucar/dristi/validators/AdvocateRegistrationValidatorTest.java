package org.pucar.dristi.validators;

import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.repository.AdvocateRepository;
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateRequest;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.service.IndividualService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;

@ExtendWith(MockitoExtension.class)
public class AdvocateRegistrationValidatorTest {

    @InjectMocks
    private AdvocateRegistrationValidator validator;

    @Mock
    private IndividualService individualService;

    @Mock
    private AdvocateRepository repository;

    private AdvocateRequest advocateRequest;
    private RequestInfo requestInfo;

    @BeforeEach
    void setUp() {
        advocateRequest = new AdvocateRequest();
        requestInfo = new RequestInfo();
        requestInfo.setUserInfo(new User());
        advocateRequest.setRequestInfo(requestInfo);
    }

    @Test
    void validateAdvocateRegistration_ValidAdvocate_NoExceptionThrown() {
        Advocate advocate = new Advocate();
        advocate.setIndividualId("validIndividualId");
        advocate.setTenantId("validTenantId");
        advocateRequest.setAdvocates(Collections.singletonList(advocate));

        when(individualService.searchIndividual(requestInfo, "validIndividualId", new HashMap<>())).thenReturn(true);

        assertDoesNotThrow(() -> validator.validateAdvocateRegistration(advocateRequest));
    }

    @Test
    void validateAdvocateRegistration_InvalidIndividualId_ThrowsIllegalArgumentException() {
        Advocate advocate = new Advocate();
        advocate.setIndividualId("invalidIndividualId");
        advocate.setTenantId("validTenantId");
        advocateRequest.setAdvocates(Collections.singletonList(advocate));

        when(individualService.searchIndividual(requestInfo, "invalidIndividualId", new HashMap<>())).thenReturn(false);

        assertThrows(CustomException.class, () -> validator.validateAdvocateRegistration(advocateRequest));
    }

    @Test
    void validateAdvocateRegistration_MissingTenantId_ThrowsCustomException() {
        Advocate advocate = new Advocate();
        advocate.setIndividualId("validIndividualId");
        // Missing tenantId intentionally
        advocateRequest.setAdvocates(Collections.singletonList(advocate));


        assertThrows(CustomException.class, () -> validator.validateAdvocateRegistration(advocateRequest));
    }
}
