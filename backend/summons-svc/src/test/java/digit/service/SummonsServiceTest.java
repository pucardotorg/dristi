package digit.service;

import digit.config.Configuration;
import digit.enrichment.SummonsDeliveryEnrichment;
import digit.kafka.Producer;
import digit.repository.SummonsRepository;
import digit.util.ExternalChannelUtil;
import digit.util.FileStorageUtil;
import digit.util.PdfServiceUtil;
import digit.util.TaskUtil;
import digit.web.models.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static digit.config.ServiceConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SummonsServiceTest {

    @Mock
    private PdfServiceUtil pdfServiceUtil;
    @Mock
    private Configuration config;
    @Mock
    private FileStorageUtil fileStorageUtil;
    @Mock
    private SummonsRepository summonsRepository;
    @Mock
    private Producer producer;
    @Mock
    private SummonsDeliveryEnrichment summonsDeliveryEnrichment;
    @Mock
    private ExternalChannelUtil externalChannelUtil;
    @Mock
    private TaskUtil taskUtil;
    @Mock
    private SummonsDeliverySearchRequest request;
    @Mock
    private TaskResponse taskResponse;

    private final List<Task> tasks = new ArrayList<>();
    private TaskListResponse taskListResponse;

    @InjectMocks
    private SummonsService summonsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Task task = createTask();
        tasks.add(task);
        taskListResponse = TaskListResponse.builder().list(tasks).build();
    }

    private Task createTask() {
        Task task = new Task();
        task.setTaskType("SUMMONS");
        SummonsDetails summonsDetails = SummonsDetails.builder().docSubType(ACCUSED).build();
        TaskDetails taskDetails = TaskDetails.builder().summonDetails(summonsDetails).build();
        task.setTaskDetails(taskDetails);
        return task;
    }

