package org.pucar.dristi.web.controllers;

import java.io.IOException;

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

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-16T15:17:16.225735+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
public class ArtifactsApiController {

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	public ArtifactsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@RequestMapping(value = "/artifacts/v1/_create", method = RequestMethod.POST)
	public ResponseEntity<EvidenceResponse> artifactsV1CreatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details for the artifact + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody EvidenceRequest body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<EvidenceResponse>(objectMapper.readValue(
						"{  \"pagination\" : {    \"offSet\" : 2.3021358869347655,    \"limit\" : 56.37376656633328,    \"sortBy\" : \"sortBy\",    \"totalCount\" : 7.061401241503109,    \"order\" : \"\"  },  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"artifacts\" : [ {    \"sourceID\" : \"sourceID\",    \"evidenceNumber\" : \"evidenceNumber\",    \"artifactType\" : \"AFFIDAVIT, DEPOSITION, SWORN_STATEMENT, OTHER etc..\",    \"comments\" : [ {      \"tenantId\" : \"tenantId\",      \"artifactId\" : \"artifactId\",      \"comment\" : \"comment\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"individualId\" : \"individualId\",      \"isActive\" : true,      \"additionalDetails\" : \"additionalDetails\",      \"auditdetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 1      }    }, {      \"tenantId\" : \"tenantId\",      \"artifactId\" : \"artifactId\",      \"comment\" : \"comment\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"individualId\" : \"individualId\",      \"isActive\" : true,      \"additionalDetails\" : \"additionalDetails\",      \"auditdetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 1      }    } ],    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"description\" : \"description\",    \"mediaType\" : \"AUDIO, VIDEO, DOC\",    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"externalRefNumber\" : \"externalRefNumber\",    \"createdDate\" : 6,    \"file\" : {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    },    \"application\" : \"application\",    \"artifactDetails\" : { },    \"caseId\" : \"caseId\",    \"tenantId\" : \"tenantId\",    \"hearing\" : \"hearing\",    \"applicableTo\" : [ \"applicableTo\", \"applicableTo\" ],    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"sourceName\" : \"Mr. Sundar, Assistant DGP\",    \"artifactNumber\" : \"artifactNumber\",    \"order\" : \"order\",    \"status\" : \"status\"  }, {    \"sourceID\" : \"sourceID\",    \"evidenceNumber\" : \"evidenceNumber\",    \"artifactType\" : \"AFFIDAVIT, DEPOSITION, SWORN_STATEMENT, OTHER etc..\",    \"comments\" : [ {      \"tenantId\" : \"tenantId\",      \"artifactId\" : \"artifactId\",      \"comment\" : \"comment\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"individualId\" : \"individualId\",      \"isActive\" : true,      \"additionalDetails\" : \"additionalDetails\",      \"auditdetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 1      }    }, {      \"tenantId\" : \"tenantId\",      \"artifactId\" : \"artifactId\",      \"comment\" : \"comment\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"individualId\" : \"individualId\",      \"isActive\" : true,      \"additionalDetails\" : \"additionalDetails\",      \"auditdetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 1      }    } ],    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"description\" : \"description\",    \"mediaType\" : \"AUDIO, VIDEO, DOC\",    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"externalRefNumber\" : \"externalRefNumber\",    \"createdDate\" : 6,    \"file\" : {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    },    \"application\" : \"application\",    \"artifactDetails\" : { },    \"caseId\" : \"caseId\",    \"tenantId\" : \"tenantId\",    \"hearing\" : \"hearing\",    \"applicableTo\" : [ \"applicableTo\", \"applicableTo\" ],    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"sourceName\" : \"Mr. Sundar, Assistant DGP\",    \"artifactNumber\" : \"artifactNumber\",    \"order\" : \"order\",    \"status\" : \"status\"  } ]}",
						EvidenceResponse.class), HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				return new ResponseEntity<EvidenceResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<EvidenceResponse>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/artifacts/v1/_update", method = RequestMethod.POST)
	public ResponseEntity<EvidenceResponse> artifactsV1UpdatePost(
			@Parameter(in = ParameterIn.DEFAULT, description = "Details for the artifact to be updated + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody EvidenceRequest body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<EvidenceResponse>(objectMapper.readValue(
						"{  \"pagination\" : {    \"offSet\" : 2.3021358869347655,    \"limit\" : 56.37376656633328,    \"sortBy\" : \"sortBy\",    \"totalCount\" : 7.061401241503109,    \"order\" : \"\"  },  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"artifacts\" : [ {    \"sourceID\" : \"sourceID\",    \"evidenceNumber\" : \"evidenceNumber\",    \"artifactType\" : \"AFFIDAVIT, DEPOSITION, SWORN_STATEMENT, OTHER etc..\",    \"comments\" : [ {      \"tenantId\" : \"tenantId\",      \"artifactId\" : \"artifactId\",      \"comment\" : \"comment\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"individualId\" : \"individualId\",      \"isActive\" : true,      \"additionalDetails\" : \"additionalDetails\",      \"auditdetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 1      }    }, {      \"tenantId\" : \"tenantId\",      \"artifactId\" : \"artifactId\",      \"comment\" : \"comment\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"individualId\" : \"individualId\",      \"isActive\" : true,      \"additionalDetails\" : \"additionalDetails\",      \"auditdetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 1      }    } ],    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"description\" : \"description\",    \"mediaType\" : \"AUDIO, VIDEO, DOC\",    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"externalRefNumber\" : \"externalRefNumber\",    \"createdDate\" : 6,    \"file\" : {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    },    \"application\" : \"application\",    \"artifactDetails\" : { },    \"caseId\" : \"caseId\",    \"tenantId\" : \"tenantId\",    \"hearing\" : \"hearing\",    \"applicableTo\" : [ \"applicableTo\", \"applicableTo\" ],    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"sourceName\" : \"Mr. Sundar, Assistant DGP\",    \"artifactNumber\" : \"artifactNumber\",    \"order\" : \"order\",    \"status\" : \"status\"  }, {    \"sourceID\" : \"sourceID\",    \"evidenceNumber\" : \"evidenceNumber\",    \"artifactType\" : \"AFFIDAVIT, DEPOSITION, SWORN_STATEMENT, OTHER etc..\",    \"comments\" : [ {      \"tenantId\" : \"tenantId\",      \"artifactId\" : \"artifactId\",      \"comment\" : \"comment\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"individualId\" : \"individualId\",      \"isActive\" : true,      \"additionalDetails\" : \"additionalDetails\",      \"auditdetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 1      }    }, {      \"tenantId\" : \"tenantId\",      \"artifactId\" : \"artifactId\",      \"comment\" : \"comment\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"individualId\" : \"individualId\",      \"isActive\" : true,      \"additionalDetails\" : \"additionalDetails\",      \"auditdetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 1      }    } ],    \"workflow\" : {      \"action\" : \"action\",      \"assignees\" : [ \"assignees\", \"assignees\" ],      \"comment\" : \"comment\"    },    \"description\" : \"description\",    \"mediaType\" : \"AUDIO, VIDEO, DOC\",    \"isActive\" : true,    \"additionalDetails\" : \"additionalDetails\",    \"externalRefNumber\" : \"externalRefNumber\",    \"createdDate\" : 6,    \"file\" : {      \"documentType\" : \"documentType\",      \"documentUid\" : \"documentUid\",      \"fileStore\" : \"fileStore\",      \"id\" : \"id\",      \"additionalDetails\" : { }    },    \"application\" : \"application\",    \"artifactDetails\" : { },    \"caseId\" : \"caseId\",    \"tenantId\" : \"tenantId\",    \"hearing\" : \"hearing\",    \"applicableTo\" : [ \"applicableTo\", \"applicableTo\" ],    \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"sourceName\" : \"Mr. Sundar, Assistant DGP\",    \"artifactNumber\" : \"artifactNumber\",    \"order\" : \"order\",    \"status\" : \"status\"  } ]}",
						EvidenceResponse.class), HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				return new ResponseEntity<EvidenceResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<EvidenceResponse>(HttpStatus.NOT_IMPLEMENTED);
	}

}
