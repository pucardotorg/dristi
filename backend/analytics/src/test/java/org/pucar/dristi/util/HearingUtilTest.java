package org.pucar.dristi.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.util.HearingUtil;
import org.pucar.dristi.util.Util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.HEARING_PATH;

@ExtendWith(MockitoExtension.class)
class HearingUtilTest {

    @Mock
    private Configuration config;

    @Mock
    private ServiceRequestRepository repository;

    @Mock
    private Util util;

    @InjectMocks
    private HearingUtil hearingUtil;

    private JSONObject request;
    private String applicationNumber;
    private String cnrNumber;
    private String hearingId;
    private String tenantId;

    @BeforeEach
    void setUp() {
        request = new JSONObject();
        applicationNumber = "app-123";
        cnrNumber = "cnr-123";
        hearingId = "hearing-123";
        tenantId = "tenant-123";

        when(config.getHearingHost()).thenReturn("http://localhost");
        when(config.getHearingSearchPath()).thenReturn("/hearing/search");
    }

    @Test
    void testGetHearing_Success() throws Exception {

        String mockResponse = "{\"hearings\": [{\"id\": \"hearing-123\"}]}";
        JSONArray mockHearingArray = new JSONArray();
        mockHearingArray.put(new JSONObject().put("id", "hearing-123"));

        when(repository.fetchResult(any(), any(JSONObject.class))).thenReturn(mockResponse);
        when(util.constructArray(mockResponse, HEARING_PATH)).thenReturn(mockHearingArray);

        Object result = hearingUtil.getHearing(request, applicationNumber, cnrNumber, hearingId, tenantId);

        assertNotNull(result);
        assertInstanceOf(JSONObject.class, result);
        assertEquals("hearing-123", ((JSONObject) result).getString("id"));

        verify(repository, times(1)).fetchResult(any(StringBuilder.class), any(JSONObject.class));
        verify(util, times(1)).constructArray(mockResponse, HEARING_PATH);
    }

    @Test
    void testGetHearing_Exception() throws Exception {

        when(repository.fetchResult(any(), any(JSONObject.class))).thenThrow(new RuntimeException("Fetch error"));

        Exception exception = assertThrows(RuntimeException.class, () -> hearingUtil.getHearing(request, applicationNumber, cnrNumber, hearingId, tenantId));

        assertEquals("Fetch error", exception.getCause().getMessage());

        verify(repository, times(1)).fetchResult(any(StringBuilder.class), any(JSONObject.class));
        verify(util, times(0)).constructArray(anyString(), anyString());
    }
}
