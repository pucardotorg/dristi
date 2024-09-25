package org.pucar.dristi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.EPostConfiguration;
import org.pucar.dristi.model.*;
import org.pucar.dristi.repository.EPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.pucar.dristi.config.ServiceConstants.EPOST_TRACKER_ERROR;
import static org.pucar.dristi.config.ServiceConstants.INVALID_EPOST_TRACKER_FIELD;

@Component
public class EpostUtil {

    private final IdgenUtil idgenUtil;

    private final EPostConfiguration config;

    private final EPostRepository ePostRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public EpostUtil(IdgenUtil idgenUtil, EPostConfiguration config, EPostRepository ePostRepository, ObjectMapper objectMapper) {
        this.idgenUtil = idgenUtil;
        this.config = config;
        this.ePostRepository = ePostRepository;
        this.objectMapper = objectMapper;
    }

    public EPostTracker createPostTrackerBody(TaskRequest request) throws JsonProcessingException {
        String processNumber = idgenUtil.getIdList(request.getRequestInfo(), config.getEgovStateTenantId(),
                config.getIdName(),null,1).get(0);
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return EPostTracker.builder()
                .processNumber(processNumber)
                .tenantId(config.getEgovStateTenantId())
                .taskNumber(request.getTask().getTaskNumber())
                .fileStoreId(getFileStore(request))
                .address(request.getTask().getTaskDetails().getRespondentDetails().getAddress().toString())
                .pinCode(request.getTask().getTaskDetails().getRespondentDetails().getAddress().getPinCode())
                .deliveryStatus(DeliveryStatus.NOT_UPDATED)
                .additionalDetails(request.getTask().getAdditionalDetails())
                .rowVersion(0)
                .bookingDate(currentDate)
                .auditDetails(createAuditDetails(request.getRequestInfo()))
                .build();
    }

    private String getFileStore(TaskRequest request) {
        if(request.getTask().getDocuments() == null || request.getTask().getDocuments().isEmpty() || request.getTask().getDocuments().get(0).getFileStore() == null){
            return null;
        }
        return request.getTask().getDocuments().get(0).getFileStore();
    }

    public EPostTracker updateEPostTracker(EPostRequest ePostRequest) {
        Pagination pagination = Pagination.builder().build();
        EPostTrackerSearchCriteria searchCriteria = EPostTrackerSearchCriteria.builder()
                .processNumber(ePostRequest.getEPostTracker().getProcessNumber()).pagination(pagination).build();
        List<EPostTracker> ePostTrackers = ePostRepository.getEPostTrackerList(searchCriteria,5,0);
        if (ePostTrackers.size() != 1) {
            throw new CustomException(EPOST_TRACKER_ERROR,INVALID_EPOST_TRACKER_FIELD + ePostRequest.getEPostTracker().getProcessNumber());
        }
        EPostTracker ePostTracker = ePostTrackers.get(0);

        Long currentTime = System.currentTimeMillis();
        ePostTracker.getAuditDetails().setLastModifiedTime(currentTime);
        ePostTracker.getAuditDetails().setLastModifiedBy(ePostRequest.getRequestInfo().getUserInfo().getUuid());
        ePostTracker.setRowVersion(ePostTracker.getRowVersion() + 1);

        ePostTracker.setTrackingNumber(ePostRequest.getEPostTracker().getTrackingNumber());
        ePostTracker.setDeliveryStatus(ePostRequest.getEPostTracker().getDeliveryStatus());
        ePostTracker.setRemarks(ePostRequest.getEPostTracker().getRemarks());
        ePostTracker.setTaskNumber(ePostRequest.getEPostTracker().getTaskNumber());
        ePostTracker.setReceivedDate(ePostRequest.getEPostTracker().getReceivedDate());

        return ePostTracker;

    }

    private AuditDetails createAuditDetails(RequestInfo requestInfo) {
        long currentTime = System.currentTimeMillis();
        String userId = requestInfo.getUserInfo().getUuid();
        return AuditDetails.builder()
                .createdBy(userId)
                .createdTime(currentTime)
                .lastModifiedBy(userId)
                .lastModifiedTime(currentTime)
                .build();
    }

}
