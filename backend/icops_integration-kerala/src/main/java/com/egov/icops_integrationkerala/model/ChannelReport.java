package com.egov.icops_integrationkerala.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelReport {

    @JsonProperty("taskNumber")
    private String taskNumber;

    @JsonProperty("processNumber")
    private String processNumber;

    @JsonProperty("deliveryStatus")
    private DeliveryStatus deliveryStatus;

    @JsonProperty("additionalFields")
    private AdditionalFields additionalFields;

    @JsonProperty("remarks")
    private String remarks;
}
