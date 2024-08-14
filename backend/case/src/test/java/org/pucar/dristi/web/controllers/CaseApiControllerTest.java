package org.pucar.dristi.web.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.service.CaseService;
import org.pucar.dristi.service.WitnessService;
import org.pucar.dristi.util.ResponseInfoFactory;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.CaseExists;
import org.pucar.dristi.web.models.CaseExistsRequest;
import org.pucar.dristi.web.models.CaseExistsResponse;
import org.pucar.dristi.web.models.CaseListResponse;
import org.pucar.dristi.web.models.CaseRequest;
import org.pucar.dristi.web.models.CaseResponse;
import org.pucar.dristi.web.models.CaseSearchRequest;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.JoinCaseRequest;
import org.pucar.dristi.web.models.JoinCaseResponse;
import org.pucar.dristi.web.models.Witness;
import org.pucar.dristi.web.models.WitnessRequest;
import org.pucar.dristi.web.models.WitnessResponse;
import org.pucar.dristi.web.models.WitnessSearchRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
* API tests for CaseApiController
*/
@ExtendWith(MockitoExtension.class)
public class CaseApiControllerTest {

    @InjectMocks
    private CaseApiController caseApiController;
    @Mock
    private CaseService caseService;
    @Mock
    private WitnessService witnessService;
    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void caseV1CreatePostSuccess() {
        // Mocking request body
        CaseRequest caseRequest = new CaseRequest(); // Create a mock CaseRequest object

        // Mocking caseService.createCase method to return a CourtCase object
        CourtCase courtCase = new CourtCase(); // Create a mock CourtCase object
        when(caseService.createCase(caseRequest)).thenReturn(courtCase);

        // Mocking responseInfoFactory.createResponseInfoFromRequestInfo method to return a ResponseInfo object
        ResponseInfo responseInfo = new ResponseInfo(); // Create a mock ResponseInfo object
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), any())).thenReturn(responseInfo);

        // Call the method under test
        ResponseEntity<CaseResponse> responseEntity = caseApiController.caseV1CreatePost(caseRequest);

        // Verify the response entity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(courtCase, responseEntity.getBody().getCases().get(0));
    }

    @Test
    public void caseV1ExistsPostSuccess() {
        // Mocking request body
        CaseExistsRequest caseRequest = new CaseExistsRequest(); // Create a mock CaseRequest object

        // Mocking caseService.createCase method to return a CourtCase object
        CaseExists caseExists = new CaseExists(); // Create a mock CourtCase object
        when(caseService.existCases(caseRequest)).thenReturn(List.of(caseExists));

        // Mocking responseInfoFactory.createResponseInfoFromRequestInfo method to return a ResponseInfo object
        ResponseInfo responseInfo = new ResponseInfo(); // Create a mock ResponseInfo object
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), any())).thenReturn(responseInfo);

        // Call the method under test
        ResponseEntity<CaseExistsResponse> responseEntity = caseApiController.caseV1ExistsPost(caseRequest);

        // Verify the response entity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(caseExists, responseEntity.getBody().getCriteria().get(0));
    }

    @Test
    public void caseV1SearchPostSuccess() {
        // Mocking request body
        CaseSearchRequest caseRequest = new CaseSearchRequest(); // Create a mock CaseRequest object
        caseRequest.setCriteria(List.of(CaseCriteria.builder().cnrNumber("cnrNumber").build()));
        // Mocking caseService.createCase method to return a CourtCase object
        caseService.searchCases(caseRequest);

        // Mocking responseInfoFactory.createResponseInfoFromRequestInfo method to return a ResponseInfo object
        ResponseInfo responseInfo = new ResponseInfo(); // Create a mock ResponseInfo object
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), any())).thenReturn(responseInfo);

        // Call the method under test
        ResponseEntity<CaseListResponse> responseEntity = caseApiController.caseV1SearchPost(caseRequest);

        // Verify the response entity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(caseRequest.getCriteria(), responseEntity.getBody().getCriteria());
    }

    @Test
    public void verifyV1JoinCaseSuccess() {
        JoinCaseRequest joinCaseRequest = new JoinCaseRequest();
        RequestInfo requestInfo = new RequestInfo();
        joinCaseRequest.setRequestInfo(requestInfo);

        JoinCaseResponse joinCaseResponse = new JoinCaseResponse();
        ResponseInfo responseInfo = new ResponseInfo();
        // Mocking caseService.verifyJoinCaseRequest method to return a JoinCaseResponse object
        when(caseService.verifyJoinCaseRequest(any(JoinCaseRequest.class))).thenReturn(joinCaseResponse);

        // Mocking responseInfoFactory.createResponseInfoFromRequestInfo method to return a ResponseInfo object
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class))).thenReturn(responseInfo);

        // Call the method under test
        ResponseEntity<JoinCaseResponse> responseEntity = caseApiController.verifyV1JoinCase(joinCaseRequest);

        // Verify the response entity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseInfo, responseEntity.getBody().getResponseInfo());
    }

    @Test
    public void caseV1UpdatePostSuccess() {
        // Mocking request body
        CaseRequest caseRequest = new CaseRequest(); // Create a mock CaseRequest object

        // Mocking caseService.createCase method to return a CourtCase object
        CourtCase courtCase = new CourtCase(); // Create a mock CourtCase object
        when(caseService.updateCase(caseRequest)).thenReturn(courtCase);

        // Mocking responseInfoFactory.createResponseInfoFromRequestInfo method to return a ResponseInfo object
        ResponseInfo responseInfo = new ResponseInfo(); // Create a mock ResponseInfo object
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), any())).thenReturn(responseInfo);

        // Call the method under test
        ResponseEntity<CaseResponse> responseEntity = caseApiController.caseV1UpdatePost(caseRequest);

        // Verify the response entity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(courtCase, responseEntity.getBody().getCases().get(0));
    }

    @Test
    public void caseWitnessV1CreatePostSuccess() {
        // Mocking request body
        WitnessRequest witnessRequest = new WitnessRequest(); // Create a mock CaseRequest object

        // Mocking caseService.createCase method to return a CourtCase object
        Witness witness = new Witness(); // Create a mock CourtCase object
        when(witnessService.registerWitnessRequest(witnessRequest)).thenReturn(witness);

        // Mocking responseInfoFactory.createResponseInfoFromRequestInfo method to return a ResponseInfo object
        ResponseInfo responseInfo = new ResponseInfo(); // Create a mock ResponseInfo object
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), any())).thenReturn(responseInfo);

        // Call the method under test
        ResponseEntity<WitnessResponse> responseEntity = caseApiController.caseWitnessV1CreatePost(witnessRequest);

        // Verify the response entity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(witness, responseEntity.getBody().getWitnesses().get(0));
    }

    @Test
    public void caseWitnessV1SearchPostSuccess() {
        // Mocking request body
        WitnessSearchRequest witnessRequest = new WitnessSearchRequest(); // Create a mock CaseRequest object

        // Mocking caseService.createCase method to return a CourtCase object
        Witness witness = new Witness(); // Create a mock CourtCase object
        when(witnessService.searchWitnesses(witnessRequest)).thenReturn(List.of(witness));

        // Mocking responseInfoFactory.createResponseInfoFromRequestInfo method to return a ResponseInfo object
        ResponseInfo responseInfo = new ResponseInfo(); // Create a mock ResponseInfo object
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), any())).thenReturn(responseInfo);

        // Call the method under test
        ResponseEntity<WitnessResponse> responseEntity = caseApiController.caseWitnessV1SearchPost(witnessRequest);

        // Verify the response entity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(witness, responseEntity.getBody().getWitnesses().get(0));
    }

    @Test
    public void caseWitnessV1UpdatePostSuccess() {
        // Mocking request body
        WitnessRequest witnessRequest = new WitnessRequest(); // Create a mock CaseRequest object

        // Mocking caseService.createCase method to return a CourtCase object
        Witness witness = new Witness(); // Create a mock CourtCase object
        when(witnessService.updateWitness(witnessRequest)).thenReturn(witness);

        // Mocking responseInfoFactory.createResponseInfoFromRequestInfo method to return a ResponseInfo object
        ResponseInfo responseInfo = new ResponseInfo(); // Create a mock ResponseInfo object
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), any())).thenReturn(responseInfo);

        // Call the method under test
        ResponseEntity<WitnessResponse> responseEntity = caseApiController.caseWitnessV1UpdatePost(witnessRequest);

        // Verify the response entity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(witness, responseEntity.getBody().getWitnesses().get(0));
    }

}
