package org.pucar.dristi.web.controllers;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.HearingService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:11.072458+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("")
public class HearingApiController {

    private HearingService hearingService;
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    public HearingApiController(HearingService hearingService, ResponseInfoFactory responseInfoFactory) {
        this.hearingService = hearingService;
        this.responseInfoFactory = responseInfoFactory;
    }

    @RequestMapping(value = "/v1/create", method = RequestMethod.POST)
    public ResponseEntity<HearingResponse> hearingV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new hearing + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody HearingRequest body) {

        Hearing hearing = hearingService.createHearing(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        HearingResponse hearingResponse = HearingResponse.builder().hearing(hearing).responseInfo(responseInfo).build();
        return new ResponseEntity<>(hearingResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/exists", method = RequestMethod.POST)
    public ResponseEntity<HearingExistsResponse> hearingV1ExistsPost(@Parameter(in = ParameterIn.DEFAULT, description = "check if the Hearing(S) exists", required = true, schema = @Schema()) @Valid @RequestBody HearingExistsRequest body) {

        HearingExists order = hearingService.isHearingExist(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        HearingExistsResponse hearingExistsResponse = HearingExistsResponse.builder().order(order).responseInfo(responseInfo).build();
        return new ResponseEntity<>(hearingExistsResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/search", method = RequestMethod.POST)
    public ResponseEntity<HearingListResponse> hearingV1SearchPost(
            @Parameter(in = ParameterIn.DEFAULT, required=true, schema=@Schema()) @Valid @RequestBody HearingSearchRequest request)
    {
        List<Hearing> hearingList = hearingService.searchHearing(request);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true);
        int totalCount;
        if(request.getPagination() != null){
            totalCount = request.getPagination().getTotalCount().intValue();
        }else{
            totalCount = hearingList.size();
        }
        HearingListResponse hearingListResponse = HearingListResponse.builder().hearingList(hearingList).totalCount(totalCount).responseInfo(responseInfo).build();
        return new ResponseEntity<>(hearingListResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/update", method = RequestMethod.POST)
    public ResponseEntity<HearingResponse> hearingV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the update hearing(s) + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody HearingRequest body) {

        Hearing hearing = hearingService.updateHearing(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        HearingResponse hearingResponse = HearingResponse.builder().hearing(hearing).responseInfo(responseInfo).build();
        return new ResponseEntity<>(hearingResponse, HttpStatus.OK);

    }

    @PostMapping(value = "/v1/update_transcript_additional_attendees")
    public ResponseEntity<HearingResponse> hearingV1UpdateHearingTranscriptAdditionAuditDetailsPost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the update hearing(s) + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody HearingRequest body) {

        Hearing hearing = hearingService.updateTranscriptAdditionalAttendees(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        HearingResponse hearingResponse = HearingResponse.builder().hearing(hearing).responseInfo(responseInfo).build();
        return new ResponseEntity<>(hearingResponse, HttpStatus.OK);

    }

}

