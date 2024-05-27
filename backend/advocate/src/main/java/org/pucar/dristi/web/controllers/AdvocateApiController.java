package org.pucar.dristi.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.AdvocateService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.Advocate;
import org.pucar.dristi.web.models.AdvocateRequest;
import org.pucar.dristi.web.models.AdvocateResponse;
import org.pucar.dristi.web.models.AdvocateSearchRequest;
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
public class AdvocateApiController {

	@Autowired
	private AdvocateService advocateService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	public AdvocateApiController(ObjectMapper objectMapper, HttpServletRequest request) {
	}

	public void setMockInjects(AdvocateService advocateService, ResponseInfoFactory responseInfoFactory){
		this.advocateService = advocateService;
		this.responseInfoFactory = responseInfoFactory;
	}

	@RequestMapping(value = "/advocate/v1/_create", method = RequestMethod.POST)
	public ResponseEntity<AdvocateResponse> advocateV1CreatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details for the advocate registration + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateRequest body) {

		List<Advocate> response = advocateService.createAdvocate(body);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
		AdvocateResponse advocateResponse = AdvocateResponse.builder().advocates(response).responseInfo(responseInfo).build();
		return new ResponseEntity<>(advocateResponse, HttpStatus.OK);
	}

	@RequestMapping(value = "/advocate/v1/_search", method = RequestMethod.POST)
	public ResponseEntity<AdvocateResponse> advocateV1SearchPost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateSearchRequest body,
	 		@Min(0) @Max(1000) @ApiParam(value = "Pagination - limit records in response", required = false) @javax.validation.Valid @RequestParam(value = "limit", required = false) Integer limit,
			@Min(0) @ApiParam(value = "Pagination - offset from which records should be returned in response", required = false) @javax.validation.Valid @RequestParam(value = "offset", required = false) Integer offset) {

		List<Advocate> advocateList = advocateService.searchAdvocate(body.getRequestInfo(), body.getCriteria(), body.getStatus(), body.getApplicationNumber() ,limit, offset);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
		AdvocateResponse advocateResponse = AdvocateResponse.builder().advocates(advocateList).responseInfo(responseInfo).build();
		return new ResponseEntity<>(advocateResponse, HttpStatus.OK);
	}

	@RequestMapping(value = "/advocate/v1/_update", method = RequestMethod.POST)
	public ResponseEntity<AdvocateResponse> advocateV1UpdatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details of the registered advocate + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateRequest body) {

		List<Advocate> advocateList = advocateService.updateAdvocate(body);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
		AdvocateResponse advocateResponse = AdvocateResponse.builder().advocates(advocateList).responseInfo(responseInfo).build();
		return new ResponseEntity<>(advocateResponse, HttpStatus.OK);
		}

}
