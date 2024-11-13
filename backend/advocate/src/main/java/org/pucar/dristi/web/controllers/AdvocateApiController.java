package org.pucar.dristi.web.controllers;

import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.AdvocateService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;


@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-04T05:55:27.937918+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
public class AdvocateApiController {
	private final AdvocateService advocateService;
	private final ResponseInfoFactory responseInfoFactory;

	@Autowired
	public AdvocateApiController(AdvocateService advocateService,
								 ResponseInfoFactory responseInfoFactory) {
		this.advocateService = advocateService;
		this.responseInfoFactory = responseInfoFactory;
	}

	@PostMapping("/v1/_create")
	public ResponseEntity<AdvocateResponse> advocateV1CreatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details for the advocate registration + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateRequest body) {

		Advocate response = advocateService.createAdvocate(body);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
		AdvocateResponse advocateResponse = AdvocateResponse.builder().advocates(Collections.singletonList(response)).responseInfo(responseInfo).build();
		return new ResponseEntity<>(advocateResponse, HttpStatus.OK);
	}

	@PostMapping("/v1/_search")
	public ResponseEntity<AdvocateListResponse> advocateV1SearchPost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateSearchRequest body,
	 		@Min(0) @Max(1000) @ApiParam(value = "Pagination - limit records in response", required = false) @javax.validation.Valid @RequestParam(value = "limit", required = false) Integer limit,
			@Min(0) @ApiParam(value = "Pagination - offset from which records should be returned in response", required = false) @javax.validation.Valid @RequestParam(value = "offset", required = false) Integer offset) {

		advocateService.searchAdvocate(body.getRequestInfo(), body.getCriteria(), body.getTenantId(), limit, offset);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
		AdvocateListResponse advocateResponse = AdvocateListResponse.builder().advocates(body.getCriteria()).responseInfo(responseInfo).build();
		return new ResponseEntity<>(advocateResponse, HttpStatus.OK);
	}

	@PostMapping("/v1/status/_search")
	public ResponseEntity<AdvocateResponse> advocateV1StatusSearchPost(@NotNull @Parameter(in = ParameterIn.QUERY, description = "status of advocate registration being searched" ,required=true,schema=@Schema()) @javax.validation.Valid @RequestParam(value = "status", required = true) String status,
																	   @NotNull @Parameter(in = ParameterIn.QUERY, description = "Search by tenantId" ,required=true,schema=@Schema()) @javax.validation.Valid @RequestParam(value = "tenantId", required = true) String tenantId,
																	   @Min(0) @Max(1000) @ApiParam(value = "Pagination - limit records in response", required = false) @javax.validation.Valid @RequestParam(value = "limit", required = false) Integer limit,
																	   @Min(0) @ApiParam(value = "Pagination - offset from which records should be returned in response", required = false) @javax.validation.Valid @RequestParam(value = "offset", required = false) Integer offset,
																	   @Parameter(in = ParameterIn.DEFAULT, description = "RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateSimpleSearchRequest body) {

		List<Advocate> advocateList = advocateService.searchAdvocateByStatus(body.getRequestInfo(), status, tenantId,limit, offset);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(null, true);
		AdvocateResponse advocateResponse = AdvocateResponse.builder().advocates(advocateList).responseInfo(responseInfo).build();
		return new ResponseEntity<>(advocateResponse, HttpStatus.OK);
	}

	@PostMapping("/v1/applicationnumber/_search")
	public ResponseEntity<AdvocateResponse> advocateV1ApplicationnumberSearchPost(@NotNull @Parameter(in = ParameterIn.QUERY, description = "applicationNumber of advocate registration being searched" ,required=true,schema=@Schema()) @javax.validation.Valid @RequestParam(value = "applicationNumber", required = true) String applicationNumber,
																				  @NotNull @Parameter(in = ParameterIn.QUERY, description = "Search by tenantId" ,required=true,schema=@Schema()) @javax.validation.Valid @RequestParam(value = "tenantId", required = true) String tenantId,
																				  @Min(0) @Max(1000) @ApiParam(value = "Pagination - limit records in response", required = false) @javax.validation.Valid @RequestParam(value = "limit", required = false) Integer limit,
																				  @Min(0) @ApiParam(value = "Pagination - offset from which records should be returned in response", required = false) @javax.validation.Valid @RequestParam(value = "offset", required = false) Integer offset,
																				  @Parameter(in = ParameterIn.DEFAULT, description = "RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateSimpleSearchRequest body) {

		List<Advocate> advocateList = advocateService.searchAdvocateByApplicationNumber(body.getRequestInfo(), applicationNumber, tenantId,limit, offset);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(null, true);
		AdvocateResponse advocateResponse = AdvocateResponse.builder().advocates(advocateList).responseInfo(responseInfo).build();
		return new ResponseEntity<>(advocateResponse, HttpStatus.OK);
	}

	@PostMapping("/v1/_update")
	public ResponseEntity<AdvocateResponse> advocateV1UpdatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details of the registered advocate + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateRequest body) {

		Advocate advocateList = advocateService.updateAdvocate(body);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
		AdvocateResponse advocateResponse = AdvocateResponse.builder().advocates(Collections.singletonList(advocateList)).responseInfo(responseInfo).build();
		return new ResponseEntity<>(advocateResponse, HttpStatus.OK);
		}

}
