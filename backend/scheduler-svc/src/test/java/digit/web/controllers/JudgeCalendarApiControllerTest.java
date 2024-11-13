package digit.web.controllers;

import digit.service.CalendarService;
import digit.web.models.*;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;


@RunWith(MockitoJUnitRunner.class)
public class JudgeCalendarApiControllerTest {


    @Mock
    private CalendarService calendarService;

    @InjectMocks
    private JudgeCalendarApiController judgeCalendarApiController;

    @Test
    public void testGetJudgeCalendar(){
        JudgeCalendarSearchRequest searchRequest = new JudgeCalendarSearchRequest();
        CalendarSearchCriteria searchCriteria = CalendarSearchCriteria.builder().judgeId("judgeId").tenantId("tenantId").fromDate(LocalDate.now().toEpochDay()).toDate(LocalDate.now().plusDays(10).toEpochDay()).build();
        searchRequest.setCriteria(searchCriteria);

        HearingCalendar hearingCalendar = HearingCalendar.builder().judgeId("judgeId").date(LocalDate.now().toEpochDay()).build();
        List<HearingCalendar> calendarList = List.of(hearingCalendar);
        when(calendarService.getJudgeCalendar(searchRequest)).thenReturn(calendarList);

        ResponseEntity<JudgeCalendarResponse> response = judgeCalendarApiController.getJudgeCalendar(searchRequest);

        assertEquals(calendarList, response.getBody().getCalendar());
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

    }

    @Test
    public void testGetJudgeAvailability(){
        JudgeAvailabilitySearchRequest request = new JudgeAvailabilitySearchRequest();
        List<AvailabilityDTO> judgeAvailability = Arrays.asList(AvailabilityDTO.builder().date(LocalDate.now().toString()).occupiedBandwidth(0.0).build());

        when(calendarService.getJudgeAvailability(any(JudgeAvailabilitySearchRequest.class))).thenReturn(judgeAvailability);

        ResponseEntity<JudgeAvailabilityResponse> responseEntity = judgeCalendarApiController.getAvailabilityOfJudge(request);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals(judgeAvailability, responseEntity.getBody().getAvailableDates());

    }

    @Test
    public void testUpdateJudgeCalendar() {
        JudgeCalendarUpdateRequest request = new JudgeCalendarUpdateRequest();
        List<JudgeCalendarRule> updatedJudgeCalendarRule = Arrays.asList(new JudgeCalendarRule(), new JudgeCalendarRule());

        when(calendarService.upsert(any(JudgeCalendarUpdateRequest.class))).thenReturn(updatedJudgeCalendarRule);

        ResponseEntity<?> responseEntity = judgeCalendarApiController.updateJudgeCalendar(request);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals(updatedJudgeCalendarRule, responseEntity.getBody());
    }
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void getIsHearingValidSuccess() throws Exception {
//        mockMvc.perform(post("/judgeCalendarRule/v1/exists/{Id}").contentType(MediaType
//                        .APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void getIsHearingValidFailure() throws Exception {
//        mockMvc.perform(post("/judgeCalendarRule/v1/exists/{Id}").contentType(MediaType
//                        .APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void judgeCalendarV1AvailabilityPostSuccess() throws Exception {
//        mockMvc.perform(post("/judgeCalendarRule/v1/availability").contentType(MediaType
//                        .APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void judgeCalendarV1AvailabilityPostFailure() throws Exception {
//        mockMvc.perform(post("/judgeCalendarRule/v1/availability").contentType(MediaType
//                        .APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void judgeCalendarV1CreatePostSuccess() throws Exception {
//        mockMvc.perform(post("/judgeCalendarRule/v1/create").contentType(MediaType
//                        .APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void judgeCalendarV1CreatePostFailure() throws Exception {
//        mockMvc.perform(post("/judgeCalendarRule/v1/create").contentType(MediaType
//                        .APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void judgeCalendarV1UpdatePostSuccess() throws Exception {
//        mockMvc.perform(post("/judgeCalendarRule/v1/update").contentType(MediaType
//                        .APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void judgeCalendarV1UpdatePostFailure() throws Exception {
//        mockMvc.perform(post("/judgeCalendarRule/v1/update").contentType(MediaType
//                        .APPLICATION_JSON_UTF8))
//                .andExpect(status().isBadRequest());
//    }

}