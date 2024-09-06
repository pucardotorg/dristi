package digit.web.controllers;

import digit.service.CauseListService;
import digit.util.ResponseInfoFactory;
import digit.web.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CauseListApiControllerTest {

    @Mock
    private CauseListService causeListService;


    @InjectMocks
    private CauseListApiController causeListApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testViewCauseList() {
        CauseListSearchRequest searchRequest = new CauseListSearchRequest();
        CauseList causeList = new CauseList();
        List<CauseList> causeLists = Collections.singletonList(causeList);
        when(causeListService.viewCauseListForTomorrow(any(CauseListSearchRequest.class))).thenReturn(causeLists);

        ResponseEntity<CauseListResponse> responseEntity = causeListApiController.viewCauseList(searchRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().getCauseList().size());
        assertEquals(causeList, responseEntity.getBody().getCauseList().get(0));
    }

    @Test
    void testDownloadCauseList() {
        CauseListSearchRequest searchRequest = new CauseListSearchRequest();
        ByteArrayResource resource = new ByteArrayResource(new byte[]{});
        when(causeListService.downloadCauseListForTomorrow(any(CauseListSearchRequest.class))).thenReturn(resource);

        ResponseEntity<Object> responseEntity = causeListApiController.downloadCauseList(searchRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_PDF, responseEntity.getHeaders().getContentType());
        assertEquals("attachment; filename=\"causelist" + LocalDate.now().plusDays(1).toString() + ".pdf\"", responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals(resource, responseEntity.getBody());
    }
}
