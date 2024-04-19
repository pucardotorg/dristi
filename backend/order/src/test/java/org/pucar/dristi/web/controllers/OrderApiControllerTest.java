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
//* API tests for OrderApiController
//*/
//@Ignore
//@RunWith(SpringRunner.class)
//@WebMvcTest(OrderApiController.class)
//@Import(TestConfiguration.class)
//public class OrderApiControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void orderV1CreatePostSuccess() throws Exception {
//        mockMvc.perform(post("/order/v1/create").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void orderV1CreatePostFailure() throws Exception {
//        mockMvc.perform(post("/order/v1/create").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void orderV1ExistsPostSuccess() throws Exception {
//        mockMvc.perform(post("/order/v1/exists").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void orderV1ExistsPostFailure() throws Exception {
//        mockMvc.perform(post("/order/v1/exists").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void orderV1SearchPostSuccess() throws Exception {
//        mockMvc.perform(post("/order/v1/search").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void orderV1SearchPostFailure() throws Exception {
//        mockMvc.perform(post("/order/v1/search").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void orderV1UpdatePostSuccess() throws Exception {
//        mockMvc.perform(post("/order/v1/update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void orderV1UpdatePostFailure() throws Exception {
//        mockMvc.perform(post("/order/v1/update").contentType(MediaType
//        .APPLICATION_JSON_UTF8))
//        .andExpect(status().isBadRequest());
//    }
//
//}
