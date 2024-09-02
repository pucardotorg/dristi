package drishti.payment.calculator.validator;

import drishti.payment.calculator.web.models.PostalHub;
import drishti.payment.calculator.web.models.PostalHubRequest;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PostalHubValidatorTest {

    @InjectMocks
    private PostalHubValidator postalHubValidator;

    private PostalHubRequest postalHubRequest;
    private List<PostalHub> postalHubs;
    @BeforeEach
    public void setUp() {
        postalHubRequest = new PostalHubRequest();
        postalHubs = new ArrayList<>();
    }

    @Test
    public void testValidatePostalHubRequest_Success() {
        PostalHub postalHub = new PostalHub();
        postalHub.setTenantId("tenantId");
        postalHub.setName("name");
        postalHub.setPincode("123456");

        PostalHubRequest request = new PostalHubRequest();
        request.setPostalHubs(Collections.singletonList(postalHub));

        assertDoesNotThrow(() -> postalHubValidator.validatePostalHubRequest(request));
    }

    @Test
    public void testValidatePostalHubRequest_MissingTenantId() {
        PostalHub postalHub = new PostalHub();
        postalHub.setName("name");
        postalHub.setPincode("123456");

        PostalHubRequest request = new PostalHubRequest();
        request.setPostalHubs(Collections.singletonList(postalHub));

        CustomException exception = assertThrows(CustomException.class, () -> postalHubValidator.validatePostalHubRequest(request));
        assertEquals("DK_PC_TENANT_ERR", exception.getCode());
        assertEquals("tenantId is mandatory for creating postal hub", exception.getMessage());
    }

    @Test
    public void testValidatePostalHubRequest_MissingName() {
        PostalHub postalHub = new PostalHub();
        postalHub.setTenantId("tenantId");
        postalHub.setPincode("123465");

        PostalHubRequest request = new PostalHubRequest();
        request.setPostalHubs(Collections.singletonList(postalHub));

        CustomException exception = assertThrows(CustomException.class, () -> postalHubValidator.validatePostalHubRequest(request));
        assertEquals("DK_PC_NAME_ERR", exception.getCode());
        assertEquals("name is mandatory for creating postal hub", exception.getMessage());
    }

    @Test
    public void testValidatePostalHubRequest_MissingPincode() {
        PostalHub postalHub = new PostalHub();
        postalHub.setTenantId("tenantId");
        postalHub.setName("name");

        PostalHubRequest request = new PostalHubRequest();
        request.setPostalHubs(Collections.singletonList(postalHub));

        CustomException exception = assertThrows(CustomException.class, () -> postalHubValidator.validatePostalHubRequest(request));
        assertEquals("DK_PC_PINCODE_ERR", exception.getCode());
        assertEquals("pincode is mandatory for creating postal hub", exception.getMessage());
    }

    @Test
    public void testValidateExistingPostalHubRequest_Success() {
        PostalHub postalHub = new PostalHub();
        postalHub.setHubId("hubId");

        PostalHubRequest request = new PostalHubRequest();
        request.setPostalHubs(Collections.singletonList(postalHub));

        assertDoesNotThrow(() -> postalHubValidator.validateExistingPostalHubRequest(request));
    }

    @Test
    public void testValidateExistingPostalHubRequest_MissingHubId() {
        PostalHub postalHub = new PostalHub();

        PostalHubRequest request = new PostalHubRequest();
        request.setPostalHubs(Collections.singletonList(postalHub));

        CustomException exception = assertThrows(CustomException.class, () -> postalHubValidator.validateExistingPostalHubRequest(request));
        assertEquals("DK_PC_ID_ERR", exception.getCode());
    }
}
