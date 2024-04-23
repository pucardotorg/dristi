package org.pucar.dristi.web.controllers;


import org.pucar.dristi.web.models.ApplicationExistsRequest;
import org.pucar.dristi.web.models.ApplicationExistsResponse;
import org.pucar.dristi.web.models.ApplicationListResponse;
import org.pucar.dristi.web.models.ApplicationRequest;
import org.pucar.dristi.web.models.ApplicationResponse;

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
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:12:15.132164900+05:30[Asia/Calcutta]")
@Controller
    @RequestMapping("")
    public class ApplicationApiController{

        private final ObjectMapper objectMapper;

        private final HttpServletRequest request;

        @Autowired
        public ApplicationApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
        }

                @RequestMapping(value="/application/v1/create", method = RequestMethod.POST)
                public ResponseEntity<ApplicationResponse> applicationV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new application + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody ApplicationRequest body) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<ApplicationResponse>(objectMapper.readValue("{  \"application\" : {    \"applicationType\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"applicationNumber\" : \"Application 1 of the year 2024\",    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    } ],    \"issuedBy\" : {      \"benchID\" : \"benchID\",      \"judgeID\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],      \"courtID\" : \"courtID\"    },    \"onBehalfOf\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],    \"cnrNumber\" : \"cnrNumber\",    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"referenceId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"statuteSection\" : {      \"subsections\" : [ \"subsections\", \"subsections\" ],      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"additionalDetails\" : \"additionalDetails\",      \"statute\" : \"statute\",      \"sections\" : [ \"sections\", \"sections\" ],      \"auditdetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      }    },    \"createdDate\" : \"createdDate\",    \"createdBy\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"caseId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"tenantId\" : \"tenantId\",    \"comment\" : \"comment\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"status\" : \"status\"  },  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", ApplicationResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<ApplicationResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<ApplicationResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/application/v1/exists", method = RequestMethod.POST)
                public ResponseEntity<ApplicationExistsResponse> applicationV1ExistsPost(@Parameter(in = ParameterIn.DEFAULT, description = "check if the application(S) exists", required=true, schema=@Schema()) @Valid @RequestBody ApplicationExistsRequest body) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<ApplicationExistsResponse>(objectMapper.readValue("{  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"order\" : {    \"exists\" : true,    \"cnrNumber\" : \"cnrNumber\"  }}", ApplicationExistsResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<ApplicationExistsResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<ApplicationExistsResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/application/v1/search", method = RequestMethod.POST)
                public ResponseEntity<ApplicationListResponse> applicationV1SearchPost(@Parameter(in = ParameterIn.QUERY, description = "Search by application ID" ,schema=@Schema()) @Valid @RequestParam(value = "id", required = false) String id,@Parameter(in = ParameterIn.QUERY, description = "Search by application number" ,schema=@Schema()) @Valid @RequestParam(value = "applicationNumber", required = false) String applicationNumber,@Parameter(in = ParameterIn.QUERY, description = "Search by case ID" ,schema=@Schema()) @Valid @RequestParam(value = "caseId", required = false) UUID caseId,@Parameter(in = ParameterIn.QUERY, description = "the cnrNumber of the case whose task(s) are being queried" ,schema=@Schema()) @Valid @RequestParam(value = "cnrNumber", required = false) String cnrNumber,@Parameter(in = ParameterIn.QUERY, description = "Search by tenantId" ,schema=@Schema()) @Valid @RequestParam(value = "tenantId", required = false) UUID tenantId,@Parameter(in = ParameterIn.QUERY, description = "No of records return" ,schema=@Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit,@Parameter(in = ParameterIn.QUERY, description = "offset" ,schema=@Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset,@Parameter(in = ParameterIn.QUERY, description = "sorted by ascending by default if this parameter is not provided" ,schema=@Schema()) @Valid @RequestParam(value = "sortBy", required = false) String sortBy) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<ApplicationListResponse>(objectMapper.readValue("{  \"HearingList\" : [ {    \"applicationType\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"applicationNumber\" : \"Application 1 of the year 2024\",    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    } ],    \"issuedBy\" : {      \"benchID\" : \"benchID\",      \"judgeID\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],      \"courtID\" : \"courtID\"    },    \"onBehalfOf\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],    \"cnrNumber\" : \"cnrNumber\",    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"referenceId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"statuteSection\" : {      \"subsections\" : [ \"subsections\", \"subsections\" ],      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"additionalDetails\" : \"additionalDetails\",      \"statute\" : \"statute\",      \"sections\" : [ \"sections\", \"sections\" ],      \"auditdetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      }    },    \"createdDate\" : \"createdDate\",    \"createdBy\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"caseId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"tenantId\" : \"tenantId\",    \"comment\" : \"comment\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"status\" : \"status\"  }, {    \"applicationType\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"applicationNumber\" : \"Application 1 of the year 2024\",    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    } ],    \"issuedBy\" : {      \"benchID\" : \"benchID\",      \"judgeID\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],      \"courtID\" : \"courtID\"    },    \"onBehalfOf\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],    \"cnrNumber\" : \"cnrNumber\",    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"referenceId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"statuteSection\" : {      \"subsections\" : [ \"subsections\", \"subsections\" ],      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"additionalDetails\" : \"additionalDetails\",      \"statute\" : \"statute\",      \"sections\" : [ \"sections\", \"sections\" ],      \"auditdetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      }    },    \"createdDate\" : \"createdDate\",    \"createdBy\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"caseId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"tenantId\" : \"tenantId\",    \"comment\" : \"comment\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"status\" : \"status\"  } ],  \"TotalCount\" : 0,  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", ApplicationListResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<ApplicationListResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<ApplicationListResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/application/v1/update", method = RequestMethod.POST)
                public ResponseEntity<ApplicationResponse> applicationV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the update application(s) + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody ApplicationRequest body) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<ApplicationResponse>(objectMapper.readValue("{  \"application\" : {    \"applicationType\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"applicationNumber\" : \"Application 1 of the year 2024\",    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    } ],    \"issuedBy\" : {      \"benchID\" : \"benchID\",      \"judgeID\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],      \"courtID\" : \"courtID\"    },    \"onBehalfOf\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],    \"cnrNumber\" : \"cnrNumber\",    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"referenceId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"statuteSection\" : {      \"subsections\" : [ \"subsections\", \"subsections\" ],      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"additionalDetails\" : \"additionalDetails\",      \"statute\" : \"statute\",      \"sections\" : [ \"sections\", \"sections\" ],      \"auditdetails\" : {        \"lastModifiedTime\" : 1,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 6      }    },    \"createdDate\" : \"createdDate\",    \"createdBy\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"caseId\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"tenantId\" : \"tenantId\",    \"comment\" : \"comment\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"status\" : \"status\"  },  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", ApplicationResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<ApplicationResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<ApplicationResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

        }
