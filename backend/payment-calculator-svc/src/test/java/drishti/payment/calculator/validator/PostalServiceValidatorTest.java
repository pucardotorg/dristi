package drishti.payment.calculator.validator;

import drishti.payment.calculator.web.models.PostalService;
import drishti.payment.calculator.web.models.PostalServiceRequest;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PostalServiceValidatorTest {
    
    @InjectMocks
    private PostalServiceValidator postalServiceValidator;
    
    @Test
    public void testValidatePostalServiceRequest_Success() {
        PostalService postalService = new PostalService();
        postalService.setTenantId("tenantId");
        postalService.setPostalHubId("postalHubId");
        postalService.setPincode("123456");
        postalService.setDistanceKM(50.0);

        PostalServiceRequest request = new PostalServiceRequest();
        request.setPostalServices(Collections.singletonList(postalService));

        assertDoesNotThrow(() -> postalServiceValidator.validatePostalServiceRequest(request));
    }

    @Test
    public void testValidatePostalServiceRequest_MissingTenantId() {
        PostalService postalService = new PostalService();
        postalService.setPostalHubId("postalHubId");
        postalService.setPincode("123456");
        postalService.setDistanceKM(50.0);

        PostalServiceRequest request = new PostalServiceRequest();
        request.setPostalServices(Collections.singletonList(postalService));

        CustomException exception = assertThrows(CustomException.class, () -> postalServiceValidator.validatePostalServiceRequest(request));
        assertEquals("DK_PC_TENANT_ERR", exception.getCode());
        assertEquals("tenantId is mandatory for creating postal.", exception.getMessage());
    }

    @Test
    public void testValidatePostalServiceRequest_MissingPostalHubId() {
        PostalService postalService = new PostalService();
        postalService.setTenantId("tenantId");
        postalService.setPincode("123456");
        postalService.setDistanceKM(50.0);

        PostalServiceRequest request = new PostalServiceRequest();
        request.setPostalServices(Collections.singletonList(postalService));

        CustomException exception = assertThrows(CustomException.class, () -> postalServiceValidator.validatePostalServiceRequest(request));
        assertEquals("DK_PC_NAME_ERR", exception.getCode());
        assertEquals("hub id is mandatory for creating postal.", exception.getMessage());
    }

    @Test
    public void testValidatePostalServiceRequest_MissingPincode() {
        PostalService postalService = new PostalService();
        postalService.setTenantId("tenantId");
        postalService.setPostalHubId("postalHubId");
        postalService.setDistanceKM(50.0);

        PostalServiceRequest request = new PostalServiceRequest();
        request.setPostalServices(Collections.singletonList(postalService));

        CustomException exception = assertThrows(CustomException.class, () -> postalServiceValidator.validatePostalServiceRequest(request));
        assertEquals("DK_PC_PINCODE_ERR", exception.getCode());
        assertEquals("pincode is mandatory for creating postal.", exception.getMessage());
    }

    @Test
    public void testValidatePostalServiceRequest_MissingDistance() {
        PostalService postalService = new PostalService();
        postalService.setTenantId("tenantId");
        postalService.setPostalHubId("postalHubId");
        postalService.setPincode("123456");

        PostalServiceRequest request = new PostalServiceRequest();
        request.setPostalServices(Collections.singletonList(postalService));

        CustomException exception = assertThrows(CustomException.class, () -> postalServiceValidator.validatePostalServiceRequest(request));
        assertEquals("DK_PC_DIS_ERR", exception.getCode());
        assertEquals("distance is mandatory for creating postal.", exception.getMessage());
    }

    @Test
    public void testValidateExistingPostalServiceRequest_Success() {
        PostalService postalService = new PostalService();
        postalService.setPostalHubId("postalHubId");

        PostalServiceRequest request = new PostalServiceRequest();
        request.setPostalServices(Collections.singletonList(postalService));

        assertDoesNotThrow(() -> postalServiceValidator.validateExistingPostalServiceRequest(request));
    }

    @Test
    public void testValidateExistingPostalServiceRequest_MissingPostalHubId() {
        PostalService postalService = new PostalService();

        PostalServiceRequest request = new PostalServiceRequest();
        request.setPostalServices(Collections.singletonList(postalService));

        CustomException exception = assertThrows(CustomException.class, () -> postalServiceValidator.validateExistingPostalServiceRequest(request));
        assertEquals("DK_PC_HUB_ERR", exception.getCode());
    }
}
