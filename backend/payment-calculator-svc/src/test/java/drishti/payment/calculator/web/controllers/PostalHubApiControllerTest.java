package drishti.payment.calculator.web.controllers;

import drishti.payment.calculator.service.PostalHubService;
import drishti.payment.calculator.web.models.HubSearchRequest;
import drishti.payment.calculator.web.models.PostalHub;
import drishti.payment.calculator.web.models.PostalHubRequest;
import drishti.payment.calculator.web.models.PostalHubResponse;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostalHubApiControllerTest {

    @Mock
    private PostalHubService postalHubService;


    @InjectMocks
    private PostalHubApiController postalHubController;

    private PostalHubRequest request;
    private List<PostalHub> postalHubs;
    private PostalHubResponse postalHubResponse;
    private HubSearchRequest searchRequest;

    @BeforeEach
    void setUp() {
        // Initialize the test data
        searchRequest = new HubSearchRequest();
        searchRequest.setRequestInfo(new RequestInfo());

        request = new PostalHubRequest();
        request.setRequestInfo(new RequestInfo());

        PostalHub postalHub = new PostalHub();
        postalHubs = Collections.singletonList(postalHub);

        ResponseInfo responseInfo = new ResponseInfo();

        postalHubResponse = PostalHubResponse.builder()
                .hubs(postalHubs)
                .responseInfo(responseInfo)
                .build();
    }

    @Test
    void createHub_success() {
        when(postalHubService.create(request)).thenReturn(postalHubs);

        ResponseEntity<PostalHubResponse> responseEntity = postalHubController.createHub(request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(responseEntity.getBody().getHubs()).isEqualTo(postalHubResponse.getHubs());
        verify(postalHubService).create(request);
    }

    @Test
    void createHub_serviceFailure() {
        when(postalHubService.create(request)).thenThrow(new RuntimeException("Service failure"));

        assertThatThrownBy(() -> postalHubController.createHub(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Service failure");

        verify(postalHubService).create(request);
    }

    @Test
    void searchHub_success() {
        when(postalHubService.search(searchRequest)).thenReturn(postalHubs);

        ResponseEntity<PostalHubResponse> responseEntity = postalHubController.searchHub(searchRequest);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(responseEntity.getBody().getHubs()).isEqualTo(postalHubResponse.getHubs());
        verify(postalHubService).search(any());
    }

    @Test
    void searchHub_serviceFailure() {
        when(postalHubService.search(searchRequest)).thenThrow(new RuntimeException("Service failure"));

        assertThatThrownBy(() -> postalHubController.searchHub(searchRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Service failure");

        verify(postalHubService).search(any());
    }

    @Test
    void updateHub_success() {
        when(postalHubService.update(request)).thenReturn(postalHubs);

        ResponseEntity<PostalHubResponse> responseEntity = postalHubController.updateHub(request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(responseEntity.getBody().getHubs()).isEqualTo(postalHubResponse.getHubs());
        verify(postalHubService).update(request);
    }

    @Test
    void updateHub_serviceFailure() {
        when(postalHubService.update(request)).thenThrow(new RuntimeException("Service failure"));

        assertThatThrownBy(() -> postalHubController.updateHub(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Service failure");

        verify(postalHubService).update(request);
    }
}

