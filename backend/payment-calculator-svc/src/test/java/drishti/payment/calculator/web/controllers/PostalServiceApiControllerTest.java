package drishti.payment.calculator.web.controllers;



import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import drishti.payment.calculator.service.PostalPinService;
import drishti.payment.calculator.util.ResponseInfoFactory;
import drishti.payment.calculator.web.models.PostalService;
import drishti.payment.calculator.web.models.PostalServiceRequest;
import drishti.payment.calculator.web.models.PostalServiceResponse;
import drishti.payment.calculator.web.models.PostalServiceSearchRequest;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PostalServiceApiControllerTest {

    @Mock
    private PostalPinService postalService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @InjectMocks
    private PostalServiceApiController postalServiceController;

    private PostalServiceRequest request;
    private List<PostalService> postalServices;
    private PostalServiceResponse postalServiceResponse;
    private PostalServiceSearchRequest searchRequest;

    @BeforeEach
    void setUp() {
        // Initialize the test data
        searchRequest = new PostalServiceSearchRequest();
        searchRequest.setRequestInfo(new RequestInfo());

        request = new PostalServiceRequest();
        request.setRequestInfo(new RequestInfo());

        PostalService postalService = new PostalService();
        postalServices = Collections.singletonList(postalService);

        ResponseInfo responseInfo = new ResponseInfo();

        postalServiceResponse = PostalServiceResponse.builder()
                .postal(postalServices)
                .responseInfo(responseInfo)
                .build();
    }

    @Test
    void createHub_success() {
        when(postalService.create(request)).thenReturn(postalServices);

        ResponseEntity<PostalServiceResponse> responseEntity = postalServiceController.createHub(request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(responseEntity.getBody().getPostal()).isEqualTo(postalServiceResponse.getPostal());
        verify(postalService).create(request);
    }

    @Test
    void createHub_serviceFailure() {
        when(postalService.create(request)).thenThrow(new RuntimeException("Service failure"));

        assertThatThrownBy(() -> postalServiceController.createHub(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Service failure");

        verify(postalService).create(request);
    }

    @Test
    void searchHub_success() {
        when(postalService.search(searchRequest)).thenReturn(postalServices);

        ResponseEntity<PostalServiceResponse> responseEntity = postalServiceController.searchHub(searchRequest);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(responseEntity.getBody().getPostal()).isEqualTo(postalServiceResponse.getPostal());
        verify(postalService).search(searchRequest);
    }

    @Test
    void searchHub_failure() {
        when(postalService.search(searchRequest)).thenThrow(new RuntimeException("Service failure"));

        assertThatThrownBy(() -> postalServiceController.searchHub(searchRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Service failure");

        verify(postalService).search(searchRequest);
    }

    @Test
    void updateHub_success() {
        when(postalService.update(request)).thenReturn(postalServices);

        ResponseEntity<PostalServiceResponse> responseEntity = postalServiceController.updateHub(request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(responseEntity.getBody().getPostal()).isEqualTo(postalServiceResponse.getPostal());
        verify(postalService).update(request);
    }

    @Test
    void updateHub_failure() {
        when(postalService.update(request)).thenThrow(new RuntimeException("Service failure"));

        assertThatThrownBy(() -> postalServiceController.updateHub(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Service failure");

        verify(postalService).update(request);
    }
//    @Test
//    void createHub_invalidRequest() {
//        // Assuming that request validation fails
//        PostalServiceRequest invalidRequest = new PostalServiceRequest();
//
//        // Optionally, mock validation exception handling
//        // when(postalService.create(invalidRequest)).thenThrow(new MethodArgumentNotValidException());
//
//        // Validate that the controller returns bad request or handles validation error as expected
//        // This can vary based on your actual validation mechanism and controller advice setup
//    }
}

