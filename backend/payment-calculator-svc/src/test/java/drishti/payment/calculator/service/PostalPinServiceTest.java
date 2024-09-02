package drishti.payment.calculator.service;

import drishti.payment.calculator.config.Configuration;
import drishti.payment.calculator.enrichment.PostalServiceEnrichment;
import drishti.payment.calculator.kafka.Producer;
import drishti.payment.calculator.repository.PostalServiceRepository;
import drishti.payment.calculator.validator.PostalServiceValidator;
import drishti.payment.calculator.web.models.PostalService;
import drishti.payment.calculator.web.models.PostalServiceRequest;
import drishti.payment.calculator.web.models.PostalServiceSearchRequest;
import org.junit.jupiter.api.BeforeEach;
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
public class PostalPinServiceTest {

    @InjectMocks
    private PostalPinService postalPinService;

    @Mock
    private PostalServiceRepository repository;

    @Mock
    private PostalServiceValidator validator;

    @Mock
    private PostalServiceEnrichment enrichment;

    @Mock
    private Producer producer;

    @Mock
    private Configuration config;

    @Test
    public void createPostalService() {
        PostalServiceRequest request = new PostalServiceRequest();
        List<PostalService> postalServices = Collections.singletonList(new PostalService());
        request.setPostalServices(postalServices);

        List<PostalService> result = postalPinService.create(request);

        verify(validator, times(1)).validatePostalServiceRequest(request);
        verify(enrichment, times(1)).enrichPostalServiceRequest(request);
        verify(producer, times(1)).push(config.getPostalServiceCreateTopic(), request.getPostalServices());

        assertEquals(postalServices, result);
    }

    @Test
    public void searchPostalService() {
        PostalServiceSearchRequest searchRequest = new PostalServiceSearchRequest();
        List<PostalService> postalServices = Collections.singletonList(new PostalService());

        when(repository.getPostalService(searchRequest.getCriteria())).thenReturn(postalServices);

        List<PostalService> result = postalPinService.search(searchRequest);

        assertEquals(postalServices, result);
    }

    @Test
    public void updatePostalService() {
        PostalServiceRequest request = new PostalServiceRequest();
        List<PostalService> postalServices = Collections.singletonList(new PostalService());
        request.setPostalServices(postalServices);

        List<PostalService> result = postalPinService.update(request);

        verify(validator, times(1)).validateExistingPostalServiceRequest(request);
        verify(enrichment, times(1)).enrichExistingPostalServiceRequest(request);
        verify(producer, times(1)).push(config.getPostalServiceUpdateTopic(), postalServices);

        assertEquals(postalServices, result);
    }
}
