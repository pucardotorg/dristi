package org.pucar.dristi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EPostTrackerSearchCriteria {

    @JsonProperty("processNumber")
    private String processNumber;

    @JsonProperty("trackingNumber")
    private  String trackingNumber;

    @JsonProperty("deliveryStatus")
    private String deliveryStatus;

    @JsonProperty("deliveryStatusList")
    private List<String> deliveryStatusList;

    @JsonProperty("bookingDate")
    private String bookingDate;

    @JsonProperty("receivedDate")
    private String receivedDate;

    @JsonProperty("pagination")
    private Pagination pagination = null;
}
