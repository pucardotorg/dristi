package org.pucar.dristi.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.HearingExists;
import org.pucar.dristi.web.models.HearingExistsRequest;
import org.pucar.dristi.web.models.HearingExistsResponse;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class HearingUtilTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private Configuration configs;

    @InjectMocks
    private HearingUtil hearingUtil;

    @BeforeEach
    void setUp() {
        when(configs.getHearingHost()).thenReturn("http://localhost:8080");
        when(configs.getHearingExistsPath()).thenReturn("/hearingExists");
    }

    @Test
    void testFetchHearingDetailsSuccess() {
        HearingExistsRequest request = new HearingExistsRequest();
        Map<String, Object> response = new HashMap<>();
        response.put("criteria", List.of(Map.of("exists", true)));

        HearingExistsResponse hearingExistsResponse = HearingExistsResponse.builder()
                .order(HearingExists.builder().exists(true).build())
                .build();

        when(restTemplate.postForObject(any(String.class), eq(request), eq(Map.class)))
                .thenReturn(response);
        when(mapper.convertValue(response, HearingExistsResponse.class))
                .thenReturn(hearingExistsResponse);

        Boolean result = hearingUtil.fetchHearingDetails(request);
        assertTrue(result);
    }

    @Test
    void testFetchHearingDetailsDoesNotExist() {
        HearingExistsRequest request = new HearingExistsRequest();
        Map<String, Object> response = new HashMap<>();
        response.put("criteria", List.of(Map.of("exists", false)));

        HearingExistsResponse hearingExistsResponse = HearingExistsResponse.builder()
                .order(HearingExists.builder().exists(false).build())
                .build();

        when(restTemplate.postForObject(any(String.class), eq(request), eq(Map.class)))
                .thenReturn(response);
        when(mapper.convertValue(response, HearingExistsResponse.class))
                .thenReturn(hearingExistsResponse);

        Boolean result = hearingUtil.fetchHearingDetails(request);
        assertFalse(result);
    }

    @Test
    void testFetchHearingDetailsException() {
        HearingExistsRequest request = new HearingExistsRequest();

        when(restTemplate.postForObject(any(String.class), eq(request), eq(Map.class)))
                .thenThrow(new RuntimeException("Error"));
        assertThrows(RuntimeException.class, () -> {
            hearingUtil.fetchHearingDetails(request);
        });
    }
}
