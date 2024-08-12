package org.pucar.dristi.web.controllers;


<<<<<<< HEAD
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.HearingService;
import org.pucar.dristi.util.ResponseInfoFactory;
=======
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
>>>>>>> main
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
<<<<<<< HEAD
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

    @RequestMapping(value = "/v1/update/time", method = RequestMethod.POST)
    public ResponseEntity<UpdateTimeResponse> hearingV1UpdateTimePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the update start and end time + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody UpdateTimeRequest body) {

        hearingService.updateStartAndTime(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        UpdateTimeResponse hearingResponse = UpdateTimeResponse.builder().hearings(body.getHearings()).responseInfo(responseInfo).build();
        return new ResponseEntity<>(hearingResponse, HttpStatus.OK);

=======
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:11.072458+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("")
public class HearingApiController{

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    public HearingApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @RequestMapping(value="/hearing/v1/create", method = RequestMethod.POST)
    public ResponseEntity<HearingResponse> hearingV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new hearing + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody HearingRequest body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<HearingResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"hearing\" : {    \"filingNumber\" : [ \"filingNumber\", \"filingNumber\" ],    \"hearingType\" : \"admission, trail, judgment, evidence, plea\",    \"notes\" : \"notes\",    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    } ],    \"attendees\" : [ {      \"associatedWith\" : \"associatedWith\",      \"name\" : \"name\",      \"wasPresent\" : true,      \"individualId\" : \"individualId\",      \"type\" : \"complainant, respondent, lawyer, witness\"    }, {      \"associatedWith\" : \"associatedWith\",      \"name\" : \"name\",      \"wasPresent\" : true,      \"individualId\" : \"individualId\",      \"type\" : \"complainant, respondent, lawyer, witness\"    } ],    \"cnrNumbers\" : [ \"cnrNumbers\", \"cnrNumbers\" ],    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"applicationNumbers\" : [ \"applicationNumbers\", \"applicationNumbers\" ],    \"transcript\" : [ \"transcript\", \"transcript\" ],    \"vcLink\" : \"http://example.com/aeiou\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"startTime\" : \"2000-01-23\",    \"presidedBy\" : {      \"benchID\" : \"benchID\",      \"judgeID\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],      \"courtID\" : \"courtID\"    },    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"endTime\" : \"2000-01-23\",    \"status\" : false  }}", HearingResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                return new ResponseEntity<HearingResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<HearingResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value="/hearing/v1/exists", method = RequestMethod.POST)
    public ResponseEntity<HearingExistsResponse> hearingV1ExistsPost(@Parameter(in = ParameterIn.DEFAULT, description = "check if the Hearing(S) exists", required=true, schema=@Schema()) @Valid @RequestBody HearingExistsRequest body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<HearingExistsResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"hearingList\" : [ {    \"filingNumber\" : \"filingNumber\",    \"applicationNumber\" : \"applicationNumber\",    \"exists\" : true,    \"hearingId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"cnrNumber\" : \"cnrNumber\"  }, {    \"filingNumber\" : \"filingNumber\",    \"applicationNumber\" : \"applicationNumber\",    \"exists\" : true,    \"hearingId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"cnrNumber\" : \"cnrNumber\"  } ]}", HearingExistsResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                return new ResponseEntity<HearingExistsResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<HearingExistsResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value="/hearing/v1/search", method = RequestMethod.POST)
    public ResponseEntity<HearingListResponse> hearingV1SearchPost(@NotNull @Parameter(in = ParameterIn.QUERY, description = "the cnrNumber of the case whose hearing(s) are being queried" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "cnrNumber", required = true) String cnrNumber,@NotNull @Parameter(in = ParameterIn.QUERY, description = "the applicationNumber of the case whose hearing(s) are being queried" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "applicationNumber", required = true) String applicationNumber,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "id", required = false) String id,@Parameter(in = ParameterIn.QUERY, description = "Search by filingNumber" ,schema=@Schema()) @Valid @RequestParam(value = "filingNumber", required = false) String filingNumber,@Parameter(in = ParameterIn.QUERY, description = "Search by tenantId of case request" ,schema=@Schema()) @Valid @RequestParam(value = "tenantId", required = false) String tenantId,@Parameter(in = ParameterIn.QUERY, description = "search hearings within date range. if only fromDate is specified, then hearing for this data will be retrieved" ,schema=@Schema()) @Valid @RequestParam(value = "fromDate", required = false) LocalDate fromDate,@Parameter(in = ParameterIn.QUERY, description = "search hearings within date range" ,schema=@Schema()) @Valid @RequestParam(value = "toDate", required = false) LocalDate toDate,@Parameter(in = ParameterIn.QUERY, description = "No of record return" ,schema=@Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit,@Parameter(in = ParameterIn.QUERY, description = "offset" ,schema=@Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset,@Parameter(in = ParameterIn.QUERY, description = "sorted by ascending by default if this parameter is not provided" ,schema=@Schema()) @Valid @RequestParam(value = "sortBy", required = false) String sortBy) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<HearingListResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"HearingList\" : [ {    \"filingNumber\" : [ \"filingNumber\", \"filingNumber\" ],    \"hearingType\" : \"admission, trail, judgment, evidence, plea\",    \"notes\" : \"notes\",    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    } ],    \"attendees\" : [ {      \"associatedWith\" : \"associatedWith\",      \"name\" : \"name\",      \"wasPresent\" : true,      \"individualId\" : \"individualId\",      \"type\" : \"complainant, respondent, lawyer, witness\"    }, {      \"associatedWith\" : \"associatedWith\",      \"name\" : \"name\",      \"wasPresent\" : true,      \"individualId\" : \"individualId\",      \"type\" : \"complainant, respondent, lawyer, witness\"    } ],    \"cnrNumbers\" : [ \"cnrNumbers\", \"cnrNumbers\" ],    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"applicationNumbers\" : [ \"applicationNumbers\", \"applicationNumbers\" ],    \"transcript\" : [ \"transcript\", \"transcript\" ],    \"vcLink\" : \"http://example.com/aeiou\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"startTime\" : \"2000-01-23\",    \"presidedBy\" : {      \"benchID\" : \"benchID\",      \"judgeID\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],      \"courtID\" : \"courtID\"    },    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"endTime\" : \"2000-01-23\",    \"status\" : false  }, {    \"filingNumber\" : [ \"filingNumber\", \"filingNumber\" ],    \"hearingType\" : \"admission, trail, judgment, evidence, plea\",    \"notes\" : \"notes\",    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    } ],    \"attendees\" : [ {      \"associatedWith\" : \"associatedWith\",      \"name\" : \"name\",      \"wasPresent\" : true,      \"individualId\" : \"individualId\",      \"type\" : \"complainant, respondent, lawyer, witness\"    }, {      \"associatedWith\" : \"associatedWith\",      \"name\" : \"name\",      \"wasPresent\" : true,      \"individualId\" : \"individualId\",      \"type\" : \"complainant, respondent, lawyer, witness\"    } ],    \"cnrNumbers\" : [ \"cnrNumbers\", \"cnrNumbers\" ],    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"applicationNumbers\" : [ \"applicationNumbers\", \"applicationNumbers\" ],    \"transcript\" : [ \"transcript\", \"transcript\" ],    \"vcLink\" : \"http://example.com/aeiou\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"startTime\" : \"2000-01-23\",    \"presidedBy\" : {      \"benchID\" : \"benchID\",      \"judgeID\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],      \"courtID\" : \"courtID\"    },    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"endTime\" : \"2000-01-23\",    \"status\" : false  } ],  \"TotalCount\" : 0}", HearingListResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                return new ResponseEntity<HearingListResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<HearingListResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value="/hearing/v1/update", method = RequestMethod.POST)
    public ResponseEntity<HearingResponse> hearingV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the update hearing(s) + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody HearingRequest body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<HearingResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"hearing\" : {    \"filingNumber\" : [ \"filingNumber\", \"filingNumber\" ],    \"hearingType\" : \"admission, trail, judgment, evidence, plea\",    \"notes\" : \"notes\",    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    } ],    \"attendees\" : [ {      \"associatedWith\" : \"associatedWith\",      \"name\" : \"name\",      \"wasPresent\" : true,      \"individualId\" : \"individualId\",      \"type\" : \"complainant, respondent, lawyer, witness\"    }, {      \"associatedWith\" : \"associatedWith\",      \"name\" : \"name\",      \"wasPresent\" : true,      \"individualId\" : \"individualId\",      \"type\" : \"complainant, respondent, lawyer, witness\"    } ],    \"cnrNumbers\" : [ \"cnrNumbers\", \"cnrNumbers\" ],    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"applicationNumbers\" : [ \"applicationNumbers\", \"applicationNumbers\" ],    \"transcript\" : [ \"transcript\", \"transcript\" ],    \"vcLink\" : \"http://example.com/aeiou\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"startTime\" : \"2000-01-23\",    \"presidedBy\" : {      \"benchID\" : \"benchID\",      \"judgeID\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],      \"courtID\" : \"courtID\"    },    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"endTime\" : \"2000-01-23\",    \"status\" : false  }}", HearingResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                return new ResponseEntity<HearingResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<HearingResponse>(HttpStatus.NOT_IMPLEMENTED);
>>>>>>> main
    }

}

