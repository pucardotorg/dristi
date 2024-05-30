package org.pucar.dristi.web.controllers;

import java.io.IOException;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
	import org.pucar.dristi.service.EvidenceService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.Artifact;
import org.pucar.dristi.web.models.EvidenceListResponse;
import org.pucar.dristi.web.models.EvidenceRequest;
import org.pucar.dristi.web.models.EvidenceResponse;
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
import org.springframework.web.bind.annotation.RequestParam;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-16T15:17:16.225735+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
public class ArtifactsApiController {

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;
	@Autowired
	private EvidenceService evidenceService;
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	@Autowired
	public ArtifactsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@RequestMapping(value = "/artifacts/v1/_create", method = RequestMethod.POST)
	public ResponseEntity<EvidenceResponse> artifactsV1CreatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details for the artifact + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody EvidenceRequest body) {
		Artifact response = evidenceService.createEvidence(body);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
		EvidenceResponse evidenceResponse = EvidenceResponse.builder().artifact(response).responseInfo(responseInfo).build();
		return new ResponseEntity<>(evidenceResponse, HttpStatus.OK);
	}
	@RequestMapping(value = "/artifacts/v1/search", method = RequestMethod.POST)
	public ResponseEntity<EvidenceListResponse> artifactsV1SearchPost(
			@Parameter(in = ParameterIn.QUERY, description = "ID of the order being searched", schema = @Schema())
			@Valid @RequestParam(value = "id", required = false) String id,
			@Parameter(in = ParameterIn.QUERY, description = "Tenant ID whose orders are being searched", schema = @Schema())
			@Valid @RequestParam(value = "tenantId", required = false) String tenantId,
			@Parameter(in = ParameterIn.QUERY, description = "Case ID whose orders are being searched", schema = @Schema())
			@Valid @RequestParam(value = "caseId", required = false) String caseId,
			@Parameter(in = ParameterIn.QUERY, description = "The application whose orders are being searched", schema = @Schema())
			@Valid @RequestParam(value = "application", required = false) String application,
			@Parameter(in = ParameterIn.QUERY, description = "The hearing whose orders are being searched", schema = @Schema())
			@Valid @RequestParam(value = "hearing", required = false) String hearing,
			@Parameter(in = ParameterIn.QUERY, description = "The order being searched", schema = @Schema())
			@Valid @RequestParam(value = "order", required = false) String order,
			@Parameter(in = ParameterIn.QUERY, description = "The source ID whose orders are being searched", schema = @Schema())
			@Valid @RequestParam(value = "sourceId", required = false) String sourceId,
			@Parameter(in = ParameterIn.QUERY, description = "The source name whose orders are being searched", schema = @Schema())
			@Valid @RequestParam(value = "sourceName", required = false) String sourceName,
			@Parameter(in = ParameterIn.DEFAULT, description = "RequestInfo meta data.", schema = @Schema())
			@Valid @RequestBody RequestInfo requestInfo) {
		try {
			List<Artifact> artifacts = evidenceService.searchEvidence(id,tenantId, caseId, application, hearing, order, sourceId, sourceName,requestInfo);
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			EvidenceListResponse orderListResponse = EvidenceListResponse.builder().list(artifacts).totalCount(artifacts.size()).responseInfo(responseInfo).build();
			return new ResponseEntity<>(orderListResponse, HttpStatus.OK);
		} catch (Exception e) {
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);
			EvidenceListResponse orderListResponse = EvidenceListResponse.builder().list(null).responseInfo(responseInfo).build();
			return new ResponseEntity<>(orderListResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/artifacts/v1/_update", method = RequestMethod.POST)
	public ResponseEntity<EvidenceResponse> artifactsV1UpdatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details for the artifact to be updated + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody EvidenceRequest body) {
				Artifact response = evidenceService.updateEvidence(body);
				ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
				EvidenceResponse evidenceResponse = EvidenceResponse.builder().artifact(response).responseInfo(responseInfo).build();
				return new ResponseEntity<>(evidenceResponse, HttpStatus.OK);
	}

}
