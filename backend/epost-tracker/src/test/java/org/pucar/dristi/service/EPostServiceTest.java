package org.pucar.dristi.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.model.*;
import org.pucar.dristi.repository.EPostRepository;
import org.pucar.dristi.util.EpostUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EPostServiceTest {

    @Mock
    private EPostRepository ePostRepository;

    @Mock
    private EpostUtil epostUtil;

    @Mock
    private Producer producer;

    @InjectMocks
    private EPostService ePostService;

    @Test
    void testSendEPost() throws JsonProcessingException {
        // Arrange
        TaskRequest taskRequest = TaskRequest.builder()
                .requestInfo(new RequestInfo())
                .task(Task.builder().build())
                .build();

        EPostTracker ePostTracker = EPostTracker.builder().processNumber("P12345").build();
        when(epostUtil.createPostTrackerBody(taskRequest)).thenReturn(ePostTracker);

        EPostRequest ePostRequest = EPostRequest.builder()
                .requestInfo(taskRequest.getRequestInfo())
                .ePostTracker(ePostTracker)
                .build();
        doNothing().when(producer).push("save-epost-tracker", ePostRequest);

        // Act
        ChannelMessage result = ePostService.sendEPost(taskRequest);

        // Assert
        assertNotNull(result);
        assertEquals("P12345", result.getProcessNumber());
        verify(producer).push("save-epost-tracker", ePostRequest);
    }

    @Test
    void testGetEPost() {
        // Arrange
        EPostTrackerSearchRequest searchRequest = new EPostTrackerSearchRequest();
        EPostResponse ePostResponse = new EPostResponse();
        when(ePostRepository.getEPostTrackerResponse(searchRequest.getEPostTrackerSearchCriteria(), 10, 0))
                .thenReturn(ePostResponse);

        // Act
        EPostResponse result = ePostService.getEPost(searchRequest, 10, 0);

        // Assert
        assertNotNull(result);
        assertEquals(ePostResponse, result);
        verify(ePostRepository).getEPostTrackerResponse(searchRequest.getEPostTrackerSearchCriteria(), 10, 0);
    }

    @Test
    void testUpdateEPost() {
        // Arrange
        EPostRequest ePostRequest = EPostRequest.builder()
                .requestInfo(new RequestInfo())
                .ePostTracker(EPostTracker.builder().build())
                .build();

        EPostTracker updatedEPostTracker = EPostTracker.builder().build();
        when(epostUtil.updateEPostTracker(ePostRequest)).thenReturn(updatedEPostTracker);

        EPostRequest postRequest = EPostRequest.builder()
                .requestInfo(ePostRequest.getRequestInfo())
                .ePostTracker(updatedEPostTracker)
                .build();
        doNothing().when(producer).push("update-epost-tracker", postRequest);

        // Act
        EPostTracker result = ePostService.updateEPost(ePostRequest);

        // Assert
        assertNotNull(result);
        assertEquals(updatedEPostTracker, result);
        verify(producer).push("update-epost-tracker", postRequest);
    }
}

