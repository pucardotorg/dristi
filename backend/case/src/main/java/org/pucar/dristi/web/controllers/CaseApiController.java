package org.pucar.dristi.web.controllers;


import java.io.IOException;

import org.pucar.dristi.web.models.CaseExistsResponse;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CaseResponse;
import org.pucar.dristi.web.models.CaseSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-04T13:54:45.904122+05:30[Asia/Kolkata]")
@Controller
    @RequestMapping("")
    public class CaseApiController{

        private final ObjectMapper objectMapper;

        private final HttpServletRequest request;

        @Autowired
        public CaseApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
        }

                @RequestMapping(value="/case/v1/_create", method = RequestMethod.POST)
                public ResponseEntity<CaseResponse> caseV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new court case + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody CaseRequest body) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            return new ResponseEntity<CaseResponse>(HttpStatus.CREATED);
                            }

                        return new ResponseEntity<CaseResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/case/v1/_exists", method = RequestMethod.POST)
                public ResponseEntity<CaseExistsResponse> caseV1ExistsPost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the case(s) + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody CaseSearchRequest body) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<CaseExistsResponse>(objectMapper.readValue("{  \"pagination\" : {    \"offSet\" : 2.3021358869347655,    \"limit\" : 56.37376656633328,    \"sortBy\" : \"sortBy\",    \"totalCount\" : 7.061401241503109,    \"order\" : \"\"  },  \"criteria\" : [ {    \"filingNumber\" : \"filingNumber\",    \"exists\" : true,    \"cnrNumber\" : \"cnrNumber\"  }, {    \"filingNumber\" : \"filingNumber\",    \"exists\" : true,    \"cnrNumber\" : \"cnrNumber\"  } ],  \"requestInfo\" : {    \"userInfo\" : {      \"mobileNumber\" : \"mobileNumber\",      \"roles\" : [ {        \"tenantId\" : \"tenantId\",        \"name\" : \"name\",        \"description\" : \"description\",        \"id\" : \"id\"      }, {        \"tenantId\" : \"tenantId\",        \"name\" : \"name\",        \"description\" : \"description\",        \"id\" : \"id\"      } ],      \"tenantId\" : \"tenantId\",      \"emailId\" : \"emailId\",      \"id\" : 6,      \"userName\" : \"userName\",      \"uuid\" : \"uuid\"    },    \"ver\" : \"ver\",    \"requesterId\" : \"requesterId\",    \"authToken\" : \"authToken\",    \"action\" : \"action\",    \"msgId\" : \"msgId\",    \"correlationId\" : \"correlationId\",    \"apiId\" : \"apiId\",    \"did\" : \"did\",    \"key\" : \"key\",    \"ts\" : 0  }}", CaseExistsResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<CaseExistsResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<CaseExistsResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/case/v1/_search", method = RequestMethod.POST)
                public ResponseEntity<CaseResponse> caseV1SearchPost(@Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody CaseSearchRequest body) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            return new ResponseEntity<CaseResponse>(HttpStatus.ACCEPTED);
                            }

                        return new ResponseEntity<CaseResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/case/v1/_update", method = RequestMethod.POST)
                public ResponseEntity<CaseResponse> caseV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for updating all updatable fields in the court case + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody CaseRequest body) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            return new ResponseEntity<CaseResponse>(HttpStatus.ACCEPTED);
                            }

                        return new ResponseEntity<CaseResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

        }
