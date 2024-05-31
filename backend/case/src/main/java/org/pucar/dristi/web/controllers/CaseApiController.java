package org.pucar.dristi.web.controllers;

import java.io.IOException;

import org.pucar.dristi.web.models.CaseExistsResponse;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CaseResponse;
import org.pucar.dristi.web.models.CaseSearchRequest;
import org.pucar.dristi.web.models.WitnessRequest;
import org.pucar.dristi.web.models.WitnessResponse;
import org.pucar.dristi.web.models.WitnessSearchRequest;
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

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
public class CaseApiController {

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
