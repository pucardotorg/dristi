//package org.pucar.web.controllers;
//
//import org.pucar.web.models.AdvocateRequest;
//import org.pucar.web.models.AdvocateResponse;
//import org.pucar.web.models.AdvocateSearchRequest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.junit.jupiter.api.Test;
//import org.pucar.TestConfiguration;
//
//    import java.util.ArrayList;
//    import java.util.HashMap;
//    import java.util.List;
//    import java.util.Map;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
//* API tests for AdvocateApiController
//*/
//@Ignore
//@RunWith(SpringRunner.class)
//@WebMvcTest(AdvocateApiController.class)
//@Import(TestConfiguration.class)
//public class AdvocateApiControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void advocateV1CreatePostSuccess() throws Exception {
//        mockMvc.perform(post("/advocate/v1/_create").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void advocateV1CreatePostFailure() throws Exception {
//        mockMvc.perform(post("/advocate/v1/_create").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void advocateV1SearchPostSuccess() throws Exception {
//        mockMvc.perform(post("/advocate/v1/_search").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void advocateV1SearchPostFailure() throws Exception {
//        mockMvc.perform(post("/advocate/v1/_search").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void advocateV1UpdatePostSuccess() throws Exception {
//        mockMvc.perform(post("/advocate/v1/_update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void advocateV1UpdatePostFailure() throws Exception {
//        mockMvc.perform(post("/advocate/v1/_update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//}
