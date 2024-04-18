package org.pucar.dristi.web.controllers;

import org.pucar.dristi.web.models.ErrorRes;
import org.pucar.dristi.web.models.HearingExistsRequest;
import org.pucar.dristi.web.models.HearingExistsResponse;
import org.pucar.dristi.web.models.HearingListResponse;
import org.pucar.dristi.web.models.HearingRequest;
import org.pucar.dristi.web.models.HearingResponse;
import java.time.LocalDate;
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
* API tests for HearingApiController
*/
@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(HearingApiController.class)
@Import(TestConfiguration.class)
public class HearingApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void hearingV1CreatePostSuccess() throws Exception {
        mockMvc.perform(post("/hearing/v1/create").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void hearingV1CreatePostFailure() throws Exception {
        mockMvc.perform(post("/hearing/v1/create").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void hearingV1ExistsPostSuccess() throws Exception {
        mockMvc.perform(post("/hearing/v1/exists").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void hearingV1ExistsPostFailure() throws Exception {
        mockMvc.perform(post("/hearing/v1/exists").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void hearingV1SearchPostSuccess() throws Exception {
        mockMvc.perform(post("/hearing/v1/search").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void hearingV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/hearing/v1/search").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void hearingV1UpdatePostSuccess() throws Exception {
        mockMvc.perform(post("/hearing/v1/update").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void hearingV1UpdatePostFailure() throws Exception {
        mockMvc.perform(post("/hearing/v1/update").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

}
