package org.pucar.dristi.web.controllers;

<<<<<<< HEAD
import java.util.Collections;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.CaseService;
import org.pucar.dristi.service.WitnessService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;
=======
import java.io.IOException;

import org.pucar.dristi.web.models.CaseExistsResponse;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CaseResponse;
import org.pucar.dristi.web.models.CaseSearchRequest;
import org.pucar.dristi.web.models.WitnessRequest;
import org.pucar.dristi.web.models.WitnessResponse;
import org.pucar.dristi.web.models.WitnessSearchRequest;
>>>>>>> main
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
=======
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
>>>>>>> main

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
<<<<<<< HEAD
=======
import jakarta.servlet.http.HttpServletRequest;
>>>>>>> main
import jakarta.validation.Valid;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
public class CaseApiController {

<<<<<<< HEAD
    private CaseService caseService;

    private WitnessService witnessService;

    private ResponseInfoFactory responseInfoFactory;


    @Autowired
    public CaseApiController(CaseService caseService, WitnessService witnessService, ResponseInfoFactory responseInfoFactory) {
        this.caseService = caseService;
        this.witnessService = witnessService;
        this.responseInfoFactory = responseInfoFactory;
    }

    @PostMapping(value = "/v1/_create")
    public ResponseEntity<CaseResponse> caseV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new court case + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CaseRequest body) {

        CourtCase cases = caseService.createCase(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        CaseResponse caseResponse = CaseResponse.builder().cases(Collections.singletonList(cases)).responseInfo(responseInfo).build();
        return new ResponseEntity<>(caseResponse, HttpStatus.OK);
        }

    @PostMapping(value = "/v1/_exists")
    public ResponseEntity<CaseExistsResponse> caseV1ExistsPost (
            @Parameter(in = ParameterIn.DEFAULT, description = "Case search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CaseExistsRequest body){

        List<CaseExists> caseExists = caseService.existCases(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        CaseExistsResponse caseExistsResponse = CaseExistsResponse.builder().criteria(caseExists).responseInfo(responseInfo).build();
        return new ResponseEntity<>(caseExistsResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/v1/_search")
    public ResponseEntity<CaseListResponse> caseV1SearchPost (
            @Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CaseSearchRequest body){

        caseService.searchCases(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        CaseListResponse caseResponse = CaseListResponse.builder().criteria(body.getCriteria()).responseInfo(responseInfo).build();
        return new ResponseEntity<>(caseResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/v1/_verify")
    public ResponseEntity<JoinCaseResponse> verifyV1JoinCase (
            @Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody JoinCaseRequest body){

        JoinCaseResponse joinCaseResponse = caseService.verifyJoinCaseRequest(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        joinCaseResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(joinCaseResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/v1/_update")
    public ResponseEntity<CaseResponse> caseV1UpdatePost (
            @Parameter(in = ParameterIn.DEFAULT, description = "Details for updating all editable fields in the court case + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CaseRequest body){

        CourtCase cases = caseService.updateCase(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        CaseResponse caseResponse = CaseResponse.builder().cases(Collections.singletonList(cases)).responseInfo(responseInfo).build();
        return new ResponseEntity<>(caseResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/v1/add/witness")
    public ResponseEntity<AddWitnessResponse> caseV1AddWitnessPost (
            @Parameter(in = ParameterIn.DEFAULT, description = "Details for adding witness details in the court case + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AddWitnessRequest body){
        AddWitnessResponse addWitnessResponse = caseService.addWitness(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        addWitnessResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(addWitnessResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/witness/v1/_create")
    public ResponseEntity<WitnessResponse> caseWitnessV1CreatePost (
            @Parameter(in = ParameterIn.DEFAULT, description = "Details for the witness + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody WitnessRequest body){

        Witness witness = witnessService.registerWitnessRequest(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        WitnessResponse witnessResponse = WitnessResponse.builder().witnesses(Collections.singletonList(witness)).requestInfo(responseInfo).build();
        return new ResponseEntity<>(witnessResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/witness/v1/_search")
    public ResponseEntity<WitnessResponse> caseWitnessV1SearchPost (
            @Parameter(in = ParameterIn.DEFAULT, description = "Details for the witness + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody WitnessSearchRequest body){

        List<Witness> witnessList = witnessService.searchWitnesses(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        WitnessResponse witnessResponse = WitnessResponse.builder().witnesses(witnessList).requestInfo(responseInfo).build();
        return new ResponseEntity<>(witnessResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/witness/v1/_update")
    public ResponseEntity<WitnessResponse> caseWitnessV1UpdatePost (
            @Parameter(in = ParameterIn.DEFAULT, description = "Details for the witness + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody WitnessRequest body){

        Witness witness= witnessService.updateWitness(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        WitnessResponse witnessResponse = WitnessResponse.builder().witnesses(Collections.singletonList(witness)).requestInfo(responseInfo).build();
        return new ResponseEntity<>(witnessResponse, HttpStatus.OK);
    }

    }


=======
	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	public CaseApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@RequestMapping(value = "/case/v1/_create", method = RequestMethod.POST)
	public ResponseEntity<CaseResponse> caseV1CreatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new court case + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CaseRequest body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<CaseResponse>(HttpStatus.NOT_IMPLEMENTED);
			} catch (Exception e) {
				return new ResponseEntity<CaseResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<CaseResponse>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/case/v1/_exists", method = RequestMethod.POST)
	public ResponseEntity<CaseExistsResponse> caseV1ExistsPost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Case search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CaseSearchRequest body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<CaseExistsResponse>(HttpStatus.NOT_IMPLEMENTED);
			} catch (Exception e) {
				return new ResponseEntity<CaseExistsResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<CaseExistsResponse>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/case/v1/_search", method = RequestMethod.POST)
	public ResponseEntity<CaseResponse> caseV1SearchPost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CaseSearchRequest body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<CaseResponse>(HttpStatus.NOT_IMPLEMENTED);
			} catch (Exception e) {
				return new ResponseEntity<CaseResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<CaseResponse>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/case/v1/_update", method = RequestMethod.POST)
	public ResponseEntity<CaseResponse> caseV1UpdatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details for updating all editable fields in the court case + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CaseRequest body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<CaseResponse>(HttpStatus.NOT_IMPLEMENTED);
			} catch (Exception e) {
				return new ResponseEntity<CaseResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<CaseResponse>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/case/witness/v1/_create", method = RequestMethod.POST)
	public ResponseEntity<WitnessResponse> caseWitnessV1CreatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details for the witness + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody WitnessRequest body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<WitnessResponse>(HttpStatus.NOT_IMPLEMENTED);
			} catch (Exception e) {
				return new ResponseEntity<WitnessResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<WitnessResponse>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/case/witness/v1/_search", method = RequestMethod.POST)
	public ResponseEntity<WitnessResponse> caseWitnessV1SearchPost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details for the witness + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody WitnessSearchRequest body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<WitnessResponse>(HttpStatus.NOT_IMPLEMENTED);
			} catch (Exception e) {
				return new ResponseEntity<WitnessResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<WitnessResponse>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/case/witness/v1/_update", method = RequestMethod.POST)
	public ResponseEntity<WitnessResponse> caseWitnessV1UpdatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details for the witness + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody WitnessRequest body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<WitnessResponse>(HttpStatus.NOT_IMPLEMENTED);
			} catch (Exception e) {
				return new ResponseEntity<WitnessResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<WitnessResponse>(HttpStatus.NOT_IMPLEMENTED);
	}

}
>>>>>>> main
