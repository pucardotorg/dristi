package org.pucar.dristi.web.controllers;

import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.TaskService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

import jakarta.validation.Valid;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:50.003326400+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("")
public class TaskApiController {

    private TaskService taskService;

    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    public TaskApiController(TaskService taskService, ResponseInfoFactory responseInfoFactory) {
        this.taskService = taskService;
        this.responseInfoFactory = responseInfoFactory;
    }

    public void setMockInjects(TaskService taskService, ResponseInfoFactory responseInfoFactory){
        this.taskService = taskService;
        this.responseInfoFactory = responseInfoFactory;
    }

    @RequestMapping(value = "/v1/create", method = RequestMethod.POST)
    public ResponseEntity<TaskResponse> taskV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "details for the creation of task", schema = @Schema()) @Valid @RequestBody TaskRequest body) {
        Task task = taskService.createTask(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        TaskResponse taskResponse = TaskResponse.builder().task(task).responseInfo(responseInfo).build();
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/exists", method = RequestMethod.POST)
    public ResponseEntity<TaskExistsResponse> taskV1ExistsPost(@Parameter(in = ParameterIn.DEFAULT, description = "check if the task(S) exists", required = true, schema = @Schema()) @Valid @RequestBody TaskExistsRequest body) {
        TaskExists taskExists = taskService.existTask(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        TaskExistsResponse taskExistsResponse = TaskExistsResponse.builder().task(taskExists).responseInfo(responseInfo).build();
        return new ResponseEntity<>(taskExistsResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/search", method = RequestMethod.POST)
    public ResponseEntity<TaskListResponse> taskV1SearchPost( @Parameter(in = ParameterIn.DEFAULT, schema = @Schema()) @Valid @RequestBody TaskSearchRequest request){
        List<Task> tasks = taskService.searchTask(request);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true);
        int totalCount;
        if (request.getPagination() != null) {
            totalCount = request.getPagination().getTotalCount().intValue();
        } else {
            totalCount = tasks.size();
        }
        TaskListResponse taskListResponse = TaskListResponse.builder().list(tasks).totalCount(totalCount).pagination(request.getPagination()).responseInfo(responseInfo).build();
        return new ResponseEntity<>(taskListResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/update", method = RequestMethod.POST)
    public ResponseEntity<TaskResponse> taskV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "details for the update of task", schema = @Schema()) @Valid @RequestBody TaskRequest body) {
        Task task = taskService.updateTask(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        TaskResponse taskResponse = TaskResponse.builder().task(task).responseInfo(responseInfo).build();
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/uploadDocument", method = RequestMethod.POST)
    public ResponseEntity<TaskResponse> taskV1UploadDocument(@Parameter(in = ParameterIn.DEFAULT, description = "details for the update of task", schema = @Schema()) @Valid @RequestBody TaskRequest body) {
        Task task = taskService.uploadDocument(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        TaskResponse taskResponse = TaskResponse.builder().task(task).responseInfo(responseInfo).build();
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

}
