package org.pucar.dristi.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.UUID;
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:13:43.389623100+05:30[Asia/Calcutta]")
@Controller
    @RequestMapping("")
    public class OrderApiController{

        private final ObjectMapper objectMapper;

        private final HttpServletRequest request;

        @Autowired
        public OrderApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
        }

                @RequestMapping(value="/order/v1/create", method = RequestMethod.POST)
                public ResponseEntity<OrderResponse> orderV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new order + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody OrderRequest body) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<OrderResponse>(objectMapper.readValue("{  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"order\" : {    \"orderType\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],    \"hearingNumber\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"orderNumber\" : \"orderNumber\",    \"comments\" : \"comments\",    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    } ],    \"issuedBy\" : {      \"benchID\" : \"benchID\",      \"judgeID\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],      \"courtID\" : \"courtID\"    },    \"cnrNumber\" : \"cnrNumber\",    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"applicationIds\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],    \"statuteSection\" : {      \"subsections\" : [ \"subsections\", \"subsections\" ],      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"additionalDetails\" : \"additionalDetails\",      \"statute\" : \"statute\",      \"sections\" : [ \"sections\", \"sections\" ],      \"auditdetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      }    },    \"createdDate\" : \"2000-01-23\",    \"caseId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"tenantId\" : \"tenantId\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"orderCategory\" : \"Intermediate, Judgement\",    \"status\" : \"status\"  }}", OrderResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<OrderResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<OrderResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/order/v1/exists", method = RequestMethod.POST)
                public ResponseEntity<OrderExistsResponse> orderV1ExistsPost(@Parameter(in = ParameterIn.DEFAULT, description = "check if the order(S) exists", required=true, schema=@Schema()) @Valid @RequestBody OrderExistsRequest body) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<OrderExistsResponse>(objectMapper.readValue("{  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"order\" : {    \"exists\" : true,    \"cnrNumber\" : \"cnrNumber\"  }}", OrderExistsResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<OrderExistsResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<OrderExistsResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/order/v1/search", method = RequestMethod.POST)
                public ResponseEntity<OrderListResponse> orderV1SearchPost(@NotNull @Parameter(in = ParameterIn.QUERY, description = "the application ID whose order(s) are being queried" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "applicationId", required = true) UUID applicationId,@NotNull @Parameter(in = ParameterIn.QUERY, description = "the cnrNumber of the case whose order(s) are being queried" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "cnrNumber", required = true) String cnrNumber,@Parameter(in = ParameterIn.QUERY, description = "id of the order being searched" ,schema=@Schema()) @Valid @RequestParam(value = "id", required = false) String id,@Parameter(in = ParameterIn.QUERY, description = "tenantId whose order(s) are being searched" ,schema=@Schema()) @Valid @RequestParam(value = "tenantId", required = false) String tenantId,@Parameter(in = ParameterIn.QUERY, description = "the status of the order(s) being searched" ,schema=@Schema()) @Valid @RequestParam(value = "status", required = false) String status) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<OrderListResponse>(objectMapper.readValue("{  \"TotalCount\" : 0,  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"list\" : [ {    \"orderType\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],    \"hearingNumber\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"orderNumber\" : \"orderNumber\",    \"comments\" : \"comments\",    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    } ],    \"issuedBy\" : {      \"benchID\" : \"benchID\",      \"judgeID\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],      \"courtID\" : \"courtID\"    },    \"cnrNumber\" : \"cnrNumber\",    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"applicationIds\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],    \"statuteSection\" : {      \"subsections\" : [ \"subsections\", \"subsections\" ],      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"additionalDetails\" : \"additionalDetails\",      \"statute\" : \"statute\",      \"sections\" : [ \"sections\", \"sections\" ],      \"auditdetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      }    },    \"createdDate\" : \"2000-01-23\",    \"caseId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"tenantId\" : \"tenantId\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"orderCategory\" : \"Intermediate, Judgement\",    \"status\" : \"status\"  }, {    \"orderType\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],    \"hearingNumber\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"orderNumber\" : \"orderNumber\",    \"comments\" : \"comments\",    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    } ],    \"issuedBy\" : {      \"benchID\" : \"benchID\",      \"judgeID\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],      \"courtID\" : \"courtID\"    },    \"cnrNumber\" : \"cnrNumber\",    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"applicationIds\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],    \"statuteSection\" : {      \"subsections\" : [ \"subsections\", \"subsections\" ],      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"additionalDetails\" : \"additionalDetails\",      \"statute\" : \"statute\",      \"sections\" : [ \"sections\", \"sections\" ],      \"auditdetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      }    },    \"createdDate\" : \"2000-01-23\",    \"caseId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"tenantId\" : \"tenantId\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"orderCategory\" : \"Intermediate, Judgement\",    \"status\" : \"status\"  } ]}", OrderListResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<OrderListResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<OrderListResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/order/v1/update", method = RequestMethod.POST)
                public ResponseEntity<OrderResponse> orderV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the update order(s) + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody OrderRequest body) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<OrderResponse>(objectMapper.readValue("{  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"order\" : {    \"orderType\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],    \"hearingNumber\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"orderNumber\" : \"orderNumber\",    \"comments\" : \"comments\",    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    } ],    \"issuedBy\" : {      \"benchID\" : \"benchID\",      \"judgeID\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],      \"courtID\" : \"courtID\"    },    \"cnrNumber\" : \"cnrNumber\",    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"applicationIds\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],    \"statuteSection\" : {      \"subsections\" : [ \"subsections\", \"subsections\" ],      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"additionalDetails\" : \"additionalDetails\",      \"statute\" : \"statute\",      \"sections\" : [ \"sections\", \"sections\" ],      \"auditdetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      }    },    \"createdDate\" : \"2000-01-23\",    \"caseId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"tenantId\" : \"tenantId\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"orderCategory\" : \"Intermediate, Judgement\",    \"status\" : \"status\"  }}", OrderResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<OrderResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<OrderResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

        }
