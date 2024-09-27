package digit.enrichment;

import digit.config.Configuration;
import digit.util.IdgenUtil;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import static digit.config.ServiceConstants.*;


@Component
@Slf4j
public class SummonsDeliveryEnrichment {

    private final Configuration config;

    private final IdgenUtil idgenUtil;

    @Autowired
    public SummonsDeliveryEnrichment(Configuration config, IdgenUtil idgenUtil) {
        this.config = config;
        this.idgenUtil = idgenUtil;
    }

    public SummonsDelivery generateAndEnrichSummonsDelivery(Task task, RequestInfo requestInfo) {
        TaskDetails taskDetails = task.getTaskDetails();
        AuditDetails auditDetails = createAuditDetails(requestInfo);
        String id = idgenUtil.getIdList(requestInfo, task.getTenantId(), config.getSummonsIdFormat(), null, 1).get(0);

        SummonsDetails summonDetails = taskDetails.getSummonDetails();
        NoticeDetails noticeDetails = taskDetails.getNoticeDetails();

        String docType = task.getTaskType().equals(NOTICE) ? noticeDetails.getDocType() : summonDetails.getDocType();
        String docSubType = task.getTaskType().equals(NOTICE) ? noticeDetails.getDocSubType() : summonDetails.getDocSubType();
        String partyType = task.getTaskType().equals(NOTICE) ? noticeDetails.getPartyType() : summonDetails.getPartyType();

        return SummonsDelivery.builder()
                .summonDeliveryId(id)
                .taskNumber(task.getTaskNumber())
                .caseId(task.getCnrNumber())
                .tenantId(config.getEgovStateTenantId())
                .docType(docType)
                .docSubType(docSubType)
                .partyType(partyType)
                .paymentFees(taskDetails.getDeliveryChannel().getPaymentFees())
                .paymentStatus(taskDetails.getDeliveryChannel().getPaymentStatus())
                .paymentTransactionId(taskDetails.getDeliveryChannel().getPaymentTransactionId())
                .channelName(ChannelName.fromString(taskDetails.getDeliveryChannel().getChannelName()))
                .deliveryRequestDate(LocalDate.now().toString())
                .auditDetails(auditDetails)
                .deliveryStatus(DeliveryStatus.NOT_UPDATED)
                .rowVersion(1)
                .build();
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

    public void enrichForUpdate(SummonsDelivery summonsDelivery, RequestInfo requestInfo) {
        Long currentTime = System.currentTimeMillis();
        summonsDelivery.getAuditDetails().setLastModifiedTime(currentTime);
        summonsDelivery.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUuid());
        summonsDelivery.setRowVersion(summonsDelivery.getRowVersion() + 1);
    }
}
