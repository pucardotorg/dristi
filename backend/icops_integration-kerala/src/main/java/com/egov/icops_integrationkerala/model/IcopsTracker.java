package com.egov.icops_integrationkerala.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IcopsTracker {
    private String processNumber;
    private String tenantId;
    private String taskNumber;
    private String taskType;
    private String fileStoreId;
    private Object taskDetails;
    private DeliveryStatus deliveryStatus;
    private String remarks;
    private AdditionalFields additionalDetails;
    private Integer rowVersion;
    private String bookingDate;
    private String receivedDate;
    private String acknowledgementId;
}
