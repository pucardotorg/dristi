package org.pucar.dristi.model;

import lombok.*;
import org.egov.common.contract.models.AuditDetails;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EPostTracker {
    private String processNumber;
    private String tenantId;
    private String taskNumber;
    private String fileStoreId;
    private String trackingNumber;
    private String address;
    private String pinCode;
    private DeliveryStatus deliveryStatus;
    private String remarks;
    private Object additionalDetails;
    private Integer rowVersion;
    private String bookingDate;
    private String receivedDate;
    private AuditDetails auditDetails;
}
