package org.egov.eTreasury.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefundDetails {

    @JsonProperty("GRN")
    private String grn;

    @JsonProperty("CHALLAN_AMOUNT")
    private String challanAmount;

    @JsonProperty("REFUND_AMOUNT")
    private String refundAmount;

    @JsonProperty("MOBILENO")
    private long mobileNo;

    @JsonProperty("IDTYPE")
    private int idType;

    @JsonProperty("ID_NO")
    private String idNo;

    @JsonProperty("BENEFICIARY_NAME")
    private String beneficiaryName;

    @JsonProperty("IFSCODE")
    private String ifsCode;

    @JsonProperty("ACCNO")
    private String accNo;

    @JsonProperty("ACC_TYPE")
    private int accType;

    @JsonProperty("ORDERNO")
    private String orderNo;

    @JsonProperty("ORDER_DATE")
    private String orderDate;

    @JsonProperty("REMARKS")
    private String remarks;

    @JsonProperty("HEADS_DET")
    private List<HeadDetails> headsDet;
}
