package org.pucar.dristi.web.controllers;

import org.pucar.dristi.web.models.ApplicationExistsRequest;
import org.pucar.dristi.web.models.ApplicationExistsResponse;
import org.pucar.dristi.web.models.ApplicationListResponse;
import org.pucar.dristi.web.models.ApplicationRequest;
import org.pucar.dristi.web.models.ApplicationResponse;
import org.pucar.dristi.web.models.ErrorRes;
import java.util.UUID;
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
* API tests for ApplicationApiController
*/
@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(ApplicationApiController.class)
@Import(TestConfiguration.class)
public class ApplicationApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void applicationV1CreatePostSuccess() throws Exception {
        mockMvc.perform(post("/application/v1/create").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void applicationV1CreatePostFailure() throws Exception {
        mockMvc.perform(post("/application/v1/create").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void applicationV1ExistsPostSuccess() throws Exception {
        mockMvc.perform(post("/application/v1/exists").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void applicationV1ExistsPostFailure() throws Exception {
        mockMvc.perform(post("/application/v1/exists").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void applicationV1SearchPostSuccess() throws Exception {
        mockMvc.perform(post("/application/v1/search").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void applicationV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/application/v1/search").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void applicationV1UpdatePostSuccess() throws Exception {
        mockMvc.perform(post("/application/v1/update").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void applicationV1UpdatePostFailure() throws Exception {
        mockMvc.perform(post("/application/v1/update").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

}
