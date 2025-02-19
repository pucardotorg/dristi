package digit.web.controllers;


import digit.service.ReScheduleHearingService;
import digit.web.models.*;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReScheduleHearingControllerTest {

    @Mock
    private ReScheduleHearingService reScheduleHearingService;

    @InjectMocks
    private ReScheduleHearingController reScheduleHearingController;

    @Test
    public void testReScheduleHearing() {
        ReScheduleHearingRequest request = new ReScheduleHearingRequest();
        ReScheduleHearing reScheduleHearing = ReScheduleHearing.builder().judgeId("judgeId").hearingBookingId("HR001").build();
        List<ReScheduleHearing> reScheduleHearings = List.of(reScheduleHearing);
        request.setReScheduleHearing(reScheduleHearings);
        request.setRequestInfo(new RequestInfo());
        when(reScheduleHearingService.create(request)).thenReturn(reScheduleHearings);

        ResponseEntity<ReScheduleHearingResponse> responseEntity = reScheduleHearingController.reScheduleHearing(request);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals(reScheduleHearings.size(), responseEntity.getBody().getReScheduleHearings().size());
    }


    @Test
    void testSearchRescheduleHearing() {
        ReScheduleHearingReqSearchRequest request = new ReScheduleHearingReqSearchRequest();
        ReScheduleHearing reScheduleHearing = new ReScheduleHearing();
        when(reScheduleHearingService.search(request, 10, 0)).thenReturn(Collections.singletonList(reScheduleHearing));

        ResponseEntity<ReScheduleHearingResponse> responseEntity = reScheduleHearingController.searchRescheduleHearing(request, 10, 0);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().getReScheduleHearings().size());
    }

    @Test
    void testBulkRescheduleHearing() {
        BulkRescheduleRequest request = new BulkRescheduleRequest();
        ScheduleHearing reScheduleHearing = new ScheduleHearing();
        when(reScheduleHearingService.bulkReschedule(request)).thenReturn(Collections.singletonList(reScheduleHearing));

        ResponseEntity<BulkRescheduleResponse> responseEntity = reScheduleHearingController.bulkRescheduleHearing(request);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().getReScheduleHearings().size());
    }
}