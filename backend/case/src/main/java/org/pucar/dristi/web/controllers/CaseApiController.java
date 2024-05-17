package org.pucar.dristi.web.controllers;

import java.io.IOException;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
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
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import static org.pucar.dristi.config.ServiceConstants.ACCEPT;
import static org.pucar.dristi.config.ServiceConstants.APPLICATION_JSON;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Controller
@RequestMapping("")
public class CaseApiController {


    private final HttpServletRequest request;

    @Autowired
    private CaseService caseService;

    @Autowired
    private WitnessService witnessService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;


    @Autowired
    public CaseApiController( HttpServletRequest request) {
        this.request = request;
    }

    @PostMapping(value = "/case/v1/_create")
    public ResponseEntity<CaseResponse> caseV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new court case + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CaseRequest body) {
        String accept = request.getHeader(ACCEPT);
            if (accept != null && accept.contains(APPLICATION_JSON)) {
                try {
                    List<CourtCase> caseList = caseService.createCase(body);
                    ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
                    CaseResponse advocateClerkResponse = CaseResponse.builder().cases(caseList).responseInfo(responseInfo).build();
                    return new ResponseEntity<>(advocateClerkResponse, HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }

    @PostMapping(value = "/case/v1/_exists")
        public ResponseEntity<CaseExistsResponse> caseV1ExistsPost (
                @Parameter(in = ParameterIn.DEFAULT, description = "Case search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CaseSearchRequest
        body){
            String accept = request.getHeader(ACCEPT);
            if (accept != null && accept.contains(APPLICATION_JSON)) {
                try {
                    List<CaseExists> caseExists = caseService.existCases(body);
                    ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
                    CaseExistsResponse caseExistsResponse = CaseExistsResponse.builder().criteria(caseExists).responseInfo(responseInfo).build();
                    return new ResponseEntity<>(caseExistsResponse, HttpStatus.OK);

                } catch (Exception e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    @PostMapping(value = "/case/v1/_search")
        public ResponseEntity<CaseResponse> caseV1SearchPost (
                @Parameter(in = ParameterIn.DEFAULT, description = "Search criteria + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CaseSearchRequest body){
            String accept = request.getHeader(ACCEPT);
            if (accept != null && accept.contains(APPLICATION_JSON)) {
                try {
                    List<CourtCase> caseList = caseService.searchCases(body);
                    ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
                    CaseResponse caseResponse = CaseResponse.builder().cases(caseList).responseInfo(responseInfo).build();
                    return new ResponseEntity<>(caseResponse, HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
        }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/case/v1/_update")
        public ResponseEntity<CaseResponse> caseV1UpdatePost (
                @Parameter(in = ParameterIn.DEFAULT, description = "Details for updating all editable fields in the court case + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody CaseRequest
        body){
            String accept = request.getHeader(ACCEPT);
            if (accept != null && accept.contains(APPLICATION_JSON)) {
                try {
                    List<CourtCase> caseList = caseService.updateCase(body);
                    ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
                    CaseResponse caseResponse = CaseResponse.builder().cases(caseList).responseInfo(responseInfo).build();
                    return new ResponseEntity<>(caseResponse, HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    @PostMapping(value = "/case/witness/v1/_create")
        public ResponseEntity<WitnessResponse> caseWitnessV1CreatePost (
                @Parameter(in = ParameterIn.DEFAULT, description = "Details for the witness + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody WitnessRequest
        body){
            String accept = request.getHeader(ACCEPT);
            if (accept != null && accept.contains(APPLICATION_JSON)) {
                List<Witness> witnessesList = witnessService.registerWitnessRequest(body);
                ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
                WitnessResponse witnessResponse = WitnessResponse.builder().witnesses(witnessesList).requestInfo(responseInfo).build();
                return new ResponseEntity<>(witnessResponse, HttpStatus.OK);
            }

            return new ResponseEntity<WitnessResponse>(HttpStatus.NOT_IMPLEMENTED);
        }

    @PostMapping(value = "/case/witness/v1/_search")
        public ResponseEntity<WitnessResponse> caseWitnessV1SearchPost (
                @Parameter(in = ParameterIn.DEFAULT, description = "Details for the witness + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody WitnessSearchRequest
        body){
            String accept = request.getHeader(ACCEPT);
            if (accept != null && accept.contains(APPLICATION_JSON)) {
                List<Witness> witnessList = witnessService.searchWitnesses(body);
                ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
                WitnessResponse witnessResponse = WitnessResponse.builder().witnesses(witnessList).requestInfo(responseInfo).build();
                return new ResponseEntity<>(witnessResponse, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }

    @PostMapping(value = "/case/witness/v1/_update")
        public ResponseEntity<WitnessResponse> caseWitnessV1UpdatePost (
                @Parameter(in = ParameterIn.DEFAULT, description = "Details for the witness + RequestInfo meta data.", required = true, schema = @Schema()) @Valid @RequestBody WitnessRequest
        body){
            String accept = request.getHeader(ACCEPT);
            if (accept != null && accept.contains(APPLICATION_JSON)) {
                List<Witness> witnessList = witnessService.updateWitness(body);
                ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
                WitnessResponse witnessResponse = WitnessResponse.builder().witnesses(witnessList).requestInfo(responseInfo).build();
                return new ResponseEntity<>(witnessResponse, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }

    }


