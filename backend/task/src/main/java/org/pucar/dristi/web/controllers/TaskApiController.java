package org.pucar.dristi.web.controllers;

import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.TaskService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;

import java.util.UUID;
    import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.*;

    import jakarta.validation.constraints.*;
    import jakarta.validation.Valid;
    import jakarta.servlet.http.HttpServletRequest;
        import java.util.Optional;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:50.003326400+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("")
public class TaskApiController{

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

    @RequestMapping(value="/task/v1/create", method = RequestMethod.POST)
    public ResponseEntity<TaskResponse> taskV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "details for the creation of task", schema=@Schema()) @Valid @RequestBody TaskRequest body) {
        Task task = taskService.createCase(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        TaskResponse taskResponse = TaskResponse.builder().task(task).responseInfo(responseInfo).build();
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    @RequestMapping(value="/task/v1/exists", method = RequestMethod.POST)
    public ResponseEntity<TaskExistsResponse> taskV1ExistsPost(@Parameter(in = ParameterIn.DEFAULT, description = "check if the task(S) exists", required=true, schema=@Schema()) @Valid @RequestBody TaskExistsRequest body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<TaskExistsResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"taskList\" : [ {    \"filingNumber\" : \"filingNumber\",    \"exists\" : true,    \"cnrNumber\" : \"cnrNumber\",    \"taskId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\"  }, {    \"filingNumber\" : \"filingNumber\",    \"exists\" : true,    \"cnrNumber\" : \"cnrNumber\",    \"taskId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\"  } ]}", TaskExistsResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                return new ResponseEntity<TaskExistsResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<TaskExistsResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value="/task/v1/search", method = RequestMethod.POST)
    public ResponseEntity<TaskListResponse> taskV1SearchPost(@NotNull @Parameter(in = ParameterIn.QUERY, description = "the order ID whose task(s) are being queried" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "orderId", required = true) UUID orderId,@NotNull @Parameter(in = ParameterIn.QUERY, description = "the cnrNumber of the case whose task(s) are being queried" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "cnrNumber", required = true) String cnrNumber,@Parameter(in = ParameterIn.QUERY, description = "id of the task(s) being searched" ,schema=@Schema()) @Valid @RequestParam(value = "id", required = false) String id,@Parameter(in = ParameterIn.QUERY, description = "tenantId whose task(s) are being searched" ,schema=@Schema()) @Valid @RequestParam(value = "tenantId", required = false) String tenantId,@Parameter(in = ParameterIn.QUERY, description = "the status of the task(s) being searched" ,schema=@Schema()) @Valid @RequestParam(value = "status", required = false) String status) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<TaskListResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"TotalCount\" : 0,  \"list\" : [ {    \"dateCloseBy\" : \"2000-01-23\",    \"filingNumber\" : \"filingNumber\",    \"amount\" : {      \"amount\" : \"amount\",      \"paymentRefNumber\" : \"paymentRefNumber\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"type\" : \"fees, fine, refund\",      \"additionalDetails\" : \"additionalDetails\",      \"status\" : \"status\"    },    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"orderId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    } ],    \"taskDescription\" : \"taskDescription\",    \"cnrNumber\" : \"cnrNumber\",    \"dateClosed\" : \"2000-01-23\",    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"assignedTo\" : {      \"name\" : \"name\",      \"individualId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\"    },    \"taskType\" : \"document submission, summons, bail.cash, bail.surety warrant\",    \"createdDate\" : \"2000-01-23\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"taskDetails\" : { },    \"status\" : \"status\"  }, {    \"dateCloseBy\" : \"2000-01-23\",    \"filingNumber\" : \"filingNumber\",    \"amount\" : {      \"amount\" : \"amount\",      \"paymentRefNumber\" : \"paymentRefNumber\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"type\" : \"fees, fine, refund\",      \"additionalDetails\" : \"additionalDetails\",      \"status\" : \"status\"    },    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"orderId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    } ],    \"taskDescription\" : \"taskDescription\",    \"cnrNumber\" : \"cnrNumber\",    \"dateClosed\" : \"2000-01-23\",    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"assignedTo\" : {      \"name\" : \"name\",      \"individualId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\"    },    \"taskType\" : \"document submission, summons, bail.cash, bail.surety warrant\",    \"createdDate\" : \"2000-01-23\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"taskDetails\" : { },    \"status\" : \"status\"  } ]}", TaskListResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                return new ResponseEntity<TaskListResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<TaskListResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value="/task/v1/update", method = RequestMethod.POST)
    public ResponseEntity<TaskResponse> taskV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "details for the update of task", schema=@Schema()) @Valid @RequestBody TaskRequest body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<TaskResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"task\" : {    \"dateCloseBy\" : \"2000-01-23\",    \"filingNumber\" : \"filingNumber\",    \"amount\" : {      \"amount\" : \"amount\",      \"paymentRefNumber\" : \"paymentRefNumber\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"type\" : \"fees, fine, refund\",      \"additionalDetails\" : \"additionalDetails\",      \"status\" : \"status\"    },    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"orderId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    } ],    \"taskDescription\" : \"taskDescription\",    \"cnrNumber\" : \"cnrNumber\",    \"dateClosed\" : \"2000-01-23\",    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"assignedTo\" : {      \"name\" : \"name\",      \"individualId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\"    },    \"taskType\" : \"document submission, summons, bail.cash, bail.surety warrant\",    \"createdDate\" : \"2000-01-23\",    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"tenantId\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"taskDetails\" : { },    \"status\" : \"status\"  }}", TaskResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                return new ResponseEntity<TaskResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<TaskResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

}
