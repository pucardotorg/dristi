package org.pucar.dristi.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateListResponse;
import org.pucar.dristi.web.models.AdvocateSearchCriteria;
import org.pucar.dristi.web.models.AdvocateSearchRequest;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

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

        Boolean result = advocateUtil.doesAdvocateExist(requestInfo, advocateId);

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

        assertThrows(Exception.class, () -> advocateUtil.doesAdvocateExist(requestInfo, advocateId));
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

        Boolean result = advocateUtil.doesAdvocateExist(requestInfo, advocateId);

        assertFalse(result);
        verify(restTemplate, times(1)).postForObject(anyString(), any(AdvocateSearchRequest.class), eq(Map.class));
        verify(mapper, times(1)).convertValue(any(), eq(AdvocateListResponse.class));
    }

    @Test
    public void testFetchAdvocatesByIndividualIdSuccess() {

        RequestInfo requestInfo = new RequestInfo();
        String individualId = "individualId";

        AdvocateSearchCriteria criteria = new AdvocateSearchCriteria();
        criteria.setIndividualId(individualId);
        Advocate advocate = Advocate.builder().id(UUID.randomUUID()).applicationNumber("appNumber").individualId(individualId).isActive(true).build();
        List<Advocate> advocates = List.of(advocate);
        criteria.setResponseList(advocates);
        // Mock the fetchAdvocates method to return a list of advocates
        when(restTemplate.postForObject(anyString(), any(AdvocateSearchRequest.class), eq(Map.class)))
                .thenReturn(new HashMap<>());

        AdvocateListResponse advocateResponse = new AdvocateListResponse();
        advocateResponse.setAdvocates(List.of(criteria));
        when(mapper.convertValue(any(), eq(AdvocateListResponse.class))).thenReturn(advocateResponse);

        List<Advocate> result = advocateUtil.fetchAdvocatesByIndividualId(requestInfo, individualId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(advocates, result);
        assertEquals(result.get(0).getIndividualId(),individualId);

        verify(restTemplate, times(1)).postForObject(anyString(), any(AdvocateSearchRequest.class), eq(Map.class));
        verify(mapper, times(1)).convertValue(any(), eq(AdvocateListResponse.class));
    }

    @Test
    public void testFetchAdvocatesByIndividualIdEmptyResponse() {
        RequestInfo requestInfo = new RequestInfo();
        String individualId = "individualId";

        AdvocateSearchCriteria criteria = new AdvocateSearchCriteria();
        criteria.setIndividualId(individualId);
        Advocate advocate = Advocate.builder().id(UUID.randomUUID()).applicationNumber("appNumber").individualId(individualId).isActive(false).build();
        List<Advocate> advocates = List.of(advocate);
        criteria.setResponseList(advocates);
        Map<String, Object> response = new HashMap<>();
        when(restTemplate.postForObject(anyString(), any(AdvocateSearchRequest.class), eq(Map.class)))
                .thenReturn(response);

        AdvocateListResponse advocateResponse = new AdvocateListResponse();
        advocateResponse.setAdvocates(List.of(criteria));
        when(mapper.convertValue(any(), eq(AdvocateListResponse.class))).thenReturn(advocateResponse);

        List<Advocate> result = advocateUtil.fetchAdvocatesByIndividualId(requestInfo, individualId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(restTemplate, times(1)).postForObject(anyString(), any(AdvocateSearchRequest.class), eq(Map.class));
        verify(mapper, times(1)).convertValue(any(), eq(AdvocateListResponse.class));
    }

    @Test
    public void testFetchAdvocatesByIndividualIdException() {
        RequestInfo requestInfo = new RequestInfo();
        String individualId = "individualId";

        when(restTemplate.postForObject(anyString(), any(AdvocateSearchRequest.class), eq(Map.class)))
                .thenThrow(new RuntimeException("Error"));

        assertThrows(CustomException.class, () -> advocateUtil.fetchAdvocatesByIndividualId(requestInfo, individualId));
    }
}
