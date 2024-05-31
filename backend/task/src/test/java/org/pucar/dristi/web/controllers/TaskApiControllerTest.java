//package org.pucar.dristi.web.controllers;
//
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.pucar.dristi.TestConfiguration;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
//* API tests for TaskApiController
//*/
//@Ignore
//@RunWith(SpringRunner.class)
//@WebMvcTest(TaskApiController.class)
//@Import(TestConfiguration.class)
//public class TaskApiControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void taskV1CreatePostSuccess() throws Exception {
//        mockMvc.perform(post("/task/v1/create").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void taskV1CreatePostFailure() throws Exception {
//        mockMvc.perform(post("/task/v1/create").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void taskV1ExistsPostSuccess() throws Exception {
//        mockMvc.perform(post("/task/v1/exists").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void taskV1ExistsPostFailure() throws Exception {
//        mockMvc.perform(post("/task/v1/exists").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void taskV1SearchPostSuccess() throws Exception {
//        mockMvc.perform(post("/task/v1/search").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void taskV1SearchPostFailure() throws Exception {
//        mockMvc.perform(post("/task/v1/search").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void taskV1UpdatePostSuccess() throws Exception {
//        mockMvc.perform(post("/task/v1/update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void taskV1UpdatePostFailure() throws Exception {
//        mockMvc.perform(post("/task/v1/update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//}
