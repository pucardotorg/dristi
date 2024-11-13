package digit.web.controllers;

import digit.service.RescheduleRequestOptOutService;
import digit.web.models.OptOut;
import digit.web.models.OptOutRequest;
import digit.web.models.OptOutResponse;
import digit.web.models.OptOutSearchRequest;
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
public class RescheduleRequestOptOutControllerTest {

    @Mock
    private RescheduleRequestOptOutService optOutService;

    @InjectMocks
    private RescheduleRequestOptOutApiController optOutApiController;

    @Test
    void testOptOutDates() {
        OptOutRequest request = new OptOutRequest();
        OptOut optOut = new OptOut();
        when(optOutService.create(request)).thenReturn(optOut);

        ResponseEntity<OptOutResponse> responseEntity = optOutApiController.optOutDates(request);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    void testSearchOptOut() {
        OptOutSearchRequest request = new OptOutSearchRequest();
        OptOut optOut = new OptOut();
        when(optOutService.search(request, 10, 0)).thenReturn(Collections.singletonList(optOut));

        ResponseEntity<List<OptOut>> responseEntity = optOutApiController.searchOptOut(request, 10, 0);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

}