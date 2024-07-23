package org.pucar.dristi.web.controllers;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.TaskService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:50.003326400+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("")
public class TaskApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    public TaskApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @RequestMapping(value = "/task/v1/create", method = RequestMethod.POST)
    public ResponseEntity<TaskResponse> taskV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "details for the creation of task", schema = @Schema()) @Valid @RequestBody TaskRequest body) {
        Task task = taskService.createTask(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        TaskResponse taskResponse = TaskResponse.builder().task(task).responseInfo(responseInfo).build();
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/task/v1/exists", method = RequestMethod.POST)
    public ResponseEntity<TaskExistsResponse> taskV1ExistsPost(@Parameter(in = ParameterIn.DEFAULT, description = "check if the task(S) exists", required = true, schema = @Schema()) @Valid @RequestBody TaskExistsRequest body) {
        TaskExists taskExists = taskService.existTask(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        TaskExistsResponse taskExistsResponse = TaskExistsResponse.builder().task(taskExists).responseInfo(responseInfo).build();
        return new ResponseEntity<>(taskExistsResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/task/v1/search", method = RequestMethod.POST)
    public ResponseEntity<TaskListResponse> taskV1SearchPost(@NotNull @Parameter(in = ParameterIn.DEFAULT, description = "RequestInfo for the creation of task", schema = @Schema()) @Valid @RequestBody RequestInfo requestInfo, @NotNull @Parameter(in = ParameterIn.QUERY, description = "the order ID whose task(s) are being queried", required = true, schema = @Schema()) @Valid @RequestParam(value = "orderId", required = true) UUID orderId, @NotNull @Parameter(in = ParameterIn.QUERY, description = "the cnrNumber of the case whose task(s) are being queried", required = true, schema = @Schema()) @Valid @RequestParam(value = "cnrNumber", required = true) String cnrNumber, @Parameter(in = ParameterIn.QUERY, description = "id of the task(s) being searched", schema = @Schema()) @Valid @RequestParam(value = "id", required = false) String id, @Parameter(in = ParameterIn.QUERY, description = "tenantId whose task(s) are being searched", schema = @Schema()) @Valid @RequestParam(value = "tenantId", required = false) String tenantId, @Parameter(in = ParameterIn.QUERY, description = "the status of the task(s) being searched", schema = @Schema()) @Valid @RequestParam(value = "status", required = false) String status) {
        List<Task> tasks = taskService.searchTask(id, tenantId, status, orderId, cnrNumber, requestInfo);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        TaskListResponse taskListResponse = TaskListResponse.builder().list(tasks).totalCount(tasks.size()).responseInfo(responseInfo).build();
        return new ResponseEntity<>(taskListResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/task/v1/update", method = RequestMethod.POST)
    public ResponseEntity<TaskResponse> taskV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "details for the update of task", schema = @Schema()) @Valid @RequestBody TaskRequest body) {
        Task task = taskService.updateTask(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        TaskResponse taskResponse = TaskResponse.builder().task(task).responseInfo(responseInfo).build();
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

}
