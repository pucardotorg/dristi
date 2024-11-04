package digit.util;

import digit.config.Configuration;
import digit.web.models.LocationBasedJurisdiction;
import digit.web.models.LocationBasedJurisdictionResponse;
import digit.web.models.LocationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IcopsUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Configuration config;

    @InjectMocks
    private IcopsUtil icopsUtil;

    private LocationRequest locationRequest;
    private LocationBasedJurisdictionResponse jurisdictionResponse;

    @BeforeEach
    public void setUp() {
        locationRequest = new LocationRequest();
        jurisdictionResponse = new LocationBasedJurisdictionResponse();

        LocationBasedJurisdiction jurisdiction = new LocationBasedJurisdiction();
        jurisdictionResponse.setLocationBasedJurisdiction(jurisdiction);
    }

    @Test
    public void testGetLocationBasedJurisdiction() {
        String host = "http://example.com";
        String endpoint = "/location-endpoint";
        when(config.getICopsHost()).thenReturn(host);
        when(config.getICopsLocationEndPoint()).thenReturn(endpoint);

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(ResponseEntity.ok(jurisdictionResponse));

        LocationBasedJurisdiction result = icopsUtil.getLocationBasedJurisdiction(locationRequest);

        assertEquals(jurisdictionResponse.getLocationBasedJurisdiction(), result);
    }
}

