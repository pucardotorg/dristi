package digit.web.controllers;

import digit.service.DiaryEntryService;
import digit.util.ResponseInfoFactory;
import digit.web.models.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CaseDiaryApiControllerTest {

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @Mock
    private DiaryEntryService diaryEntryService;

    @InjectMocks
    private CaseDiaryApiController caseDiaryApiController;

    private CaseDiaryEntryRequest diaryEntryRequest;
    private CaseDiaryEntry diaryEntry;

    @BeforeEach
    void setUp() {
        diaryEntryRequest = new CaseDiaryEntryRequest();
        diaryEntryRequest.setRequestInfo(new RequestInfo());
        diaryEntry = new CaseDiaryEntry();
        diaryEntry.setId(UUID.fromString(UUID.randomUUID().toString()));
    }

    @Test
    void addDiaryEntrySuccess() {
        when(diaryEntryService.addDiaryEntry(diaryEntryRequest)).thenReturn(diaryEntry);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), eq(true))).thenReturn(new ResponseInfo());

        ResponseEntity<CaseDiaryEntryResponse> response = caseDiaryApiController.addDiaryEntry(diaryEntryRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getDiaryEntry());
    }

    @Test
    void addDiaryEntryFailure() {
        when(diaryEntryService.addDiaryEntry(diaryEntryRequest)).thenThrow(new RuntimeException("Error adding entry"));

        assertThrows(RuntimeException.class, () -> {
            caseDiaryApiController.addDiaryEntry(diaryEntryRequest);
        });
    }

    @Test
    void updateDiaryEntrySuccess() {
        when(diaryEntryService.updateDiaryEntry(diaryEntryRequest)).thenReturn(diaryEntry);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), eq(true))).thenReturn(new ResponseInfo());

        ResponseEntity<CaseDiaryEntryResponse> response = caseDiaryApiController.caseDiaryEntryV1UpdatePost(diaryEntryRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getDiaryEntry());
    }

    @Test
    void updateDiaryEntryFailure() {
        when(diaryEntryService.updateDiaryEntry(diaryEntryRequest)).thenThrow(new RuntimeException("Error updating entry"));

        assertThrows(RuntimeException.class, () -> {
            caseDiaryApiController.caseDiaryEntryV1UpdatePost(diaryEntryRequest);
        });
    }

    @Test
    void searchDiaryEntrySuccess() {
        List<CaseDiaryEntry> entries = Collections.singletonList(diaryEntry);
        when(diaryEntryService.searchDiaryEntries(any())).thenReturn(entries);
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), eq(true))).thenReturn(new ResponseInfo());

        CaseDiarySearchRequest searchRequest = new CaseDiarySearchRequest();
        searchRequest.setRequestInfo(new RequestInfo());
        searchRequest.setPagination(new Pagination());

        ResponseEntity<CaseDiaryEntryListResponse> response = caseDiaryApiController.searchDiaryEntry(searchRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getEntries().size());
    }

    @Test
    void searchDiaryEntryEmptyResult() {
        when(diaryEntryService.searchDiaryEntries(any())).thenReturn(Collections.emptyList());
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(), eq(true))).thenReturn(new ResponseInfo());

        CaseDiarySearchRequest searchRequest = new CaseDiarySearchRequest();
        searchRequest.setRequestInfo(new RequestInfo());
        searchRequest.setPagination(new Pagination());

        ResponseEntity<CaseDiaryEntryListResponse> response = caseDiaryApiController.searchDiaryEntry(searchRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getEntries().isEmpty());
    }

}
