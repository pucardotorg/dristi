package org.pucar.dristi.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.EPostConfiguration;
import org.pucar.dristi.model.*;
import org.pucar.dristi.repository.EPostRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class EpostUtilTest {

    @Mock
    private IdgenUtil idgenUtil;

    @Mock
    private EPostConfiguration config;

    @Mock
    private EPostRepository ePostRepository;

    @Mock
    private User user;

    @InjectMocks
    private EpostUtil epostUtil;

    @Mock
    private EPostRequest ePostRequest;

    @Mock
    private EPostTracker ePostTracker;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Initialization for any common setup can be done here
    }

    @Test
    void testCreatePostTrackerBody() throws JsonProcessingException {
        // Arrange
        TaskRequest request = mock(TaskRequest.class);
        RequestInfo requestInfo = mock(RequestInfo.class);
        Task task = mock(Task.class);
        Document document = new Document();
        document.setFileStore("fileStoreId");
        document.setDocumentType("SIGNED_TASK_DOCUMENT");
        RespondentDetails respondentDetails = mock(RespondentDetails.class);
        TaskDetails taskDetails = mock(TaskDetails.class);

        when(request.getRequestInfo()).thenReturn(requestInfo);
        when(request.getTask()).thenReturn(task);
        when(task.getTaskDetails()).thenReturn(taskDetails);
        when(taskDetails.getRespondentDetails()).thenReturn(respondentDetails);
        //when(respondentDetails.getAddress().getPinCode()).thenReturn("123456");
        when(respondentDetails.getAddress()).thenReturn(new Address());
        when(task.getDocuments()).thenReturn(Collections.singletonList(document));

        when(idgenUtil.getIdList(any(), any(), any(), any(), anyInt())).thenReturn(Collections.singletonList("PN123"));
        when(config.getEgovStateTenantId()).thenReturn("tenantId");
        when(requestInfo.getUserInfo()).thenReturn(user);

        // Act
        EPostTracker ePostTrackerResult = epostUtil.createPostTrackerBody(request);

        // Assert
        assertNotNull(ePostTracker);
        assertEquals("PN123", ePostTrackerResult.getProcessNumber());
        assertEquals("tenantId", ePostTrackerResult.getTenantId());
//        assertEquals("fileStoreId", ePostTrackerResult.getFileStoreId());
        //assertEquals("123456", ePostTrackerResult.getPinCode());
        assertEquals(DeliveryStatus.NOT_UPDATED, ePostTrackerResult.getDeliveryStatus());
        assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), ePostTrackerResult.getBookingDate()); // Current date comparison
    }

    @Test
    void testUpdateEPostTracker() {
        // Arrange
        RequestInfo requestInfo = mock(RequestInfo.class);
        AuditDetails auditDetails = mock(AuditDetails.class);
        User userInfo = mock(User.class);

        when(ePostRequest.getEPostTracker()).thenReturn(ePostTracker);
        when(ePostRequest.getRequestInfo()).thenReturn(requestInfo);
        when(ePostTracker.getAuditDetails()).thenReturn(auditDetails);
        when(requestInfo.getUserInfo()).thenReturn(userInfo);
        when(userInfo.getUuid()).thenReturn("userId");

        List<EPostTracker> ePostTrackers = Collections.singletonList(ePostTracker);
        when(ePostRepository.getEPostTrackerList(any(), anyInt(), anyInt())).thenReturn(ePostTrackers);

        when(ePostTracker.getProcessNumber()).thenReturn("PN123");
        when(ePostTracker.getRowVersion()).thenReturn(1);

        when(ePostRequest.getEPostTracker().getTrackingNumber()).thenReturn("TN123");
        when(ePostRequest.getEPostTracker().getDeliveryStatus()).thenReturn(DeliveryStatus.DELIVERED);
        when(ePostRequest.getEPostTracker().getRemarks()).thenReturn("Remarks");
        when(ePostRequest.getEPostTracker().getTaskNumber()).thenReturn("TaskNumber");
        when(ePostRequest.getEPostTracker().getReceivedDate()).thenReturn("2024-08-07");
        when(ePostRequest.getRequestInfo().getUserInfo().getUuid()).thenReturn("hdjs");

        // Act
        EPostTracker result = epostUtil.updateEPostTracker(ePostRequest);

        // Assert
        assertNotNull(result);
        assertEquals("TN123", result.getTrackingNumber());
        assertEquals(DeliveryStatus.DELIVERED, result.getDeliveryStatus());
        assertEquals("Remarks", result.getRemarks());
        assertEquals("TaskNumber", result.getTaskNumber());
        assertEquals("2024-08-07", result.getReceivedDate());
        assertEquals(1, result.getRowVersion());  // Expecting incremented row version
    }
}

