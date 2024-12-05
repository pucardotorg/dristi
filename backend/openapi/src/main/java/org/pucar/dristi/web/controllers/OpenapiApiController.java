package org.pucar.dristi.web.controllers;


import org.pucar.dristi.web.models.CaseListResponse;
import org.pucar.dristi.web.models.CaseSummaryResponse;
import org.pucar.dristi.web.models.ErrorRes;
    import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.*;

    import jakarta.validation.constraints.*;
    import jakarta.validation.Valid;
    import jakarta.servlet.http.HttpServletRequest;
        import java.util.Optional;
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-12-03T13:11:23.212020900+05:30[Asia/Calcutta]")
@Controller
    @RequestMapping("")
    public class OpenapiApiController{

        private final ObjectMapper objectMapper;

        private final HttpServletRequest request;

        @Autowired
        public OpenapiApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
        }

                @RequestMapping(value="/openapi/v1/{tenantID}/case/cnr/{cnrNumber}", method = RequestMethod.GET)
                public ResponseEntity<CaseSummaryResponse> getCaseByCNR(@Pattern(regexp="^[a-zA-Z]{2}$") @Size(min=2,max=2) @Parameter(in = ParameterIn.PATH, description = "tenant ID", required=true, schema=@Schema()) @PathVariable("tenantID") String tenantID,@Size(min=16,max=16) @Parameter(in = ParameterIn.PATH, description = "the CNR number of the case in format SCDCECNNNNNNYYYY where SC=State Code, DC=District Code, EC=Establishment Code NNNNNN=Case Number and YYYY=Year, whose summary is requested", required=true, schema=@Schema()) @PathVariable("cnrNumber") String cnrNumber) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<CaseSummaryResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"caseSummary\" : {    \"filingNumber\" : \"KL-000075-2024\",    \"filingDate\" : 6,    \"judgeName\" : \"judgeName\",    \"respondent\" : \"respondent\",    \"cnrNumber\" : \"KLKM521235562024\",    \"complainant\" : \"complainant\",    \"nextHearingDate\" : 5,    \"caseType\" : \"Criminal Miscellaneous Petition\",    \"statutesAndSections\" : [ {      \"statute\" : \"NIA\",      \"sections\" : [ \"S138\", \"S142(1)(a)\", \"S145\" ]    }, {      \"statute\" : \"NIA\",      \"sections\" : [ \"S138\", \"S142(1)(a)\", \"S145\" ]    } ],    \"advocateRespondent\" : \"advocateRespondent\",    \"registrationNumber\" : \"CMP/1/2024\",    \"registrationDate\" : 1,    \"advocateComplainant\" : \"advocateComplainant\",    \"subStage\" : \"Pre-Trail - Appearance\",    \"status\" : \"Pending\"  }}", CaseSummaryResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<CaseSummaryResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<CaseSummaryResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/openapi/v1/{tenantID}/case/{year}/{caseType}/{caseNumber}", method = RequestMethod.GET)
                public ResponseEntity<CaseListResponse> getCaseByCaseNumber(@Pattern(regexp="^[a-zA-Z]{2}$") @Size(min=2,max=2) @Parameter(in = ParameterIn.PATH, description = "tenant ID", required=true, schema=@Schema()) @PathVariable("tenantID") String tenantID,@Min(2024)@Parameter(in = ParameterIn.PATH, description = "if type= CMP, then year in which the case was registered. Can check based on registration date also. If type = CC/ST, then check against CourtCase.courtCaseNumber(year). The minimum year is set to 2024 as this is the year the system has gone live and the first case in the system is from 2024. No earlier cases exist.", required=true, schema=@Schema(allowableValues="")) @PathVariable("year") Integer year,@Parameter(in = ParameterIn.PATH, description = "the type of the case CMP/CC/ST", required=true, schema=@Schema(allowableValues="")) @PathVariable("caseType") String caseType,@NotNull @Min(1) @Max(99999999) @Parameter(in = ParameterIn.QUERY, description = "Number part of CMP/CC/ST case number in format <type>/<number>/<year>" ,required=true,schema=@Schema(allowableValues="")) @Valid @RequestParam(value = "caseNumber", required = true) Integer caseNumber) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<CaseListResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"pagination\" : {    \"offSet\" : 6.027456183070403,    \"limit\" : 8.008281904610115,    \"sortBy\" : \"sortBy\",    \"totalCount\" : 1.4658129805029452,    \"order\" : \"\"  },  \"caseList\" : [ {    \"caseNumber\" : \"CMP/1/2024\",    \"caseTitle\" : \"caseTitle\"  }, {    \"caseNumber\" : \"CMP/1/2024\",    \"caseTitle\" : \"caseTitle\"  } ]}", CaseListResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<CaseListResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<CaseListResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

                @RequestMapping(value="/openapi/v1/{tenantID}/case/{year}/{caseType}", method = RequestMethod.GET)
                public ResponseEntity<CaseListResponse> getCaseListByCaseType(@Pattern(regexp="^[a-zA-Z]{2}$") @Size(min=2,max=2) @Parameter(in = ParameterIn.PATH, description = "tenant ID", required=true, schema=@Schema()) @PathVariable("tenantID") String tenantID,@Min(2024)@Parameter(in = ParameterIn.PATH, description = "if type= CMP, then year in which the case was registered. Can check based on registration date also. If type = CC/ST, then check against CourtCase.courtCaseNumber(year). The minimum year is set to 2024 as this is the year the system has gone live and the first case in the system is from 2024. No earlier cases exist.", required=true, schema=@Schema(allowableValues="")) @PathVariable("year") Integer year,@Parameter(in = ParameterIn.PATH, description = "the type of the case CMP/CC/ST", required=true, schema=@Schema(allowableValues="")) @PathVariable("caseType") String caseType,@Min(0)@Parameter(in = ParameterIn.QUERY, description = "Page number to retrieve (0-based index)" ,schema=@Schema(allowableValues="", defaultValue="0")) @Valid @RequestParam(value = "offset", required = false, defaultValue="0") Integer offset,@Min(1) @Max(100) @Parameter(in = ParameterIn.QUERY, description = "Number of items per page" ,schema=@Schema(allowableValues="", defaultValue="10")) @Valid @RequestParam(value = "limit", required = false, defaultValue="10") Integer limit,@Pattern(regexp="^(registrationDate|filingDate),(asc|desc)$") @Parameter(in = ParameterIn.QUERY, description = "Sorting criteria in the format `field,asc` or `field,desc`" ,schema=@Schema()) @Valid @RequestParam(value = "sort", required = false) String sort) {
                        String accept = request.getHeader("Accept");
                            if (accept != null && accept.contains("application/json")) {
                            try {
                            return new ResponseEntity<CaseListResponse>(objectMapper.readValue("{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"pagination\" : {    \"offSet\" : 6.027456183070403,    \"limit\" : 8.008281904610115,    \"sortBy\" : \"sortBy\",    \"totalCount\" : 1.4658129805029452,    \"order\" : \"\"  },  \"caseList\" : [ {    \"caseNumber\" : \"CMP/1/2024\",    \"caseTitle\" : \"caseTitle\"  }, {    \"caseNumber\" : \"CMP/1/2024\",    \"caseTitle\" : \"caseTitle\"  } ]}", CaseListResponse.class), HttpStatus.NOT_IMPLEMENTED);
                            } catch (IOException e) {
                            return new ResponseEntity<CaseListResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                            }

                        return new ResponseEntity<CaseListResponse>(HttpStatus.NOT_IMPLEMENTED);
                }

        }
