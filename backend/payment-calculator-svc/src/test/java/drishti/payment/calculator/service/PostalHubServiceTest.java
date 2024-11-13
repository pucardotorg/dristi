package drishti.payment.calculator.service;

import drishti.payment.calculator.config.Configuration;
import drishti.payment.calculator.enrichment.PostalHubEnrichment;
import drishti.payment.calculator.kafka.Producer;
import drishti.payment.calculator.repository.PostalHubRepository;
import drishti.payment.calculator.validator.PostalHubValidator;
import drishti.payment.calculator.web.models.HubSearchRequest;
import drishti.payment.calculator.web.models.PostalHub;
import drishti.payment.calculator.web.models.PostalHubRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostalHubServiceTest {

    @InjectMocks
    private PostalHubService postalHubService;

    @Mock
    private PostalHubRepository repository;

    @Mock
    private PostalHubValidator validator;

    @Mock
    private PostalHubEnrichment enrichment;

    @Mock
    private Producer producer;

    @Mock
    private Configuration config;

    @Test
    public void createPostalHub() {
        PostalHubRequest request = new PostalHubRequest();
        List<PostalHub> postalHubs = Collections.singletonList(new PostalHub());
        request.setPostalHubs(postalHubs);

        List<PostalHub> result = postalHubService.create(request);

//        verify(validator, times(1)).validatePostalHubRequest(request);
        verify(enrichment, times(1)).enrichPostalHubRequest(request);
        verify(producer, times(1)).push(config.getPostalHubCreateTopic(), request);
        assertEquals(postalHubs, result);
    }

    @Test
    public void searchPostalHub() {
        HubSearchRequest searchRequest = new HubSearchRequest();
        List<PostalHub> postalHubs = Collections.singletonList(new PostalHub());

        when(repository.getPostalHub(searchRequest.getCriteria())).thenReturn(postalHubs);

        List<PostalHub> result = postalHubService.search(searchRequest);

        assertEquals(postalHubs, result);
    }

    @Test
    public void updatePostalHub() {
        PostalHubRequest request = new PostalHubRequest();
        List<PostalHub> postalHubs = Collections.singletonList(new PostalHub());
        request.setPostalHubs(postalHubs);

        List<PostalHub> result = postalHubService.update(request);

        verify(validator, times(1)).validateExistingPostalHubRequest(request);
        verify(enrichment, times(1)).enrichExistingPostalHubRequest(request);
        verify(producer, times(1)).push(config.getPostalHubUpdateTopic(), request);
        assertEquals(postalHubs, result);
    }
}
