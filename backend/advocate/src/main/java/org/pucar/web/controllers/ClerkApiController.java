package org.pucar.web.controllers;

import java.io.IOException;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.pucar.service.AdvocateClerkService;
import org.pucar.util.ResponseInfoFactory;
import org.pucar.web.models.AdvocateClerk;
import org.pucar.web.models.AdvocateClerkRequest;
import org.pucar.web.models.AdvocateClerkResponse;
import org.pucar.web.models.AdvocateClerkSearchRequest;
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

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-04T05:55:27.937918+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
public class ClerkApiController {

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;
	@Autowired
	private AdvocateClerkService advocateClerkService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	@Autowired
	public ClerkApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@RequestMapping(value = "/clerk/v1/_create", method = RequestMethod.POST)
	public ResponseEntity<AdvocateClerkResponse> clerkV1CreatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details for the user registration + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateClerkRequest body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try{
				List<AdvocateClerk> advocateList = advocateClerkService.registerAdvocateRequest(body);
				ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
				AdvocateClerkResponse advocateClerkResponse = AdvocateClerkResponse.builder().clerks(advocateList).responseInfo(responseInfo).build();
				return new ResponseEntity<>(advocateClerkResponse, HttpStatus.OK);
			} catch (Exception e) {
			    return new ResponseEntity<AdvocateClerkResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
	      	}
		}

		return new ResponseEntity<AdvocateClerkResponse>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/clerk/v1/_search", method = RequestMethod.POST)
	public ResponseEntity<AdvocateClerkResponse> clerkV1SearchPost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateClerkSearchRequest body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("*/*")) {
			try {
				List<AdvocateClerk> applications = advocateClerkService.searchAdvocateApplications(body.getRequestInfo(), body.getCriteria(), body.getStatus());
				ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
				AdvocateClerkResponse response = AdvocateClerkResponse.builder().clerks(applications).responseInfo(responseInfo).build();
				return new ResponseEntity<>(response,HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<AdvocateClerkResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<AdvocateClerkResponse>(HttpStatus.BAD_REQUEST);

	}

	@RequestMapping(value = "/clerk/v1/_update", method = RequestMethod.POST)
	public ResponseEntity<AdvocateClerkResponse> clerkV1UpdatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details of the registered advocate + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateClerkRequest body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")){
			try {
				return new ResponseEntity<AdvocateClerkResponse>(objectMapper.readValue(
						"{  \"pagination\" : {    \"offSet\" : 5.637376656633329,    \"limit\" : 59.621339166831824,    \"sortBy\" : \"sortBy\",    \"totalCount\" : 2.3021358869347655,    \"order\" : \"\"  },  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"clerks\" : [ {    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"applicationNumber\" : \"applicationNumber\",    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    } ],    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"kl\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"individualId\" : \"individualId\",    \"isActive\" : true,    \"additionalDetails\" : { },    \"stateRegnNumber\" : \"stateRegnNumber\"  }, {    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"applicationNumber\" : \"applicationNumber\",    \"documents\" : [ {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    }, {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    } ],    \"auditDetails\" : {      \"lastModifiedTime\" : 1,      \"createdBy\" : \"createdBy\",      \"lastModifiedBy\" : \"lastModifiedBy\",      \"createdTime\" : 6    },    \"tenantId\" : \"kl\",    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"individualId\" : \"individualId\",    \"isActive\" : true,    \"additionalDetails\" : { },    \"stateRegnNumber\" : \"stateRegnNumber\"  } ]}",
						AdvocateClerkResponse.class), HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				return new ResponseEntity<AdvocateClerkResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<AdvocateClerkResponse>(HttpStatus.BAD_REQUEST);
	}

}
