package org.pucar.dristi.web.controllers;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.PendingTaskService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.PendingTask;
import org.pucar.dristi.web.models.PendingTaskRequest;
import org.pucar.dristi.web.models.PendingTaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:11.072458+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("")
public class PendingTaskApiController {

    private PendingTaskService pendingTaskService;
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    public PendingTaskApiController(PendingTaskService pendingTaskService, ResponseInfoFactory responseInfoFactory) {
        this.pendingTaskService = pendingTaskService;
        this.responseInfoFactory = responseInfoFactory;
    }

    @RequestMapping(value = "/pending_task/v1/create", method = RequestMethod.POST)
    public ResponseEntity<PendingTaskResponse> pendingTaskV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new pending task + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody PendingTaskRequest body) {

        PendingTask pendingTask = pendingTaskService.createPendingTask(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        PendingTaskResponse hearingResponse = PendingTaskResponse.builder().pendingTask(pendingTask).responseInfo(responseInfo).build();
        return new ResponseEntity<>(hearingResponse, HttpStatus.OK);
    }

}

