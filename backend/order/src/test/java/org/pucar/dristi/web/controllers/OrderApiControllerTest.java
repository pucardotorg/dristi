package org.pucar.dristi.web.controllers;

import org.pucar.dristi.web.models.ErrorRes;
import org.pucar.dristi.web.models.OrderExistsRequest;
import org.pucar.dristi.web.models.OrderExistsResponse;
import org.pucar.dristi.web.models.OrderListResponse;
import org.pucar.dristi.web.models.OrderRequest;
import org.pucar.dristi.web.models.OrderResponse;
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
* API tests for OrderApiController
*/
@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(OrderApiController.class)
@Import(TestConfiguration.class)
public class OrderApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void orderV1CreatePostSuccess() throws Exception {
        mockMvc.perform(post("/order/v1/create").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void orderV1CreatePostFailure() throws Exception {
        mockMvc.perform(post("/order/v1/create").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void orderV1ExistsPostSuccess() throws Exception {
        mockMvc.perform(post("/order/v1/exists").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void orderV1ExistsPostFailure() throws Exception {
        mockMvc.perform(post("/order/v1/exists").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void orderV1SearchPostSuccess() throws Exception {
        mockMvc.perform(post("/order/v1/search").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void orderV1SearchPostFailure() throws Exception {
        mockMvc.perform(post("/order/v1/search").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void orderV1UpdatePostSuccess() throws Exception {
        mockMvc.perform(post("/order/v1/update").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void orderV1UpdatePostFailure() throws Exception {
        mockMvc.perform(post("/order/v1/update").contentType(MediaType
        .APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest());
    }

}