//    @Test
//    void generateSummonsDocument_Summon() {
//        TaskRequest taskRequest = createTaskRequest(SUMMON);
//        when(config.getEgovStateTenantId()).thenReturn("state");
//        when(config.getSummonsPdfTemplateKey()).thenReturn("summons_template");
//        when(pdfServiceUtil.generatePdfFromPdfService(any(), anyString(), anyString()))
//                .thenReturn(new ByteArrayResource("pdf".getBytes()));
//        when(fileStorageUtil.saveDocumentToFileStore(any())).thenReturn("fileStoreId");
//        when(taskUtil.callUploadDocumentTask(any())).thenReturn(taskResponse);
//
//        TaskResponse response = summonsService.generateSummonsDocument(taskRequest);
//
//        assertNotNull(response);
//        verify(pdfServiceUtil).generatePdfFromPdfService(eq(taskRequest), eq("state"), eq("summons_template"));
//        verify(fileStorageUtil).saveDocumentToFileStore(any());
//    }
//
//    @Test
//    void generateSummonsDocument_Warrant() {
//        TaskRequest taskRequest = createTaskRequest(WARRANT);
//        when(config.getEgovStateTenantId()).thenReturn("state");
//        when(config.getBailableWarrantPdfTemplateKey()).thenReturn("warrant_template");
//        when(pdfServiceUtil.generatePdfFromPdfService(any(), anyString(), anyString()))
//                .thenReturn(new ByteArrayResource("pdf".getBytes()));
//        when(fileStorageUtil.saveDocumentToFileStore(any())).thenReturn("fileStoreId");
//        when(taskUtil.callUploadDocumentTask(any())).thenReturn(new TaskResponse());
//
//        TaskResponse response = summonsService.generateSummonsDocument(taskRequest);
//
//        assertNotNull(response);
//    }
//
//    @Test
//    void generateSummonsDocument_Bail() {
//        TaskRequest taskRequest = createTaskRequest(WARRANT);
//        when(config.getEgovStateTenantId()).thenReturn("state");
//        when(config.getBailableWarrantPdfTemplateKey()).thenReturn("bail_template");
//        when(pdfServiceUtil.generatePdfFromPdfService(any(), anyString(), anyString()))
//                .thenReturn(new ByteArrayResource("pdf".getBytes()));
//        when(fileStorageUtil.saveDocumentToFileStore(any())).thenReturn("fileStoreId");
//        when(taskUtil.callUploadDocumentTask(any())).thenReturn(new TaskResponse());
//
//        TaskResponse response = summonsService.generateSummonsDocument(taskRequest);
//
//        assertNotNull(response);
//    }

    @Test
    void generateSummonsDocument_InvalidTaskType() {
        TaskRequest taskRequest = createTaskRequest("invalid");

        assertThrows(CustomException.class, () -> summonsService.generateSummonsDocument(taskRequest));
    }

    @Test
    void sendSummonsViaChannels_Success() {
        TaskRequest request = createTaskRequest("SUMMONS");
        SummonsDelivery summonsDelivery = new SummonsDelivery();
        ChannelMessage channelMessage = new ChannelMessage();
        channelMessage.setAcknowledgementStatus("SUCCESS");
        channelMessage.setAcknowledgeUniqueNumber("123");
        ByteArrayResource byteArrayResource = new ByteArrayResource(new byte[]{});

        when(summonsDeliveryEnrichment.generateAndEnrichSummonsDelivery(any(), any())).thenReturn(summonsDelivery);
        when(externalChannelUtil.sendSummonsByDeliveryChannel(any(), any())).thenReturn(channelMessage);
        when(pdfServiceUtil.generatePdfFromPdfService(any(),any(),any(),anyBoolean())).thenReturn(byteArrayResource);
        when(fileStorageUtil.saveDocumentToFileStore(any())).thenReturn("kdshfj");
        when(taskUtil.callUpdateTask(any())).thenReturn(taskResponse);
        when(taskUtil.callSearchTask(any())).thenReturn(taskListResponse);

        SummonsDelivery result = summonsService.sendSummonsViaChannels(request);

        assertNotNull(result);
        assertTrue(result.getIsAcceptedByChannel());
        assertEquals("123", result.getChannelAcknowledgementId());
    }

    @Test
    void sendSummonsViaChannels_SMSChannel() {
        TaskRequest request = createTaskRequest("SUMMONS");
        SummonsDelivery summonsDelivery = new SummonsDelivery();
        summonsDelivery.setChannelName(ChannelName.SMS);
        ChannelMessage channelMessage = new ChannelMessage();
        channelMessage.setAcknowledgementStatus("SUCCESS");
        ByteArrayResource byteArrayResource = new ByteArrayResource(new byte[]{});

        when(summonsDeliveryEnrichment.generateAndEnrichSummonsDelivery(any(), any())).thenReturn(summonsDelivery);
        when(externalChannelUtil.sendSummonsByDeliveryChannel(any(), any())).thenReturn(channelMessage);
        when(pdfServiceUtil.generatePdfFromPdfService(any(),any(),any(),anyBoolean())).thenReturn(byteArrayResource);
        when(fileStorageUtil.saveDocumentToFileStore(any())).thenReturn("kdshfj");
        when(taskUtil.callUpdateTask(any())).thenReturn(taskResponse);
        when(taskUtil.callSearchTask(any())).thenReturn(taskListResponse);

        SummonsDelivery result = summonsService.sendSummonsViaChannels(request);

        assertEquals(DeliveryStatus.DELIVERED, result.getDeliveryStatus());
    }

    @Test
    void getSummonsDelivery() {
        request.setSearchCriteria(new SummonsDeliverySearchCriteria());
        when(summonsRepository.getSummons(any())).thenReturn(Collections.singletonList(new SummonsDelivery()));

        List<SummonsDelivery> result = summonsService.getSummonsDelivery(request);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void updateSummonsDeliveryStatus_Success() {
        UpdateSummonsRequest request = new UpdateSummonsRequest();
        request.setRequestInfo(new RequestInfo());
        ChannelReport channelReport = new ChannelReport();
        channelReport.setProcessNumber("123");
        channelReport.setDeliveryStatus(DeliveryStatus.DELIVERED);
        request.setChannelReport(channelReport);

        SummonsDelivery summonsDelivery = new SummonsDelivery();
        summonsDelivery.setSummonDeliveryId("123");

        when(summonsRepository.getSummons(any())).thenReturn(Collections.singletonList(summonsDelivery));

        ChannelMessage result = summonsService.updateSummonsDeliveryStatus(request);

        assertNotNull(result);
        assertEquals("SUCCESS", result.getAcknowledgementStatus());
    }

    @Test
    void updateSummonsDeliveryStatus_InvalidSummonsId() {
        UpdateSummonsRequest request = new UpdateSummonsRequest();
        ChannelReport channelReport = new ChannelReport();
        channelReport.setProcessNumber("invalid");
        request.setChannelReport(channelReport);

        when(summonsRepository.getSummons(any())).thenReturn(Collections.emptyList());

        assertThrows(CustomException.class, () -> summonsService.updateSummonsDeliveryStatus(request));
    }

    @Test
    void updateTaskStatus_Summon() {

        SummonsRequest request = new SummonsRequest();

        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setRoles(new ArrayList<>());

        requestInfo.setUserInfo(userInfo);
        request.setRequestInfo(requestInfo);

        SummonsDelivery summonsDelivery = new SummonsDelivery();
        summonsDelivery.setTaskNumber("123");
        request.setSummonsDelivery(summonsDelivery);

        Task task = new Task();
        task.setTaskType("SUMMON");
        TaskListResponse taskListResponse = new TaskListResponse();
        taskListResponse.setList(Collections.singletonList(task));

        when(taskUtil.callSearchTask(any())).thenReturn(taskListResponse);

        // Act
        summonsService.updateTaskStatus(request);

        // Assert
        // Here you can add assertions to check if the workflow was set correctly
        // and that the role was added to the requestInfo.userInfo.roles
        assertEquals(1, request.getRequestInfo().getUserInfo().getRoles().size());
        assertEquals(config.getSystemAdmin(), request.getRequestInfo().getUserInfo().getRoles().get(0).getCode());
    }


//    @Test
//    void updateTaskStatus_Warrant() {
//        SummonsRequest request = new SummonsRequest();
//        request.setRequestInfo(new RequestInfo());
//        SummonsDelivery summonsDelivery = new SummonsDelivery();
//        summonsDelivery.setTaskNumber("123");
//        request.setSummonsDelivery(summonsDelivery);
//
//        Task task = new Task();
//        task.setTaskType("warrant");
//        TaskListResponse taskListResponse = new TaskListResponse();
//        taskListResponse.setList(Collections.singletonList(task));
//
//        when(taskUtil.callSearchTask(any())).thenReturn(taskListResponse);
//
//        summonsService.updateTaskStatus(request);
//
//        verify(taskUtil).callUpdateTask(argThat(req ->
//                "DELIVERED".equals(req.getTask().getWorkflow().getAction())
//        ));
//    }

    private TaskRequest createTaskRequest(String taskType) {
        Task task = new Task();
        task.setTaskType(taskType);
        SummonsDetails summonsDetails = SummonsDetails.builder().docSubType(ACCUSED).build();
        TaskDetails taskDetails = TaskDetails.builder().summonDetails(summonsDetails).build();
        task.setTaskDetails(taskDetails);
        return TaskRequest.builder().task(task).build();
    }
}