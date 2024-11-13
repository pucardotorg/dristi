package org.egov.eTreasury.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ChallanData {

    @JsonProperty("billId")
    private String billId;

    @JsonProperty("businessService")
    private String businessService;

    @JsonProperty("serviceNumber")
    private String serviceNumber;

    @JsonProperty("totalDue")
    private double totalDue;

    @JsonProperty("mobileNumber")
    private String mobileNumber;

    @JsonProperty("paidBy")
    private String paidBy;
}