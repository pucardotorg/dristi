package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AdvocateUtilTest {

    @InjectMocks
    private AdvocateUtil advocateUtil;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private Configuration configs;

    private final String ADVOCATE_HOST = "http://localhost:8080";
    private final String ADVOCATE_PATH = "/advocate/search";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(configs.getAdvocateHost()).thenReturn(ADVOCATE_HOST);
        when(configs.getAdvocatePath()).thenReturn(ADVOCATE_PATH);
    }

    @Test
    public void testFetchAdvocateDetailsSuccess() {
        RequestInfo requestInfo = new RequestInfo();
        String advocateId = "advocateId";

        AdvocateSearchRequest advocateSearchRequest = new AdvocateSearchRequest();
        advocateSearchRequest.setRequestInfo(requestInfo);
        AdvocateSearchCriteria criteria = new AdvocateSearchCriteria();
        criteria.setId(advocateId);
        criteria.setResponseList(List.of(Advocate.builder().id(UUID.randomUUID()).applicationNumber("appNumber").isActive(true).build()));
        advocateSearchRequest.setCriteria(List.of(criteria));

        Map<String, Object> response = new HashMap<>();
        when(restTemplate.postForObject(anyString(), any(AdvocateSearchRequest.class), eq(Map.class)))
                .thenReturn(response);

        AdvocateListResponse advocateResponse = new AdvocateListResponse();
        advocateResponse.setAdvocates(List.of(criteria));
        when(mapper.convertValue(any(), eq(AdvocateListResponse.class))).thenReturn(advocateResponse);

        Boolean result = advocateUtil.fetchAdvocateDetails(requestInfo, advocateId);

        assertTrue(result);
        verify(restTemplate, times(1)).postForObject(anyString(), any(AdvocateSearchRequest.class), eq(Map.class));
        verify(mapper, times(1)).convertValue(any(), eq(AdvocateListResponse.class));
    }

    @Test
    public void testFetchAdvocateDetailsFailure() {
        RequestInfo requestInfo = new RequestInfo();
        String advocateId = "advocateId";

        when(restTemplate.postForObject(anyString(), any(AdvocateSearchRequest.class), eq(Map.class)))
                .thenThrow(new RuntimeException("Error"));

        assertThrows(Exception.class, () -> advocateUtil.fetchAdvocateDetails(requestInfo, advocateId));
    }

    @Test
    public void testFetchAdvocateDetailsEmptyResponse() {
        RequestInfo requestInfo = new RequestInfo();
        String advocateId = "advocateId";
        AdvocateSearchRequest advocateSearchRequest = new AdvocateSearchRequest();
        advocateSearchRequest.setRequestInfo(requestInfo);
        AdvocateSearchCriteria criteria = new AdvocateSearchCriteria();
        criteria.setId(advocateId);
        criteria.setResponseList(new ArrayList<>());
        advocateSearchRequest.setCriteria(List.of(criteria));

        Map<String, Object> response = new HashMap<>();
        when(restTemplate.postForObject(anyString(), any(AdvocateSearchRequest.class), eq(Map.class)))
                .thenReturn(response);

        AdvocateListResponse advocateResponse = new AdvocateListResponse();
        advocateResponse.setAdvocates(List.of(criteria));
        when(mapper.convertValue(any(), eq(AdvocateListResponse.class))).thenReturn(advocateResponse);

        Boolean result = advocateUtil.fetchAdvocateDetails(requestInfo, advocateId);

        assertFalse(result);
        verify(restTemplate, times(1)).postForObject(anyString(), any(AdvocateSearchRequest.class), eq(Map.class));
        verify(mapper, times(1)).convertValue(any(), eq(AdvocateListResponse.class));
    }
}
