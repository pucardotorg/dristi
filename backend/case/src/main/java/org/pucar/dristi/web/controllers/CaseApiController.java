package org.pucar.dristi.web.controllers;

import java.util.Collections;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.service.CasePdfService;
import org.pucar.dristi.service.CaseService;
import org.pucar.dristi.service.WitnessService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
public class CaseApiController {

    private CaseService caseService;

    private WitnessService witnessService;

    private ResponseInfoFactory responseInfoFactory;

    private CasePdfService casePdfService;


    @Autowired
    public CaseApiController(CaseService caseService, WitnessService witnessService, ResponseInfoFactory responseInfoFactory, CasePdfService casePdfService) {
        this.caseService = caseService;
        this.witnessService = witnessService;
        this.responseInfoFactory = responseInfoFactory;
        this.casePdfService = casePdfService;
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

    @PostMapping(value = "/v1/_generatePdf")
    public ResponseEntity<?> caseV1GeneratePdf (
            @Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CaseSearchRequest body){

        CourtCase courtCase = casePdfService.generatePdf(body);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
        CaseResponse caseResponse = CaseResponse.builder().cases(Collections.singletonList(courtCase)).responseInfo(responseInfo).build();
        return new ResponseEntity<>(caseResponse, HttpStatus.OK);
    }

}