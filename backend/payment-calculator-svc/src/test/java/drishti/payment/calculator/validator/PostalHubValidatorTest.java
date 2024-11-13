package drishti.payment.calculator.validator;

import drishti.payment.calculator.repository.PostalHubRepository;
import drishti.payment.calculator.web.models.PostalHub;
import drishti.payment.calculator.web.models.PostalHubRequest;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostalHubValidatorTest {

    @InjectMocks
    private PostalHubValidator postalHubValidator;

    @Mock
    private PostalHubRepository hubRepository;


    @Test
    @DisplayName("do validate create hub request")
    public void doValidateCreateHubRequest() {
        PostalHub postalHub = PostalHub.builder().tenantId("pb")
                .name("HubName")
                .pincode("693201").build();

        PostalHubRequest request = PostalHubRequest.builder().postalHubs(Collections.singletonList(postalHub)).build();
        when(hubRepository.getPostalHub(any())).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> postalHubValidator.validateCreateHubRequest(request));
    }

    @Test
    @DisplayName("do validate create hub request hub already exist")
    public void doValidateCreateHubRequestAlreadyExistHub() {
        PostalHub postalHub = PostalHub.builder().tenantId("pb")
                .name("HubName")
                .pincode("693201").build();

        PostalHubRequest request = PostalHubRequest.builder().postalHubs(Collections.singletonList(postalHub)).build();
        when(hubRepository.getPostalHub(any())).thenReturn(Collections.singletonList(postalHub));

        CustomException exception = assertThrows(CustomException.class, () -> postalHubValidator.validateCreateHubRequest(request));
        assertEquals("HUB_ALREADY_EXIST", exception.getCode());
        assertEquals("Hub already exist in DB", exception.getMessage());
    }


    @Test
    @DisplayName("do validate existing postal hub request")
    public void doValidateExistingPostalHubRequest() {
        PostalHub postalHub = PostalHub.builder().hubId("hubId").tenantId("pb")
                .name("HubName")
                .pincode("693201").build();
        PostalHubRequest request = PostalHubRequest.builder().postalHubs(Collections.singletonList(postalHub)).build();

        assertDoesNotThrow(() -> postalHubValidator.validateExistingPostalHubRequest(request));
    }

    @Test
    @DisplayName("do validate existing postal hub request with missing hub id")
    public void doValidateExistingPostalHubRequestMissingHubId() {
        PostalHub postalHub = PostalHub.builder().tenantId("pb")
                .name("HubName")
                .pincode("693201").build();
        PostalHubRequest request = PostalHubRequest.builder().postalHubs(Collections.singletonList(postalHub)).build();

        CustomException exception = assertThrows(CustomException.class, () -> postalHubValidator.validateExistingPostalHubRequest(request));
        assertEquals("DK_PC_ID_ERR", exception.getCode());
    }
}
