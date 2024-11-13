package digit.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import digit.config.Configuration;
import digit.repository.ServiceRequestRepository;
import digit.web.models.hearing.Hearing;
import digit.web.models.hearing.HearingListSearchRequest;
import digit.web.models.hearing.HearingSearchCriteria;
import digit.web.models.hearing.HearingUpdateBulkRequest;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.ServiceCallException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HearingUtilTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Configuration configuration;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @InjectMocks
    private HearingUtil hearingUtil;

    private HearingUpdateBulkRequest updateBulkRequest = new HearingUpdateBulkRequest();
    private RequestInfo requestInfo;
    private HearingListSearchRequest searchRequest = mock(HearingListSearchRequest.class);

    @BeforeEach
    void setUp() {
       requestInfo = new RequestInfo();
       updateBulkRequest.setRequestInfo(requestInfo);
       searchRequest.setRequestInfo(requestInfo);

       lenient().when(objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)).thenReturn(objectMapper);
    }
    @Test
    public void testCallHearing_Success() {
        Hearing hearing = new Hearing();
        hearing.setTenantId("tenantId");
        hearing.setStartTime(1614556800000L);
        hearing.setEndTime(1614556800000L);
        updateBulkRequest.setHearings(List.of(hearing));

        when(configuration.getHearingHost()).thenReturn("http://hearing-host");
        when(configuration.getHearingUpdateEndPoint()).thenReturn("/hearing/_update");
        when(serviceRequestRepository.fetchResult(any(), any())).thenReturn(null);

        hearingUtil.callHearing(updateBulkRequest);

        verify(objectMapper).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        verify(serviceRequestRepository, times(1)).fetchResult(any(), any());
    }

    @Test
    public void testCallHearing_Failure() {
        Hearing hearing = new Hearing();
        updateBulkRequest.setHearings(List.of(hearing));

        when(configuration.getHearingHost()).thenReturn("http://hearing-host");
        when(configuration.getHearingUpdateEndPoint()).thenReturn("/hearing/_update");
        when(serviceRequestRepository.fetchResult(any(), any())).thenThrow(new ServiceCallException("ServiceCallException"));

        hearingUtil.callHearing(updateBulkRequest);
    }

    @Test
    public void testFetchHearingSuccess() throws JsonProcessingException {
        HearingListSearchRequest request = mock(HearingListSearchRequest.class);
        String hearingHost = "http://hearing-service";
        String hearingSearchEndPoint = "/search";
        StringBuilder expectedUri = new StringBuilder(hearingHost.concat(hearingSearchEndPoint));
        Object response = new Object(); // Replace with actual object if needed
        JsonNode jsonNode = mock(JsonNode.class);
        JsonNode hearingListNode = mock(JsonNode.class);
        List<Hearing> expectedHearingList = List.of(new Hearing()); // Replace with actual Hearing list

        when(configuration.getHearingHost()).thenReturn(hearingHost);
        when(configuration.getHearingSearchEndPoint()).thenReturn(hearingSearchEndPoint);
        when(serviceRequestRepository.fetchResult(any(), any())).thenReturn(response);
        when(objectMapper.valueToTree(response)).thenReturn(jsonNode);
        when(jsonNode.get("HearingList")).thenReturn(hearingListNode);
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(expectedHearingList);
        // Act
        List<Hearing> actualHearingList = hearingUtil.fetchHearing(request);

        // Assert
        assertEquals(expectedHearingList, actualHearingList);
    }

    @Test
    public void testFetchHearingException() throws JsonProcessingException {
        HearingSearchCriteria searchCriteria = mock(HearingSearchCriteria.class);
        searchRequest.setCriteria(searchCriteria);
        Object response = new Object();
        JsonNode jsonNode = mock(JsonNode.class);
        JsonNode hearingListNode = mock(JsonNode.class);

        when(configuration.getHearingHost()).thenReturn("http://hearing-service");
        when(configuration.getHearingSearchEndPoint()).thenReturn("/search");
        when(serviceRequestRepository.fetchResult(any(), any())).thenReturn(response);
        when(objectMapper.valueToTree(response)).thenReturn(jsonNode);
        when(jsonNode.get("HearingList")).thenReturn(hearingListNode);
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(new ServiceCallException("ServiceCallException"));

        hearingUtil.fetchHearing(searchRequest);

    }
}
