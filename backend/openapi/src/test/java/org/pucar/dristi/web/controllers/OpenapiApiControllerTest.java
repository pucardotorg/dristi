package org.pucar.dristi.web.controllers;

import org.pucar.dristi.web.models.CaseListResponse;
import org.pucar.dristi.web.models.CaseSummaryResponse;
import org.pucar.dristi.web.models.ErrorRes;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.pucar.dristi.TestConfiguration;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
* API tests for OpenapiApiController
*/
@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(OpenapiApiController.class)
@Import(TestConfiguration.class)
public class OpenapiApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getCaseByCNRSuccess() throws Exception {
        mockMvc.perform(post("/openapi/v1/{tenantID}/case/cnr/{cnrNumber}").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void getCaseByCNRFailure() throws Exception {
        mockMvc.perform(post("/openapi/v1/{tenantID}/case/cnr/{cnrNumber}").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void getCaseByCaseNumberSuccess() throws Exception {
        mockMvc.perform(post("/openapi/v1/{tenantID}/case/{year}/{caseType}/{caseNumber}").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void getCaseByCaseNumberFailure() throws Exception {
        mockMvc.perform(post("/openapi/v1/{tenantID}/case/{year}/{caseType}/{caseNumber}").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void getCaseListByCaseTypeSuccess() throws Exception {
        mockMvc.perform(post("/openapi/v1/{tenantID}/case/{year}/{caseType}").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void getCaseListByCaseTypeFailure() throws Exception {
        mockMvc.perform(post("/openapi/v1/{tenantID}/case/{year}/{caseType}").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

}
