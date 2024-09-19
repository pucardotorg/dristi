package digit.service;

import digit.config.Configuration;
import digit.enrichment.SummonsDeliveryEnrichment;
import digit.kafka.Producer;
import digit.repository.SummonsRepository;
import digit.util.*;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static digit.config.ServiceConstants.*;

@Service
@Slf4j
public class SummonsService {


    private final PdfServiceUtil pdfServiceUtil;

    private final Configuration config;

    private final FileStorageUtil fileStorageUtil;

    private final SummonsRepository summonsRepository;

    private final Producer producer;

    private final SummonsDeliveryEnrichment summonsDeliveryEnrichment;

    private final ExternalChannelUtil externalChannelUtil;

    private final TaskUtil taskUtil;

    private final CaseManagementUtil caseManagementUtil;

    @Autowired
    public SummonsService(PdfServiceUtil pdfServiceUtil, Configuration config, Producer producer,
                          FileStorageUtil fileStorageUtil, SummonsRepository summonsRepository,
                          SummonsDeliveryEnrichment summonsDeliveryEnrichment, ExternalChannelUtil externalChannelUtil,
                          TaskUtil taskUtil, CaseManagementUtil caseManagementUtil) {
        this.pdfServiceUtil = pdfServiceUtil;
        this.config = config;
        this.producer = producer;
        this.fileStorageUtil = fileStorageUtil;
        this.summonsRepository = summonsRepository;
        this.summonsDeliveryEnrichment = summonsDeliveryEnrichment;
        this.externalChannelUtil = externalChannelUtil;
        this.taskUtil = taskUtil;
        this.caseManagementUtil = caseManagementUtil;
    }

    public TaskResponse generateSummonsDocument(TaskRequest taskRequest) {
        String taskType = taskRequest.getTask().getTaskType();
        String pdfTemplateKey = getPdfTemplateKey(taskType);
        String moduleName = config.getBffServiceSummonsModule();

        // generate credentials
        caseManagementUtil.generateVcForTask(taskRequest, moduleName);

        String fileStoreId = caseManagementUtil.getFileStoreIdFromBffService(taskRequest, pdfTemplateKey, moduleName);

        Document document = createDocument(fileStoreId);
        taskRequest.getTask().addDocumentsItem(document);

        return taskUtil.callUploadDocumentTask(taskRequest);
    }

    public SummonsDelivery sendSummonsViaChannels(TaskRequest request) {
        SummonsDelivery summonsDelivery = summonsDeliveryEnrichment.generateAndEnrichSummonsDelivery(request.getTask(), request.getRequestInfo());

        ChannelMessage channelMessage = externalChannelUtil.sendSummonsByDeliveryChannel(request, summonsDelivery);

        if (channelMessage.getAcknowledgementStatus().equalsIgnoreCase("success")) {
            summonsDelivery.setIsAcceptedByChannel(Boolean.TRUE);
            if (summonsDelivery.getChannelName() == ChannelName.SMS || summonsDelivery.getChannelName() == ChannelName.EMAIL) {
                summonsDelivery.setDeliveryStatus(DeliveryStatus.DELIVERED);
            } else {
                summonsDelivery.setDeliveryStatus(DeliveryStatus.NOT_UPDATED);
            }
            summonsDelivery.setChannelAcknowledgementId(channelMessage.getAcknowledgeUniqueNumber());
        }
        SummonsRequest summonsRequest = createSummonsRequest(request.getRequestInfo(), summonsDelivery);

        producer.push(config.getInsertSummonsTopic(), summonsRequest);
        return summonsDelivery;
    }

    public List<SummonsDelivery> getSummonsDelivery(SummonsDeliverySearchRequest request) {
        return getSummonsDeliveryFromSearchCriteria(request.getSearchCriteria());
    }

    public ChannelMessage updateSummonsDeliveryStatus(UpdateSummonsRequest request) {
        SummonsDelivery summonsDelivery = fetchSummonsDelivery(request);

        enrichAndUpdateSummonsDelivery(summonsDelivery, request);

        SummonsRequest summonsRequest = createSummonsRequest(request.getRequestInfo(), summonsDelivery);
        producer.push(config.getUpdateSummonsTopic(), summonsRequest);

        return createChannelMessage(summonsDelivery);
    }

