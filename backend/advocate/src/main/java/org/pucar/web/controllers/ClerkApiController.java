package org.pucar.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

import java.util.List;

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

				List<AdvocateClerk> advocateList = advocateClerkService.registerAdvocateClerkRequest(body);
				ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
				AdvocateClerkResponse advocateClerkResponse = AdvocateClerkResponse.builder().clerks(advocateList).responseInfo(responseInfo).build();
				return new ResponseEntity<>(advocateClerkResponse, HttpStatus.OK);
	}

	@RequestMapping(value = "/clerk/v1/_search", method = RequestMethod.POST)
	public ResponseEntity<AdvocateClerkResponse> clerkV1SearchPost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateClerkSearchRequest body) {

				List<AdvocateClerk> applications = advocateClerkService.searchAdvocateClerkApplications(body.getRequestInfo(), body.getCriteria(), body.getStatus(), body.getApplicationNumber());
				ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
				AdvocateClerkResponse response = AdvocateClerkResponse.builder().clerks(applications).responseInfo(responseInfo).build();
				return new ResponseEntity<>(response,HttpStatus.OK);
	}

	@RequestMapping(value = "/clerk/v1/_update", method = RequestMethod.POST)
	public ResponseEntity<AdvocateClerkResponse> clerkV1UpdatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details of the registered advocate + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateClerkRequest body) {

				List<AdvocateClerk> advocateClerkList = advocateClerkService.updateAdvocateClerk(body);
				ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
				AdvocateClerkResponse advocateClerkResponse = AdvocateClerkResponse.builder().clerks(advocateClerkList).responseInfo(responseInfo).build();
				return new ResponseEntity<>(advocateClerkResponse, HttpStatus.OK);
	}

}
