//package org.pucar.dristi.web.controllers;
//
//import org.pucar.dristi.web.models.CaseFileResponse;
//import org.pucar.dristi.web.models.CaseGroupRequest;
//import org.pucar.dristi.web.models.CaseGroupResponse;
//import org.pucar.dristi.web.models.CaseRequest;
//import org.pucar.dristi.web.models.CaseSummaryResponse;
//import org.pucar.dristi.web.models.ErrorRes;
//import org.junit.Test;
//import org.junit.Ignore;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.pucar.dristi.TestConfiguration;
//
//    import java.util.ArrayList;
//    import java.util.HashMap;
//    import java.util.List;
//    import java.util.Map;
//
//import static org.mockito.Matchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
//* API tests for CasemanagerApiController
//*/
//@Ignore
//@RunWith(SpringRunner.class)
//@WebMvcTest(CasemanagerApiController.class)
//@Import(TestConfiguration.class)
//public class CasemanagerApiControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void casemanagerCaseV1GroupPostSuccess() throws Exception {
//        mockMvc.perform(post("/casemanager/case/v1/_group").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void casemanagerCaseV1GroupPostFailure() throws Exception {
//        mockMvc.perform(post("/casemanager/case/v1/_group").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void casemanagerCaseV1HistoryPostSuccess() throws Exception {
//        mockMvc.perform(post("/casemanager/case/v1/_history").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void casemanagerCaseV1HistoryPostFailure() throws Exception {
//        mockMvc.perform(post("/casemanager/case/v1/_history").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void casemanagerCaseV1SummaryPostSuccess() throws Exception {
//        mockMvc.perform(post("/casemanager/case/v1/_summary").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void casemanagerCaseV1SummaryPostFailure() throws Exception {
//        mockMvc.perform(post("/casemanager/case/v1/_summary").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void casemanagerCaseV1UngroupPostSuccess() throws Exception {
//        mockMvc.perform(post("/casemanager/case/v1/_ungroup").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void casemanagerCaseV1UngroupPostFailure() throws Exception {
//        mockMvc.perform(post("/casemanager/case/v1/_ungroup").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//}
