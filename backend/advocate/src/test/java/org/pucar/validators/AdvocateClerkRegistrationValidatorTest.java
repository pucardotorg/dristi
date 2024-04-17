//package org.pucar.validators;
//
//import org.egov.tracer.model.CustomException;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.pucar.repository.AdvocateClerkRepository;
//import org.pucar.web.models.AdvocateClerk;
//import org.pucar.web.models.AdvocateClerkRequest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.util.ObjectUtils;
//import java.util.Collections;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//public class AdvocateClerkRegistrationValidatorTest {
//
//    @Mock
//    private AdvocateClerkRepository repository;
//
//    @InjectMocks
//    private AdvocateClerkRegistrationValidator validator;
//
//    @Test
//    void validateAdvocateClerkRegistration_ValidTenantId_NoExceptionThrown() {
//        // Prepare valid AdvocateClerkRequest
//        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
//        AdvocateClerk advocateClerk = new AdvocateClerk();
//        advocateClerk.setTenantId("tenantId");
//        advocateClerkRequest.setClerks(Collections.singletonList(advocateClerk));
//
//        // No exception should be thrown
//        validator.validateAdvocateClerkRegistration(advocateClerkRequest);
//    }
//
//    @Test
//    void validateAdvocateClerkRegistration_NullTenantId_ExceptionThrown() {
//        // Prepare AdvocateClerkRequest with null tenantId
//        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
//        AdvocateClerk advocateClerk = new AdvocateClerk();
//        advocateClerk.setTenantId(null);
//        advocateClerkRequest.setClerks(Collections.singletonList(advocateClerk));
//
//        // Exception should be thrown
//        assertThrows(CustomException.class, () -> validator.validateAdvocateClerkRegistration(advocateClerkRequest));
//    }
//
//    @Test
//    void validateAdvocateClerkRegistration_EmptyTenantId_ExceptionThrown() {
//        // Prepare AdvocateClerkRequest with empty tenantId
//        AdvocateClerkRequest advocateClerkRequest = new AdvocateClerkRequest();
//        AdvocateClerk advocateClerk = new AdvocateClerk();
//        advocateClerk.setTenantId("");
//        advocateClerkRequest.setClerks(Collections.singletonList(advocateClerk));
//
//        // Exception should be thrown
//        assertThrows(CustomException.class, () -> validator.validateAdvocateClerkRegistration(advocateClerkRequest));
//    }
//}
