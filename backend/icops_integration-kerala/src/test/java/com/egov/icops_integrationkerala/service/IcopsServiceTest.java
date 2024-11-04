package com.egov.icops_integrationkerala.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.egov.icops_integrationkerala.enrichment.IcopsEnrichment;
import com.egov.icops_integrationkerala.kafka.Producer;
import com.egov.icops_integrationkerala.model.*;
import com.egov.icops_integrationkerala.util.*;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IcopsServiceTest {

    @Mock
    private AuthUtil authUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private IcopsEnrichment icopsEnrichment;

    @Mock
    private ProcessRequestUtil processRequestUtil;

    @Mock
    private RequestInfoGenerator requestInfoGenerator;

    @Mock
    private PoliceJurisdictionUtil policeJurisdictionUtil;

    @Mock
    private Producer producer;

    @InjectMocks
    private IcopsService icopsService;

    private TaskRequest taskRequest;
    private ProcessRequest processRequest;
    private Location location;
    private LocationBasedJurisdiction locationBasedJurisdiction;
    private AuthResponse authResponse;
    private ChannelMessage channelMessage;
    private IcopsTracker icopsTracker;
    private Task task;
    private IcopsProcessReport icopsProcessReport;

    private Address address;

    private Coordinate coordinate;

    private LocationRequest locationRequest;

    @BeforeEach
    void setUp() {
        taskRequest = mock(TaskRequest.class);
        task = mock(Task.class);
        processRequest = mock(ProcessRequest.class);
        location = mock(Location.class);
        locationBasedJurisdiction = mock(LocationBasedJurisdiction.class);
        authResponse = mock(AuthResponse.class);
        channelMessage = new ChannelMessage();
        icopsTracker = mock(IcopsTracker.class);
        icopsProcessReport = mock(IcopsProcessReport.class);
        address = mock(Address.class);
        coordinate = mock(Coordinate.class);
        locationRequest = new LocationRequest();
        locationRequest.setLocation(location);
    }

    @Test
    void testSendRequestToIcops_Success() throws Exception {
        when(icopsEnrichment.getProcessRequest(any(TaskRequest.class))).thenReturn(processRequest);
        when(authUtil.authenticateAndGetToken()).thenReturn(authResponse);
        when(processRequestUtil.callProcessRequest(any(AuthResponse.class), any(ProcessRequest.class))).thenReturn(channelMessage);
        when(policeJurisdictionUtil.getLocationBasedJurisdiction(any(AuthResponse.class), any(Location.class))).thenReturn(locationBasedJurisdiction);
        when(taskRequest.getTask()).thenReturn(task);
        when(task.getTaskDetails()).thenReturn(mock(TaskDetails.class));
        when(task.getTaskDetails().getRespondentDetails()).thenReturn(mock(RespondentDetails.class));
        when(taskRequest.getTask().getTaskDetails().getRespondentDetails().getAddress()).thenReturn(address);
        when(taskRequest.getTask().getTaskDetails().getRespondentDetails().getAddress().getCoordinate()).thenReturn(coordinate);
        when(taskRequest.getTask().getTaskDetails().getRespondentDetails().getAddress().getCoordinate().getLatitude()).thenReturn("latitude");
        when(taskRequest.getTask().getTaskDetails().getRespondentDetails().getAddress().getCoordinate().getLongitude()).thenReturn("longitude");
        when(locationBasedJurisdiction.getIncludedJurisdiction()).thenReturn(mock(PoliceStationDetails.class));
        when(locationBasedJurisdiction.getIncludedJurisdiction().getCode()).thenReturn("code");
        when(locationBasedJurisdiction.getIncludedJurisdiction().getStation()).thenReturn("station");
        channelMessage.setAcknowledgementStatus("SUCCESS");

        ChannelMessage result = icopsService.sendRequestToIcops(taskRequest);

        assertEquals("SUCCESS", result.getAcknowledgementStatus());
        verify(producer, times(1)).push(eq("save-icops-tracker"), any(IcopsRequest.class));
    }

    @Test
    void testSendRequestToIcops_Failure() throws Exception {
        when(icopsEnrichment.getProcessRequest(any(TaskRequest.class))).thenReturn(processRequest);
        when(authUtil.authenticateAndGetToken()).thenReturn(authResponse);
        when(processRequestUtil.callProcessRequest(any(AuthResponse.class), any(ProcessRequest.class))).thenReturn(channelMessage);
        when(policeJurisdictionUtil.getLocationBasedJurisdiction(any(AuthResponse.class), any(Location.class))).thenReturn(locationBasedJurisdiction);
        when(taskRequest.getTask()).thenReturn(task);
        when(task.getTaskDetails()).thenReturn(mock(TaskDetails.class));
        when(task.getTaskDetails().getRespondentDetails()).thenReturn(mock(RespondentDetails.class));
        when(taskRequest.getTask().getTaskDetails().getRespondentDetails().getAddress()).thenReturn(address);
        when(taskRequest.getTask().getTaskDetails().getRespondentDetails().getAddress().getCoordinate()).thenReturn(coordinate);
        when(taskRequest.getTask().getTaskDetails().getRespondentDetails().getAddress().getCoordinate().getLatitude()).thenReturn("latitude");
        when(taskRequest.getTask().getTaskDetails().getRespondentDetails().getAddress().getCoordinate().getLongitude()).thenReturn("longitude");
        when(locationBasedJurisdiction.getIncludedJurisdiction()).thenReturn(mock(PoliceStationDetails.class));
        when(locationBasedJurisdiction.getIncludedJurisdiction().getCode()).thenReturn("code");
        when(locationBasedJurisdiction.getIncludedJurisdiction().getStation()).thenReturn("station");
        channelMessage.setAcknowledgementStatus("FAILURE");
        channelMessage.setFailureMsg("Failure message");

        ChannelMessage result = icopsService.sendRequestToIcops(taskRequest);

        assertEquals("FAILURE", result.getAcknowledgementStatus());
        verify(producer, times(1)).push(eq("save-icops-tracker"), any(IcopsRequest.class));
    }

    @Test
    void testGenerateAuthToken_Success() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtUtil.generateToken(anyString())).thenReturn(authResponse);

        AuthResponse result = icopsService.generateAuthToken("serviceName", "serviceKey", "authType");

        assertNotNull(result);
    }

    @Test
    void testGenerateAuthToken_Failure() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException("Invalid Service Credentials"));

        assertThrows(Exception.class, () -> icopsService.generateAuthToken("serviceName", "serviceKey", "authType"));
    }

    @Test
    void testProcessPoliceReport_Success_1() throws ProcessReportException {
        when(icopsEnrichment.enrichIcopsTrackerForUpdate(any(IcopsProcessReport.class))).thenReturn(icopsTracker);
        when(requestInfoGenerator.generateSystemRequestInfo()).thenReturn(new RequestInfo());
        when(icopsTracker.getRowVersion()).thenReturn(1);
        when(icopsProcessReport.getProcessActionStatus()).thenReturn("Executed");
        ChannelMessage result = icopsService.processPoliceReport(icopsProcessReport);

        assertEquals("SUCCESS", result.getAcknowledgementStatus());
        verify(producer, times(1)).push(eq("update-icops-tracker"), any(IcopsRequest.class));
    }

    @Test
    void testProcessPoliceReport_Success_2() throws ProcessReportException {
        when(icopsEnrichment.enrichIcopsTrackerForUpdate(any(IcopsProcessReport.class))).thenReturn(icopsTracker);
        when(requestInfoGenerator.generateSystemRequestInfo()).thenReturn(new RequestInfo());
        when(icopsTracker.getRowVersion()).thenReturn(1);
        when(icopsProcessReport.getProcessActionStatus()).thenReturn("Not Executed");
        ChannelMessage result = icopsService.processPoliceReport(icopsProcessReport);

        assertEquals("SUCCESS", result.getAcknowledgementStatus());
        verify(producer, times(1)).push(eq("update-icops-tracker"), any(IcopsRequest.class));
    }

    @Test
    void testProcessPoliceReport_Success_3() throws ProcessReportException {
        when(icopsEnrichment.enrichIcopsTrackerForUpdate(any(IcopsProcessReport.class))).thenReturn(icopsTracker);
        when(requestInfoGenerator.generateSystemRequestInfo()).thenReturn(new RequestInfo());
        when(icopsTracker.getRowVersion()).thenReturn(1);
        when(icopsProcessReport.getProcessActionStatus()).thenReturn("Else");
        ChannelMessage result = icopsService.processPoliceReport(icopsProcessReport);

        assertEquals("SUCCESS", result.getAcknowledgementStatus());
        verify(producer, times(1)).push(eq("update-icops-tracker"), any(IcopsRequest.class));
    }

    @Test
    void testGetLocationBasedJurisdiction_Success() throws Exception {
        when(authUtil.authenticateAndGetToken()).thenReturn(authResponse);
        when(policeJurisdictionUtil.getLocationBasedJurisdiction(any(AuthResponse.class), any(Location.class)))
                .thenReturn(locationBasedJurisdiction);

        LocationBasedJurisdiction result = icopsService.getLocationBasedJurisdiction(locationRequest);

        assertNotNull(result);
    }
}