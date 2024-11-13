package digit.web.controllers;


import digit.service.ReScheduleHearingService;
import digit.util.ResponseInfoFactory;
import digit.web.models.*;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@RestController("reScheduleHearingApiController")
@RequestMapping("")
@Slf4j
public class ReScheduleHearingController {

    @Autowired
    private ReScheduleHearingService reScheduleHearingService;

    @RequestMapping(value = "/hearing/v1/_reschedule", method = RequestMethod.POST)
    public ResponseEntity<ReScheduleHearingResponse> reScheduleHearing(@Parameter(in = ParameterIn.DEFAULT, description = "Hearing Details and Request Info", required = true, schema = @Schema()) @Valid @RequestBody ReScheduleHearingRequest request) {
        log.info("api = /hearing/v1/_reschedule, result = IN_PROGRESS");
        List<ReScheduleHearing> scheduledHearings = reScheduleHearingService.create(request);
        ReScheduleHearingResponse response = ReScheduleHearingResponse.builder().ResponseInfo(ResponseInfoFactory.createResponseInfo(request.getRequestInfo(), true))
                .reScheduleHearings(scheduledHearings).build();
        log.info("api = /hearing/v1/_reschedule, result = SUCCESS");
        return ResponseEntity.accepted().body(response);
    }


    @RequestMapping(value = "/hearing/v1/reschedule/_search", method = RequestMethod.POST)
    public ResponseEntity<ReScheduleHearingResponse> searchRescheduleHearing(@Parameter(in = ParameterIn.DEFAULT, description = "Hearing Details and Request Info", required = true, schema = @Schema()) @Valid @RequestBody ReScheduleHearingReqSearchRequest request, @NotNull @Min(0) @Max(1000) @ApiParam(value = "Pagination - limit records in response", required = true) @Valid @RequestParam(value = "limit", required = true) Integer limit, @NotNull @Min(1) @ApiParam(value = "Pagination - offset for which response is returned", required = true) @Valid @RequestParam(value = "offset", required = true) Integer offset) {
        log.info("api = /hearing/v1/reschedule/_search, result = IN_PROGRESS");
        List<ReScheduleHearing> scheduledHearings = reScheduleHearingService.search(request, limit, offset);
        ReScheduleHearingResponse response = ReScheduleHearingResponse.builder().ResponseInfo(ResponseInfoFactory.createResponseInfo(request.getRequestInfo(), true))
                .reScheduleHearings(scheduledHearings).build();
        log.info("api = /hearing/v1/reschedule/_search, result = SUCCESS");
        return ResponseEntity.accepted().body(response);
    }


    @RequestMapping(value = "/hearing/v1/bulk/_reschedule", method = RequestMethod.POST)
    public ResponseEntity<ReScheduleHearingResponse> bulkRescheduleHearing(@Parameter(in = ParameterIn.DEFAULT, description = "Hearing Details and Request Info", required = true, schema = @Schema()) @Valid @RequestBody BulkReScheduleHearingRequest request) {
        log.info("api =/hearing/v1/bulk/_reschedule, result = IN_PROGRESS");
        List<ReScheduleHearing> scheduledHearings = reScheduleHearingService.bulkReschedule(request);
        ReScheduleHearingResponse response = ReScheduleHearingResponse.builder().ResponseInfo(ResponseInfoFactory.createResponseInfo(request.getRequestInfo(), true))
                .reScheduleHearings(scheduledHearings).build();
        log.info("api =/hearing/v1/bulk/_reschedule, result = SUCCESS");
        return ResponseEntity.accepted().body(response);
    }
}
