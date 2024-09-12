package com.egov.icops_integrationkerala.service;

import com.egov.icops_integrationkerala.enrichment.IcopsEnrichment;
import com.egov.icops_integrationkerala.kafka.Producer;
import com.egov.icops_integrationkerala.model.*;
import com.egov.icops_integrationkerala.util.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IcopsService {


    private final AuthUtil authUtil;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final IcopsEnrichment icopsEnrichment;

    private final ProcessRequestUtil processRequestUtil;

    private final RequestInfoGenerator requestInfoGenerator;

    private final PoliceJurisdictionUtil policeJurisdictionUtil;


    private final Producer producer;


    @Autowired
    public IcopsService(AuthUtil authUtil, AuthenticationManager authenticationManager,
                        JwtUtil jwtUtil, IcopsEnrichment icopsEnrichment, ProcessRequestUtil processRequestUtil, RequestInfoGenerator requestInfoGenerator, PoliceJurisdictionUtil policeJurisdictionUtil, Producer producer) {

        this.authUtil = authUtil;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.icopsEnrichment = icopsEnrichment;
        this.processRequestUtil = processRequestUtil;
        this.requestInfoGenerator = requestInfoGenerator;
        this.policeJurisdictionUtil = policeJurisdictionUtil;
        this.producer = producer;
    }


    public ChannelMessage sendRequestToIcops(TaskRequest taskRequest) throws Exception {

        ProcessRequest processRequest = icopsEnrichment.getProcessRequest(taskRequest);

        Coordinate coordinate = taskRequest.getTask().getTaskDetails().getRespondentDetails().getAddress().getCoordinate();

        if (coordinate == null) {
            return createFailureResponse(taskRequest, processRequest, "coordinate object is missing in address field of respondentDetails");
        }

        String latitude = coordinate.getLatitude();
        String longitude = coordinate.getLongitude();

        if (latitude == null || latitude.trim().isEmpty() || longitude == null || longitude.trim().isEmpty()) {
            return createFailureResponse(taskRequest, processRequest, "latitude or longitude data is missing or empty in coordinate field inside Address of respondentDetails");
        }

        Location location = Location.builder()
                .latitude(taskRequest.getTask().getTaskDetails().getRespondentDetails().getAddress().getCoordinate().getLatitude())
                .longitude(taskRequest.getTask().getTaskDetails().getRespondentDetails().getAddress().getCoordinate().getLongitude()).build();

        LocationBasedJurisdiction locationBasedJurisdiction = getLocationBasedJurisdiction(location);

        processRequest.setProcessPoliceStationCode(locationBasedJurisdiction.getIncludedJurisdiction().getCode());
        processRequest.setProcessPoliceStationName(locationBasedJurisdiction.getIncludedJurisdiction().getStation());

        AuthResponse authResponse = authUtil.authenticateAndGetToken();

        ChannelMessage channelMessage = processRequestUtil.callProcessRequest(authResponse, processRequest);

        IcopsTracker icopsTracker = null;
        if(channelMessage.getAcknowledgementStatus().equalsIgnoreCase("SUCCESS")) {
            log.info("successfully send request to icops");
             icopsTracker = icopsEnrichment.createIcopsTrackerBody(taskRequest,processRequest,channelMessage,DeliveryStatus.STATUS_UNKNOWN);
        }
        else {
            log.error("Failure message",channelMessage.getFailureMsg());
            icopsTracker = icopsEnrichment.createIcopsTrackerBody(taskRequest,processRequest,channelMessage,DeliveryStatus.FAILED);
        }
        IcopsRequest request = IcopsRequest.builder().requestInfo(taskRequest.getRequestInfo()).icopsTracker(icopsTracker).build();
        producer.push("save-icops-tracker", request);

        return channelMessage;
    }

    private ChannelMessage createFailureResponse(TaskRequest taskRequest, ProcessRequest processRequest, String failureMessage) {
        log.error(failureMessage);

        ChannelMessage failureMessageResponse = new ChannelMessage();
        failureMessageResponse.setAcknowledgementStatus("FAILURE");
        failureMessageResponse.setFailureMsg(failureMessage);

        // Create and push IcopsTracker for failure scenario
        IcopsTracker icopsTracker = icopsEnrichment.createIcopsTrackerBody(taskRequest, processRequest, failureMessageResponse, DeliveryStatus.FAILED);
        IcopsRequest request = IcopsRequest.builder().requestInfo(taskRequest.getRequestInfo()).icopsTracker(icopsTracker).build();
        producer.push("save-icops-tracker", request);

        return failureMessageResponse;
    }

    public AuthResponse generateAuthToken(String serviceName, String serviceKey, String authType) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(serviceName, serviceKey));
            return jwtUtil.generateToken(serviceName);
        } catch (AuthenticationException e) {
            throw new Exception("Invalid Service Credentials");
        }
    }

    public ChannelMessage processPoliceReport(IcopsProcessReport icopsProcessReport) throws ProcessReportException {

        IcopsTracker icopsTracker = icopsEnrichment.enrichIcopsTrackerForUpdate(icopsProcessReport);
        updateIcopsTracker(icopsTracker,icopsProcessReport);
        RequestInfo requestInfo = requestInfoGenerator.generateSystemRequestInfo();
        IcopsRequest icopsRequest = IcopsRequest.builder().requestInfo(requestInfo).icopsTracker(icopsTracker).build();
        producer.push("update-icops-tracker",icopsRequest);
        return ChannelMessage.builder().acknowledgeUniqueNumber(icopsTracker.getTaskNumber()).acknowledgementStatus("SUCCESS").build();
    }

    private void updateIcopsTracker(IcopsTracker icopsTracker, IcopsProcessReport icopsProcessReport) {

        icopsTracker.setRowVersion(icopsTracker.getRowVersion() + 1);
        if(icopsProcessReport.getProcessActionStatus().equalsIgnoreCase("Executed")){
            icopsTracker.setDeliveryStatus(DeliveryStatus.DELIVERED);
            icopsTracker.setRemarks(icopsProcessReport.getProcessActionRemarks());
        }
        else if(icopsProcessReport.getProcessActionStatus().equalsIgnoreCase("Not Executed")) {
            icopsTracker.setDeliveryStatus(DeliveryStatus.NOT_DELIVERED);
            icopsTracker.setRemarks(icopsProcessReport.getProcessFailureReason());
        }
        else{
            icopsTracker.setDeliveryStatus(DeliveryStatus.IN_TRANSIT);
            icopsTracker.setRemarks(icopsProcessReport.getProcessFailureReason());
        }
    }

    public LocationBasedJurisdiction getLocationBasedJurisdiction(Location location) throws Exception {
        AuthResponse authResponse = authUtil.authenticateAndGetToken();
        return policeJurisdictionUtil.getLocationBasedJurisdiction(authResponse,location);
    }
}
