package org.pucar.validators;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.repository.AdvocateRegistrationRepository;
import org.pucar.web.models.Advocate;
import org.pucar.web.models.AdvocateRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AdvocateRegistrationValidatorTest {

    @Mock
    private AdvocateRegistrationRepository repository;

    @InjectMocks
    private AdvocateRegistrationValidator validator;

    @Test
    void validateAdvocateRegistrationThrowsExceptionWhenTenantIdIsEmpty() {
        // Prepare data
        AdvocateRequest request = new AdvocateRequest();
        Advocate advocate = new Advocate();
        advocate.setTenantId(""); // Empty tenant ID
        request.setAdvocates(Collections.singletonList(advocate));

        // Assert that validation throws a CustomException
        assertThrows(CustomException.class, () -> validator.validateAdvocateRegistration(request), "Expected validateAdvocateRegistration to throw, but it didn't");
    }

    @Test
    void validateAdvocateRegistrationThrowsExceptionWhenTenantIdIsNull() {
        // Prepare data
        AdvocateRequest request = new AdvocateRequest();
        Advocate advocate = new Advocate();
        advocate.setTenantId(null); // Null tenant ID
        request.setAdvocates(Collections.singletonList(advocate));

        // Assert that validation throws a CustomException
        assertThrows(CustomException.class, () -> validator.validateAdvocateRegistration(request), "Expected validateAdvocateRegistration to throw, but it didn't");
    }

    @Test
    void validateAdvocateRegistrationPassesWhenTenantIdIsNotEmpty() {
        // Prepare data
        AdvocateRequest request = new AdvocateRequest();
        Advocate advocate = new Advocate();
        advocate.setTenantId("valid-tenant-id");
        request.setAdvocates(Collections.singletonList(advocate));

        // Attempt to validate; no exception should be thrown
        validator.validateAdvocateRegistration(request);
        // No assertion for exception here, as a pass is indicated by the absence of an exception.
    }
}
