package org.pucar.dristi.validators;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.repository.AdvocateClerkRepository;
import org.pucar.dristi.web.models.AdvocateClerk;
import org.pucar.dristi.web.models.AdvocateClerkRequest;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.service.IndividualService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;

@ExtendWith(MockitoExtension.class)
public class AdvocateClerkRegistrationValidatorTest {

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
        advocateClerkRequest.setClerks(Collections.singletonList(advocate));

        when(individualService.searchIndividual(requestInfo, "validIndividualId", new HashMap<>())).thenReturn(true);

        assertDoesNotThrow(() -> validator.validateAdvocateClerkRegistration(advocateClerkRequest));
    }

    @Test
    void validateAdvocateRegistration_InvalidIndividualId_ThrowsIllegalArgumentException() {
        AdvocateClerk advocate = new AdvocateClerk();
        advocate.setIndividualId("invalidIndividualId");
        advocate.setTenantId("validTenantId");
        advocateClerkRequest.setClerks(Collections.singletonList(advocate));

        when(individualService.searchIndividual(requestInfo, "invalidIndividualId", new HashMap<>())).thenReturn(false);

        assertThrows(CustomException.class, () -> validator.validateAdvocateClerkRegistration(advocateClerkRequest));
    }

}
