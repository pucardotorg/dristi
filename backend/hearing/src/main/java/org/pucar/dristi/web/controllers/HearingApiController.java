package org.pucar.dristi.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.HearingService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:11.072458+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("")
public class HearingApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private HearingService hearingService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    public HearingApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @RequestMapping(value = "/hearing/v1/create", method = RequestMethod.POST)
    public ResponseEntity<HearingResponse> hearingV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new hearing + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody HearingRequest body) {

        Hearing hearing = hearingService.createHearing(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        HearingResponse hearingResponse = HearingResponse.builder().hearing(hearing).responseInfo(responseInfo).build();
        return new ResponseEntity<>(hearingResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/hearing/v1/exists", method = RequestMethod.POST)
    public ResponseEntity<HearingExistsResponse> hearingV1ExistsPost(@Parameter(in = ParameterIn.DEFAULT, description = "check if the Hearing(S) exists", required = true, schema = @Schema()) @Valid @RequestBody HearingExistsRequest body) {

        HearingExists order = hearingService.isHearingExist(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        HearingExistsResponse hearingExistsResponse = HearingExistsResponse.builder().order(order).responseInfo(responseInfo).build();
        return new ResponseEntity<>(hearingExistsResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/hearing/v1/search", method = RequestMethod.POST)
    public ResponseEntity<HearingListResponse> hearingV1SearchPost(@NotNull @Parameter(in = ParameterIn.QUERY, description = "the cnrNumber of the case whose hearing(s) are being queried", required = true, schema = @Schema()) @Valid @RequestParam(value = "cnrNumber", required = true) String cnrNumber,
                                                                   @NotNull @Parameter(in = ParameterIn.QUERY, description = "the applicationNumber of the case whose hearing(s) are being queried", required = true, schema = @Schema()) @Valid @RequestParam(value = "applicationNumber", required = true) String applicationNumber,
                                                                   @Parameter(in = ParameterIn.QUERY, description = "the hearing id", schema = @Schema()) @Valid @RequestParam(value = "hearingId", required = false) String hearingId,
                                                                   @Parameter(in = ParameterIn.QUERY, description = "Search by filingNumber", schema = @Schema()) @Valid @RequestParam(value = "filingNumber", required = false) String filingNumber,
                                                                   @Parameter(in = ParameterIn.QUERY, description = "Search by tenantId of case request", schema = @Schema()) @Valid @RequestParam(value = "tenantId", required = false) String tenantId,
                                                                   @Parameter(in = ParameterIn.QUERY, description = "search hearings within date range. if only fromDate is specified, then hearing for this data will be retrieved", schema = @Schema()) @Valid @RequestParam(value = "fromDate", required = false) LocalDate fromDate,
                                                                   @Parameter(in = ParameterIn.QUERY, description = "search hearings within date range", schema = @Schema()) @Valid @RequestParam(value = "toDate", required = false) LocalDate toDate,
                                                                   @Parameter(in = ParameterIn.QUERY, description = "No of record return", schema = @Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit,
                                                                   @Parameter(in = ParameterIn.QUERY, description = "offset", schema = @Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset,
                                                                   @Parameter(in = ParameterIn.QUERY, description = "sorted by ascending by default if this parameter is not provided", schema = @Schema()) @Valid @RequestParam(value = "sortBy", required = false) String sortBy) {

        List<Hearing> hearingList = hearingService.searchHearing(cnrNumber, applicationNumber, hearingId, filingNumber, tenantId, fromDate, toDate, limit, offset, sortBy);
        HearingListResponse hearingListResponse = HearingListResponse.builder().hearingList(hearingList).totalCount(hearingList.size()).build();
        return new ResponseEntity<>(hearingListResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/hearing/v1/update", method = RequestMethod.POST)
    public ResponseEntity<HearingResponse> hearingV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the update hearing(s) + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody HearingRequest body) {

        Hearing hearing = hearingService.updateHearing(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        HearingResponse hearingResponse = HearingResponse.builder().hearing(hearing).responseInfo(responseInfo).build();
        return new ResponseEntity<>(hearingResponse, HttpStatus.OK);

    }

}

