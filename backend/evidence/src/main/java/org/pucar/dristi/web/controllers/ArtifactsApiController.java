package org.pucar.dristi.web.controllers;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.EvidenceService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-16T15:17:16.225735+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
public class ArtifactsApiController {

    private EvidenceService evidenceService;
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    public ArtifactsApiController(EvidenceService evidenceService, ResponseInfoFactory responseInfoFactory) {
        this.evidenceService = evidenceService;
        this.responseInfoFactory = responseInfoFactory;
    }

    @RequestMapping(value = "/v1/_create", method = RequestMethod.POST)
    public ResponseEntity<EvidenceResponse> artifactsV1CreatePost(
            @Parameter(in = ParameterIn.DEFAULT, description = "Details for the artifact + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody EvidenceRequest body) {
        Artifact response = evidenceService.createEvidence(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        EvidenceResponse evidenceResponse = EvidenceResponse.builder().artifact(response).responseInfo(responseInfo).build();
        return new ResponseEntity<>(evidenceResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/_search", method = RequestMethod.POST)
	public ResponseEntity<EvidenceSearchResponse> artifactsV1SearchPost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody EvidenceSearchRequest body) {

		List<Artifact> artifactList = evidenceService.searchEvidence(body.getRequestInfo(), body.getCriteria(),body.getPagination());
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
		EvidenceSearchResponse artifactResponse = EvidenceSearchResponse.builder()
				.artifacts(artifactList)
				.responseInfo(responseInfo)
				.pagination(body.getPagination())
				.build();
		return new ResponseEntity<>(artifactResponse, HttpStatus.OK);
	}
  
	@RequestMapping(value = "/v1/_update", method = RequestMethod.POST)
	public ResponseEntity<EvidenceResponse> artifactsV1UpdatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details for the artifact to be updated + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody EvidenceRequest body) {
				Artifact response = evidenceService.updateEvidence(body);
				ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
				EvidenceResponse evidenceResponse = EvidenceResponse.builder().artifact(response).responseInfo(responseInfo).build();
				return new ResponseEntity<>(evidenceResponse, HttpStatus.OK);
	}

}
