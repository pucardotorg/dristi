package org.pucar.dristi.web.controllers;


import java.io.IOException;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.CaseBundleService;
import org.pucar.dristi.service.CaseManagerService;
import org.pucar.dristi.service.ServiceUrlMapperVCService;
import org.pucar.dristi.service.ServiceUrlMappingPdfService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-17T10:19:47.222225+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
public class CasemanagerApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;


    private final ServiceUrlMappingPdfService serviceUrlMappingPdfService;


    private final ServiceUrlMapperVCService serviceUrlMapperVCService;

    private final CaseManagerService caseManagerService;

    private  final CaseBundleService caseBundleService;

    private final ResponseInfoFactory responseInfoFactory;

    @Autowired
    public CasemanagerApiController(CaseBundleService caseBundleService,ObjectMapper objectMapper, HttpServletRequest request, ServiceUrlMappingPdfService serviceUrlMappingPdfService, ServiceUrlMapperVCService serviceUrlMapperVCService, CaseManagerService caseManagerService, ResponseInfoFactory responseInfoFactory) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.serviceUrlMapperVCService = serviceUrlMapperVCService;
        this.serviceUrlMappingPdfService = serviceUrlMappingPdfService;
        this.caseManagerService = caseManagerService;
        this.responseInfoFactory = responseInfoFactory;
        this.caseBundleService = caseBundleService;
    }

    @RequestMapping(value = "/casemanager/case/v1/_group", method = RequestMethod.POST)
    public ResponseEntity<CaseGroupResponse> casemanagerCaseV1GroupPost(@Parameter(in = ParameterIn.DEFAULT, description = "Details of the court cases to be grouped together + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CaseGroupRequest body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<CaseGroupResponse>(objectMapper.readValue("{  \"caseGroups\" : [ {    \"caseIds\" : [ \"caseIds\", \"caseIds\" ],    \"id\" : \"id\"  }, {    \"caseIds\" : [ \"caseIds\", \"caseIds\" ],    \"id\" : \"id\"  } ],  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", CaseGroupResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                return new ResponseEntity<CaseGroupResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<CaseGroupResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/casemanager/case/v1/_history", method = RequestMethod.POST)
    public ResponseEntity<CaseFileResponse> casemanagerCaseV1HistoryPost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for updating all updatable fields in the court case + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CaseRequest body) {
        List<CaseFile> caseFiles = caseManagerService.getCaseFiles(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        CaseFileResponse caseFileResponse = CaseFileResponse.builder().caseFiles(caseFiles).responseInfo(responseInfo).build();
        return new ResponseEntity<>(caseFileResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/casemanager/case/v1/_summary", method = RequestMethod.POST)
    public ResponseEntity<CaseSummaryResponse> casemanagerCaseV1SummaryPost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new court case + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CaseRequest body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<CaseSummaryResponse>(objectMapper.readValue("{  \"criteria\" : [ {    \"filingNumber\" : \"filingNumber\",    \"judgement\" : {      \"filingNumber\" : \"filingNumber\",      \"orderType\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],      \"hearingNumber\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"orderNumber\" : \"orderNumber\",      \"comments\" : \"comments\",      \"workflow\" : {        \"action\" : \"action\",        \"assignees\" : [ \"assignees\", \"assignees\" ],        \"comment\" : \"comment\"      },      \"applicationNumber\" : [ \"applicationNumber\", \"applicationNumber\" ],      \"documents\" : [ {        \"documentType\" : \"documentType\",        \"documentUid\" : \"documentUid\",        \"fileStore\" : \"fileStore\",        \"id\" : \"id\",        \"additionalDetails\" : { }      }, {        \"documentType\" : \"documentType\",        \"documentUid\" : \"documentUid\",        \"fileStore\" : \"fileStore\",        \"id\" : \"id\",        \"additionalDetails\" : { }      } ],      \"issuedBy\" : {        \"benchID\" : \"benchID\",        \"judgeID\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],        \"courtID\" : \"courtID\"      },      \"cnrNumber\" : \"cnrNumber\",      \"isActive\" : true,      \"additionalDetails\" : \"additionalDetails\",      \"createdDate\" : \"2000-01-23\",      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"orderCategory\" : \"Intermediate, Judgement\",      \"status\" : \"status\"    },    \"filingDate\" : \"2000-01-23\",    \"caseTitle\" : \"caseTitle\",    \"cnrNumber\" : \"PBJL01-123556-2024\",    \"statutesAndSections\" : [ {      \"subsections\" : [ \"subsections\", \"subsections\" ],      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"additionalDetails\" : \"additionalDetails\",      \"statute\" : \"statute\",      \"sections\" : [ \"sections\", \"sections\" ],      \"auditdetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 1      }    }, {      \"subsections\" : [ \"subsections\", \"subsections\" ],      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"additionalDetails\" : \"additionalDetails\",      \"statute\" : \"statute\",      \"sections\" : [ \"sections\", \"sections\" ],      \"auditdetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 1      }    } ],    \"resolutionMechanism\" : \"COURT, ADR.MEDIATION, ADR.CONCILIATION, ODR.ARBITRATION, Lok Adalat\",    \"caseDescription\" : \"caseDescription\",    \"registrationDate\" : \"registrationDate\",    \"caseDetails\" : { },    \"caseCategory\" : \"CIVIL, CRIMINAL\",    \"remarks\" : \"remarks\",    \"courCaseNumber\" : \"CC/023443/2024\",    \"status\" : \"status\"  }, {    \"filingNumber\" : \"filingNumber\",    \"judgement\" : {      \"filingNumber\" : \"filingNumber\",      \"orderType\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],      \"hearingNumber\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"orderNumber\" : \"orderNumber\",      \"comments\" : \"comments\",      \"workflow\" : {        \"action\" : \"action\",        \"assignees\" : [ \"assignees\", \"assignees\" ],        \"comment\" : \"comment\"      },      \"applicationNumber\" : [ \"applicationNumber\", \"applicationNumber\" ],      \"documents\" : [ {        \"documentType\" : \"documentType\",        \"documentUid\" : \"documentUid\",        \"fileStore\" : \"fileStore\",        \"id\" : \"id\",        \"additionalDetails\" : { }      }, {        \"documentType\" : \"documentType\",        \"documentUid\" : \"documentUid\",        \"fileStore\" : \"fileStore\",        \"id\" : \"id\",        \"additionalDetails\" : { }      } ],      \"issuedBy\" : {        \"benchID\" : \"benchID\",        \"judgeID\" : [ \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\" ],        \"courtID\" : \"courtID\"      },      \"cnrNumber\" : \"cnrNumber\",      \"isActive\" : true,      \"additionalDetails\" : \"additionalDetails\",      \"createdDate\" : \"2000-01-23\",      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"orderCategory\" : \"Intermediate, Judgement\",      \"status\" : \"status\"    },    \"filingDate\" : \"2000-01-23\",    \"caseTitle\" : \"caseTitle\",    \"cnrNumber\" : \"PBJL01-123556-2024\",    \"statutesAndSections\" : [ {      \"subsections\" : [ \"subsections\", \"subsections\" ],      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"additionalDetails\" : \"additionalDetails\",      \"statute\" : \"statute\",      \"sections\" : [ \"sections\", \"sections\" ],      \"auditdetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 1      }    }, {      \"subsections\" : [ \"subsections\", \"subsections\" ],      \"tenantId\" : \"tenantId\",      \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",      \"additionalDetails\" : \"additionalDetails\",      \"statute\" : \"statute\",      \"sections\" : [ \"sections\", \"sections\" ],      \"auditdetails\" : {        \"lastModifiedTime\" : 5,        \"createdBy\" : \"createdBy\",        \"lastModifiedBy\" : \"lastModifiedBy\",        \"createdTime\" : 1      }    } ],    \"resolutionMechanism\" : \"COURT, ADR.MEDIATION, ADR.CONCILIATION, ODR.ARBITRATION, Lok Adalat\",    \"caseDescription\" : \"caseDescription\",    \"registrationDate\" : \"registrationDate\",    \"caseDetails\" : { },    \"caseCategory\" : \"CIVIL, CRIMINAL\",    \"remarks\" : \"remarks\",    \"courCaseNumber\" : \"CC/023443/2024\",    \"status\" : \"status\"  } ],  \"requestInfo\" : {    \"userInfo\" : {      \"mobileNumber\" : \"mobileNumber\",      \"roles\" : [ {        \"tenantId\" : \"tenantId\",        \"name\" : \"name\",        \"description\" : \"description\",        \"id\" : \"id\"      }, {        \"tenantId\" : \"tenantId\",        \"name\" : \"name\",        \"description\" : \"description\",        \"id\" : \"id\"      } ],      \"tenantId\" : \"tenantId\",      \"emailId\" : \"emailId\",      \"id\" : 6,      \"userName\" : \"userName\",      \"uuid\" : \"uuid\"    },    \"ver\" : \"ver\",    \"requesterId\" : \"requesterId\",    \"authToken\" : \"authToken\",    \"action\" : \"action\",    \"msgId\" : \"msgId\",    \"correlationId\" : \"correlationId\",    \"apiId\" : \"apiId\",    \"did\" : \"did\",    \"key\" : \"key\",    \"ts\" : 0  }}", CaseSummaryResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                return new ResponseEntity<CaseSummaryResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<CaseSummaryResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/casemanager/case/v1/_ungroup", method = RequestMethod.POST)
    public ResponseEntity<CaseGroupResponse> casemanagerCaseV1UngroupPost(@Parameter(in = ParameterIn.DEFAULT, description = "Details of the court cases to be ungrouped + ResponseInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CaseGroupRequest body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<CaseGroupResponse>(objectMapper.readValue("{  \"caseGroups\" : [ {    \"caseIds\" : [ \"caseIds\", \"caseIds\" ],    \"id\" : \"id\"  }, {    \"caseIds\" : [ \"caseIds\", \"caseIds\" ],    \"id\" : \"id\"  } ],  \"responseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  }}", CaseGroupResponse.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                return new ResponseEntity<CaseGroupResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<CaseGroupResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/credentials/v1/_generate")
    public ResponseEntity<VcCredentialRequest> generateVc(@Valid @RequestBody VcCredentialRequest vcCredentialRequest) {
        VcCredentialRequest response = serviceUrlMapperVCService.generateVc(vcCredentialRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/pdf/v1/_get")
    public ResponseEntity<Object> getPdf(@Valid @RequestBody PdfRequest pdfRequest) {
        Object pdfResponse = serviceUrlMappingPdfService.getSVcUrlMappingPdf(pdfRequest);
        return new ResponseEntity<>(pdfResponse, HttpStatus.OK);
    }

    @PostMapping("/casemanager/case/v1/_buildcasebundle")
    public ResponseEntity<Object> caseBundle(@Valid @RequestBody CaseBundleRequest caseBundleRequest) {
        String fileStoreId = caseBundleService.getCaseBundle(caseBundleRequest);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(caseBundleRequest.getRequestInfo(), true);
        CaseBundleResponse caseBundleResponse = CaseBundleResponse.builder().responseInfo(responseInfo).fileStoreId(fileStoreId).build();
        return new ResponseEntity<>(caseBundleResponse, HttpStatus.OK);
    }

    @PostMapping("/casemanager/case/v1/bulk/_buildcasebundle")
    public ResponseEntity<Object> caseBundleBulk(@Valid @RequestBody CaseBundleBulkRequest caseBundleBulkRequest) {
        Boolean status = caseBundleService.getBulkCaseBundle(caseBundleBulkRequest);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(caseBundleBulkRequest.getRequestInfo(), true);
        CaseBundleBulkResponse caseBundleBulkResponse = CaseBundleBulkResponse.builder().responseInfo(responseInfo).status(status).build();
        return new ResponseEntity<>(caseBundleBulkResponse, HttpStatus.OK);
    }

}
