package org.egov.eTreasury.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundData {

    @JsonProperty("GRN")
    private String grn;

    @JsonProperty("RefundAmount")
    private String refundAmount;

    @JsonProperty("MobileNo")
    private String mobileNo;

    @JsonProperty("RefundRequestNumber")
    private String refundRequestNumber;

    @JsonProperty("STATUS")
    private String status;

    @JsonProperty("ERROR")
    private String error;

    @JsonProperty("Remarks")
    private String remarks;

    @JsonProperty("ACK_STATUS")
    private String ackStatus;

    @JsonProperty("CREDIT_DATE")
    private String creditDate;

    @JsonProperty("DN_STATUS")
    private String dnStatus;

    @JsonProperty("RN_STATUS")
    private String rnStatus;
}