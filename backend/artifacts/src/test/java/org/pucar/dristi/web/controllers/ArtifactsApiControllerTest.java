//package org.pucar.dristi.web.controllers;
//
//import org.pucar.dristi.web.models.ErrorRes;
//import org.pucar.dristi.web.models.EvidenceRequest;
//import org.pucar.dristi.web.models.EvidenceResponse;
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
//* API tests for ArtifactsApiController
//*/
//@Ignore
//@RunWith(SpringRunner.class)
//@WebMvcTest(ArtifactsApiController.class)
//@Import(TestConfiguration.class)
//public class ArtifactsApiControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void artifactsV1CreatePostSuccess() throws Exception {
//        mockMvc.perform(post("/artifacts/v1/_create").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void artifactsV1CreatePostFailure() throws Exception {
//        mockMvc.perform(post("/artifacts/v1/_create").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void artifactsV1UpdatePostSuccess() throws Exception {
//        mockMvc.perform(post("/artifacts/v1/_update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void artifactsV1UpdatePostFailure() throws Exception {
//        mockMvc.perform(post("/artifacts/v1/_update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//}
