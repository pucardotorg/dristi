package org.pucar.dristi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Validated
@Data
@Builder
public class DeliveryChannel {

    @JsonProperty("channelName")
    private ChannelName channelName;

    @JsonProperty("paymentFees")
    private String paymentFees;

    @JsonProperty("paymentTransactionId")
    private String paymentTransactionId;

    @JsonProperty("paymentStatus")
    private String paymentStatus;

    @JsonProperty("deliveryStatus")
    private String deliveryStatus;

    @JsonProperty("deliveryStatusChangedDate")
    private String deliveryStatusChangedDate;

    @JsonProperty("channelDetails")
    private Map<String, String> channelDetails;
}