    private void enrichAndUpdateSummonsDelivery(SummonsDelivery summonsDelivery, UpdateSummonsRequest request) {
        summonsDeliveryEnrichment.enrichForUpdate(summonsDelivery, request.getRequestInfo());
        ChannelReport channelReport = request.getChannelReport();
        summonsDelivery.setDeliveryStatus(channelReport.getDeliveryStatus());
        summonsDelivery.setAdditionalFields(channelReport.getAdditionalFields());
    }

    public void updateTaskStatus(SummonsRequest request) {
        TaskCriteria taskCriteria = TaskCriteria.builder().taskNumber(request.getSummonsDelivery().getTaskNumber()).build();
        TaskSearchRequest searchRequest = TaskSearchRequest.builder()
                .requestInfo(request.getRequestInfo()).criteria(taskCriteria).build();
        TaskListResponse taskListResponse = taskUtil.callSearchTask(searchRequest);
        Task task = taskListResponse.getList().get(0);
        Workflow workflow = null;
        if (task.getTaskType().equalsIgnoreCase(SUMMON)) {
            if (request.getSummonsDelivery().getDeliveryStatus().equals(DeliveryStatus.DELIVERED)) {
                workflow = Workflow.builder().action("SERVE").build();
            } else {
                workflow = Workflow.builder().action("REISSUE").build();
            }
        } else if (task.getTaskType().equalsIgnoreCase(WARRANT)) {
            if (request.getSummonsDelivery().getDeliveryStatus().equals(DeliveryStatus.EXECUTED)) {
                workflow = Workflow.builder().action("DELIVERED").build();
            } else {
                workflow = Workflow.builder().action("NOT DELIVERED").build();
            }
        }
        task.setWorkflow(workflow);
        TaskRequest taskRequest = TaskRequest.builder()
                .requestInfo(request.getRequestInfo()).task(task).build();
        taskUtil.callUpdateTask(taskRequest);
    }

    private String getPdfTemplateKey(String taskType) {
        return switch (taskType) {
            case SUMMON -> config.getSummonsPdfTemplateKey();
            case WARRANT -> config.getNonBailableWarrantPdfTemplateKey();
            case NOTICE -> config.getTaskNoticePdfTemplateKey();
            default -> throw new CustomException("INVALID_TASK_TYPE", "Task Type must be valid. Provided: " + taskType);
        };
    }

    private SummonsDelivery fetchSummonsDelivery(UpdateSummonsRequest request) {
        SummonsDeliverySearchCriteria searchCriteria = SummonsDeliverySearchCriteria.builder()
                .taskNumber(request.getChannelReport().getTaskNumber())
                .build();
        Optional<SummonsDelivery> optionalSummons = getSummonsDeliveryFromSearchCriteria(searchCriteria).stream().findFirst();
        if (optionalSummons.isEmpty()) {
            throw new CustomException("SUMMONS_UPDATE_ERROR", "Invalid summons delivery id was provided");
        }
        return optionalSummons.get();
    }

    private List<SummonsDelivery> getSummonsDeliveryFromSearchCriteria(SummonsDeliverySearchCriteria searchCriteria) {
        return summonsRepository.getSummons(searchCriteria);
    }

    private Document createDocument(String fileStoreId) {
        Field field = Field.builder().key("FILE_CATEGORY").value("GENERATE_SUMMONS_DOCUMENT").build();
        AdditionalFields additionalFields = AdditionalFields.builder().fields(Collections.singletonList(field)).build();
        return Document.builder()
                .fileStore(fileStoreId)
                .documentType("UNSIGNED")
                .additionalDetails(additionalFields)
                .build();
    }

    private SummonsRequest createSummonsRequest(RequestInfo requestInfo, SummonsDelivery summonsDelivery) {
        return SummonsRequest.builder()
                .requestInfo(requestInfo)
                .summonsDelivery(summonsDelivery)
                .build();
    }

    private ChannelMessage createChannelMessage(SummonsDelivery summonsDelivery) {
        return ChannelMessage.builder()
                .acknowledgeUniqueNumber(summonsDelivery.getSummonDeliveryId())
                .acknowledgementStatus("SUCCESS")
                .build();
    }
}
