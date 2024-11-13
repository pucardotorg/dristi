package digit.web.controllers;

import digit.TestConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API tests for JudgeCalendarApiController
 */
@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(JudgeCalendarApiController.class)
@Import(TestConfiguration.class)
public class JudgeCalendarRuleApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getIsHearingValidSuccess() throws Exception {
        mockMvc.perform(post("/judgeCalendarRule/v1/exists/{Id}").contentType(MediaType
                        .APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void getIsHearingValidFailure() throws Exception {
        mockMvc.perform(post("/judgeCalendarRule/v1/exists/{Id}").contentType(MediaType
                        .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void judgeCalendarV1AvailabilityPostSuccess() throws Exception {
        mockMvc.perform(post("/judgeCalendarRule/v1/availability").contentType(MediaType
                        .APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void judgeCalendarV1AvailabilityPostFailure() throws Exception {
        mockMvc.perform(post("/judgeCalendarRule/v1/availability").contentType(MediaType
                        .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void judgeCalendarV1CreatePostSuccess() throws Exception {
        mockMvc.perform(post("/judgeCalendarRule/v1/create").contentType(MediaType
                        .APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void judgeCalendarV1CreatePostFailure() throws Exception {
        mockMvc.perform(post("/judgeCalendarRule/v1/create").contentType(MediaType
                        .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void judgeCalendarV1UpdatePostSuccess() throws Exception {
        mockMvc.perform(post("/judgeCalendarRule/v1/update").contentType(MediaType
                        .APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void judgeCalendarV1UpdatePostFailure() throws Exception {
        mockMvc.perform(post("/judgeCalendarRule/v1/update").contentType(MediaType
                        .APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

}
