//package org.pucar.web.controllers;
//
//import org.junit.Test;
//import org.junit.Ignore;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.pucar.TestConfiguration;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
//* API tests for ClerkApiController
//*/
//@ExtendWith(MockitoExtension.class)
//public class ClerkApiControllerTest {
//
//    @Mock
//    private MockMvc mockMvc;
//
//    @InjectMocks
//    private ClerkApiController clerkApiController;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(clerkApiController).build();
//    }
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
