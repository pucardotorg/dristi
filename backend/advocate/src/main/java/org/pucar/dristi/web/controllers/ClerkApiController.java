package org.pucar.dristi.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.AdvocateClerkService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.AdvocateClerk;
import org.pucar.dristi.web.models.AdvocateClerkRequest;
import org.pucar.dristi.web.models.AdvocateClerkResponse;
import org.pucar.dristi.web.models.AdvocateClerkSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-04T05:55:27.937918+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
public class ClerkApiController {

	@Autowired
	private AdvocateClerkService advocateClerkService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	@Autowired
	public ClerkApiController(ObjectMapper objectMapper, HttpServletRequest request) {
	}
	public void setMockInjects(AdvocateClerkService advocateClerkService, ResponseInfoFactory responseInfoFactory){
		this.advocateClerkService = advocateClerkService;
		this.responseInfoFactory = responseInfoFactory;
	}
	@RequestMapping(value = "/clerk/v1/_create", method = RequestMethod.POST)
	public ResponseEntity<AdvocateClerkResponse> clerkV1CreatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details for the clerk registration + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateClerkRequest body) {

				List<AdvocateClerk> advocateList = advocateClerkService.registerAdvocateClerkRequest(body);
				ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
				AdvocateClerkResponse advocateClerkResponse = AdvocateClerkResponse.builder().clerks(advocateList).responseInfo(responseInfo).build();
				return new ResponseEntity<>(advocateClerkResponse, HttpStatus.OK);
	}

	@RequestMapping(value = "/clerk/v1/_search", method = RequestMethod.POST)
	public ResponseEntity<AdvocateClerkResponse> clerkV1SearchPost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateClerkSearchRequest body,
			@Min(0) @Max(1000) @ApiParam(value = "Pagination - limit records in response", required = false) @javax.validation.Valid @RequestParam(value = "limit", required = false) Integer limit,
			@Min(0) @ApiParam(value = "Pagination - offset from which records should be returned in response", required = false) @javax.validation.Valid @RequestParam(value = "offset", required = false) Integer offset) {

				List<AdvocateClerk> applications = advocateClerkService.searchAdvocateClerkApplications(body.getRequestInfo(), body.getCriteria(), body.getStatus(), body.getApplicationNumber(), limit, offset);
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
