package org.pucar.web.controllers;

import java.io.IOException;

import org.pucar.web.models.AdvocateRequest;
import org.pucar.web.models.AdvocateResponse;
import org.pucar.web.models.AdvocateSearchRequest;
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
public class AdvocateApiController {

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	public AdvocateApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@RequestMapping(value = "/advocate/v1/_create", method = RequestMethod.POST)
	public ResponseEntity<AdvocateResponse> advocateV1CreatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details for the user registration + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateRequest body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				// Example after implementing a service layer
				//AdvocateResponse response = advocateService.createAdvocate(body);
				AdvocateResponse response = new AdvocateResponse();
				return new ResponseEntity<>(response, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<AdvocateResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<AdvocateResponse>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/advocate/v1/_search", method = RequestMethod.POST)
	public ResponseEntity<AdvocateResponse> advocateV1SearchPost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateSearchRequest body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				// Example after implementing a service layer
				//AdvocateResponse response = advocateService.searchAdvocates(body);
				AdvocateResponse response = new AdvocateResponse();
				return new ResponseEntity<>(response, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<AdvocateResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<AdvocateResponse>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/advocate/v1/_update", method = RequestMethod.POST)
	public ResponseEntity<AdvocateResponse> advocateV1UpdatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details of the registered advocate + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody AdvocateRequest body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				// Example after implementing a service layer
				//AdvocateResponse response = advocateService.updateAdvocate(body);
				AdvocateResponse response = new AdvocateResponse();
				return new ResponseEntity<>(response, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<AdvocateResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<AdvocateResponse>(HttpStatus.NOT_IMPLEMENTED);
	}

}
