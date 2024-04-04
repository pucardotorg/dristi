//package org.pucar.web.controllers;
//
//import org.pucar.web.models.AdvocateClerkRequest;
//import org.pucar.web.models.AdvocateClerkResponse;
//import org.pucar.web.models.AdvocateClerkSearchRequest;
//import org.pucar.web.models.ErrorRes;
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
//import org.pucar.TestConfiguration;
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
//* API tests for ClerkApiController
//*/
//@Ignore
//@RunWith(SpringRunner.class)
//@WebMvcTest(ClerkApiController.class)
//@Import(TestConfiguration.class)
//public class ClerkApiControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void clerkV1CreatePostSuccess() throws Exception {
//        mockMvc.perform(post("/clerk/v1/_create").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void clerkV1CreatePostFailure() throws Exception {
//        mockMvc.perform(post("/clerk/v1/_create").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void clerkV1SearchPostSuccess() throws Exception {
//        mockMvc.perform(post("/clerk/v1/_search").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void clerkV1SearchPostFailure() throws Exception {
//        mockMvc.perform(post("/clerk/v1/_search").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void clerkV1UpdatePostSuccess() throws Exception {
//        mockMvc.perform(post("/clerk/v1/_update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void clerkV1UpdatePostFailure() throws Exception {
//        mockMvc.perform(post("/clerk/v1/_update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//}
