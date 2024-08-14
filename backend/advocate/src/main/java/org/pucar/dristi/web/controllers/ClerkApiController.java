package org.pucar.dristi.web.controllers;

import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.AdvocateClerkService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-04T05:55:27.937918+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
public class ClerkApiController {

	private AdvocateClerkService advocateClerkService;
	private ResponseInfoFactory responseInfoFactory;
	@Autowired
	public ClerkApiController(AdvocateClerkService advocateClerkService, ResponseInfoFactory responseInfoFactory) {
		this.advocateClerkService = advocateClerkService;
		this.responseInfoFactory = responseInfoFactory;
	}

	@PostMapping("/clerk/v1/_create")
	public ResponseEntity<AdvocateClerkResponse> clerkV1CreatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details for the clerk registration + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateClerkRequest body) {

				AdvocateClerk advocateList = advocateClerkService.registerAdvocateClerkRequest(body);
				ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
				AdvocateClerkResponse advocateClerkResponse = AdvocateClerkResponse.builder().clerks(Collections.singletonList(advocateList)).responseInfo(responseInfo).build();
				return new ResponseEntity<>(advocateClerkResponse, HttpStatus.OK);
	}

	@PostMapping("/clerk/v1/_search")
	public ResponseEntity<AdvocateClerkListResponse> clerkV1SearchPost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateClerkSearchRequest body,
			@Min(0) @Max(1000) @ApiParam(value = "Pagination - limit records in response", required = false) @javax.validation.Valid @RequestParam(value = "limit", required = false) Integer limit,
			@Min(0) @ApiParam(value = "Pagination - offset from which records should be returned in response", required = false) @javax.validation.Valid @RequestParam(value = "offset", required = false) Integer offset) {

				advocateClerkService.searchAdvocateClerkApplications(body.getRequestInfo(), body.getCriteria(), body.getTenantId(), limit, offset);
				ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
				AdvocateClerkListResponse response = AdvocateClerkListResponse.builder().clerks(body.getCriteria()).responseInfo(responseInfo).build();
				return new ResponseEntity<>(response,HttpStatus.OK);
	}

	@PostMapping("/clerk/v1/status/_search")
	public ResponseEntity<AdvocateClerkResponse> clerkV1StatusSearchPost(@NotNull @Parameter(in = ParameterIn.QUERY, description = "status of clerks registration being searched" ,
			required=true,schema=@Schema()) @javax.validation.Valid @RequestParam(value = "status", required = true) String status,
			@NotNull @Parameter(in = ParameterIn.QUERY, description = "Search by tenantId" ,required=true,schema=@Schema()) @javax.validation.Valid @RequestParam(value = "tenantId", required = true) String tenantId,
			@Min(0) @Max(1000) @ApiParam(value = "Pagination - limit records in response", required = false) @javax.validation.Valid @RequestParam(value = "limit", required = false) Integer limit,
			@Min(0) @ApiParam(value = "Pagination - offset from which records should be returned in response", required = false) @javax.validation.Valid @RequestParam(value = "offset", required = false) Integer offset,
			@Parameter(in = ParameterIn.DEFAULT, description = "RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateSimpleSearchRequest body) {

		List<AdvocateClerk> applications = advocateClerkService.searchAdvocateClerkApplicationsByStatus(body.getRequestInfo(), status, tenantId, limit, offset);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(null, true);
		AdvocateClerkResponse response = AdvocateClerkResponse.builder().clerks(applications).responseInfo(responseInfo).build();
		return new ResponseEntity<>(response,HttpStatus.OK);

	}

	@PostMapping("/clerk/v1/applicationnumber/_search")
	public ResponseEntity<AdvocateClerkResponse> clerkV1ApplicationnumberSearchPost(@NotNull @Parameter(in = ParameterIn.QUERY, description = "applicationNumber of clerks registration being searched" ,required=true,schema=@Schema()) @javax.validation.Valid @RequestParam(value = "applicationNumber", required = true) String applicationNumber,
		@NotNull @Parameter(in = ParameterIn.QUERY, description = "Search by tenantId" ,required=true,schema=@Schema()) @javax.validation.Valid @RequestParam(value = "tenantId", required = true) String tenantId,
		@Min(0) @Max(1000) @ApiParam(value = "Pagination - limit records in response", required = false) @javax.validation.Valid @RequestParam(value = "limit", required = false) Integer limit,
		@Min(0) @ApiParam(value = "Pagination - offset from which records should be returned in response", required = false) @javax.validation.Valid @RequestParam(value = "offset", required = false) Integer offset,
		@Parameter(in = ParameterIn.DEFAULT, description = "RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateSimpleSearchRequest body) {

		List<AdvocateClerk> applications = advocateClerkService.searchAdvocateClerkApplicationsByAppNumber(body.getRequestInfo(), applicationNumber, tenantId, limit, offset);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(null, true);
		AdvocateClerkResponse response = AdvocateClerkResponse.builder().clerks(applications).responseInfo(responseInfo).build();
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	@PostMapping("/clerk/v1/_update")
	public ResponseEntity<AdvocateClerkResponse> clerkV1UpdatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details of the registered advocate + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateClerkRequest body) {

				AdvocateClerk advocateClerkList = advocateClerkService.updateAdvocateClerk(body);
				ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
				AdvocateClerkResponse advocateClerkResponse = AdvocateClerkResponse.builder().clerks(Collections.singletonList(advocateClerkList)).responseInfo(responseInfo).build();
				return new ResponseEntity<>(advocateClerkResponse, HttpStatus.OK);
	}

}
