package org.pucar.dristi.web.controllers;


import org.pucar.dristi.service.OpenApiService;
import org.pucar.dristi.web.models.CaseListResponse;
import org.pucar.dristi.web.models.CaseSummaryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-12-03T13:11:23.212020900+05:30[Asia/Calcutta]")
@Controller
@RequestMapping("")
public class OpenapiApiController {

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final OpenApiService openApiService;

    @Autowired
    public OpenapiApiController(ObjectMapper objectMapper, HttpServletRequest request, OpenApiService openApiService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.openApiService = openApiService;
    }

    @RequestMapping(value = "/openapi/v1/{tenantId}/case/cnr/{cnrNumber}", method = RequestMethod.GET)
    public ResponseEntity<CaseSummaryResponse> getCaseByCNR(@Pattern(regexp = "^[a-zA-Z]{2}$") @Size(min = 2, max = 2) @Parameter(in = ParameterIn.PATH, description = "tenant ID", required = true, schema = @Schema()) @PathVariable("tenantId") String tenantId, @Size(min = 16, max = 16) @Parameter(in = ParameterIn.PATH, description = "the CNR number of the case in format SCDCECNNNNNNYYYY where SC=State Code, DC=District Code, EC=Establishment Code NNNNNN=Case Number and YYYY=Year, whose summary is requested", required = true, schema = @Schema()) @PathVariable("cnrNumber") String cnrNumber) {
        CaseSummaryResponse caseSummaryResponse = openApiService.getCaseByCnrNumber(tenantId, cnrNumber);
        return new ResponseEntity<>(caseSummaryResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/openapi/v1/{tenantId}/case/{year}/{caseType}/{caseNumber}", method = RequestMethod.GET)
    public ResponseEntity<CaseSummaryResponse> getCaseByCaseNumber(@Pattern(regexp = "^[a-zA-Z]{2}$") @Size(min = 2, max = 2) @Parameter(in = ParameterIn.PATH, description = "tenant ID", required = true, schema = @Schema()) @PathVariable("tenantId") String tenantId, @Min(2024) @Parameter(in = ParameterIn.PATH, description = "if type= CMP, then year in which the case was registered. Can check based on registration date also. If type = CC/ST, then check against CourtCase.courtCaseNumber(year). The minimum year is set to 2024 as this is the year the system has gone live and the first case in the system is from 2024. No earlier cases exist.", required = true, schema = @Schema(allowableValues = "")) @PathVariable("year") Integer year, @Parameter(in = ParameterIn.PATH, description = "the type of the case CMP/CC/ST", required = true, schema = @Schema(allowableValues = "")) @PathVariable("caseType") String caseType, @NotNull @Min(1) @Max(99999999) @Parameter(in = ParameterIn.QUERY, description = "Number part of CMP/CC/ST case number in format <type>/<number>/<year>", required = true, schema = @Schema(allowableValues = "")) @Valid @PathVariable(value = "caseNumber") Integer caseNumber) {
        CaseSummaryResponse caseSummaryResponse = openApiService.getCaseByCaseNumber(tenantId, year, caseType, caseNumber);
        return new ResponseEntity<>(caseSummaryResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/openapi/v1/{tenantId}/case/{year}/{caseType}", method = RequestMethod.GET)
    public ResponseEntity<CaseListResponse> getCaseListByCaseType(@Pattern(regexp = "^[a-zA-Z]{2}$") @Size(min = 2, max = 2) @Parameter(in = ParameterIn.PATH, description = "tenant ID", required = true, schema = @Schema()) @PathVariable("tenantId") String tenantId, @Min(2024) @Parameter(in = ParameterIn.PATH, description = "if type= CMP, then year in which the case was registered. Can check based on registration date also. If type = CC/ST, then check against CourtCase.courtCaseNumber(year). The minimum year is set to 2024 as this is the year the system has gone live and the first case in the system is from 2024. No earlier cases exist.", required = true, schema = @Schema(allowableValues = "")) @PathVariable("year") Integer year, @Parameter(in = ParameterIn.PATH, description = "the type of the case CMP/CC/ST", required = true, schema = @Schema(allowableValues = "")) @PathVariable("caseType") String caseType, @Min(0) @Parameter(in = ParameterIn.QUERY, description = "Page number to retrieve (0-based index)", schema = @Schema(allowableValues = "", defaultValue = "0")) @Valid @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset, @Min(1) @Max(100) @Parameter(in = ParameterIn.QUERY, description = "Number of items per page", schema = @Schema(allowableValues = "", defaultValue = "10")) @Valid @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit, @Pattern(regexp = "^(registrationDate|filingDate),(asc|desc)$") @Parameter(in = ParameterIn.QUERY, description = "Sorting criteria in the format `field,asc` or `field,desc`", schema = @Schema()) @Valid @RequestParam(value = "sort", required = false , defaultValue = "registrationDate,desc") String sort) {

        CaseListResponse caseList = openApiService.getCaseListByCaseType(tenantId, year, caseType, offset, limit, sort);
        return new ResponseEntity<>(caseList, HttpStatus.OK);
    }

}
