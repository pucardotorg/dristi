//package org.pucar.dristi.web.controllers;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.junit.Ignore;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.pucar.dristi.TestConfiguration;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
///**
//* API tests for CaseApiController
//*/
//@Ignore
//@RunWith(SpringRunner.class)
//@WebMvcTest(CaseApiController.class)
//@Import(TestConfiguration.class)
//public class CaseApiControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void caseV1CreatePostSuccess() throws Exception {
//        mockMvc.perform(post("/case/v1/_create").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void caseV1CreatePostFailure() throws Exception {
//        mockMvc.perform(post("/case/v1/_create").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void caseV1ExistsPostSuccess() throws Exception {
//        mockMvc.perform(post("/case/v1/_exists").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void caseV1ExistsPostFailure() throws Exception {
//        mockMvc.perform(post("/case/v1/_exists").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void caseV1SearchPostSuccess() throws Exception {
//        mockMvc.perform(post("/case/v1/_search").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void caseV1SearchPostFailure() throws Exception {
//        mockMvc.perform(post("/case/v1/_search").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void caseV1UpdatePostSuccess() throws Exception {
//        mockMvc.perform(post("/case/v1/_update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void caseV1UpdatePostFailure() throws Exception {
//        mockMvc.perform(post("/case/v1/_update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void caseWitnessV1CreatePostSuccess() throws Exception {
//        mockMvc.perform(post("/case/witness/v1/_create").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void caseWitnessV1CreatePostFailure() throws Exception {
//        mockMvc.perform(post("/case/witness/v1/_create").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void caseWitnessV1SearchPostSuccess() throws Exception {
//        mockMvc.perform(post("/case/witness/v1/_search").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void caseWitnessV1SearchPostFailure() throws Exception {
//        mockMvc.perform(post("/case/witness/v1/_search").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void caseWitnessV1UpdatePostSuccess() throws Exception {
//        mockMvc.perform(post("/case/witness/v1/_update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void caseWitnessV1UpdatePostFailure() throws Exception {
//        mockMvc.perform(post("/case/witness/v1/_update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//}
