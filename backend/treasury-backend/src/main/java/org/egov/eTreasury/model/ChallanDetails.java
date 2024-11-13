package org.egov.eTreasury.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ChallanDetails {

    @JsonProperty("OFFICE_CODE")
    private String officeCode;

    @JsonProperty("FROM_DATE")
    private String fromDate;

    @JsonProperty("TO_DATE")
    private String toDate;

    @JsonProperty("PAYMENT_MODE")
    private String paymentMode;

    @JsonProperty("NO_OF_HEADS")
    private String noOfHeads;

    @JsonProperty("HEADS_DET")
    private List<HeadDetails> headsDet;

    @JsonProperty("CHALLAN_AMOUNT")
    private String challanAmount;

    @JsonProperty("TAX_ID")
    private String taxId;

    @JsonProperty("PAN_NO")
    private String panNo;

    @JsonProperty("PARTY_NAME")
    private String partyName;

    @JsonProperty("ADDRESS1")
    private String address1;

    @JsonProperty("ADDRESS2")
    private String address2;

    @JsonProperty("ADDRESS3")
    private String address3;

    @JsonProperty("PIN_NO")
    private String pinNo;

    @JsonProperty("MOBILE_NO")
    private String mobileNo;

    @JsonProperty("DEPARTMENT_ID")
    private String departmentId;

    @JsonProperty("REMARKS")
    private String remarks;

    @JsonProperty("SERVICE_DEPT_CODE")
    private String serviceDeptCode;

    @JsonProperty("TSB_RECEIPTS")
    private String tsbReceipts;

    @JsonProperty("TSB_DATA")
    private List<TsbData> tsbData;
}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class TsbData {

    @JsonProperty("TSB_ACCTYPE")
    private String tsbAccType;

    @JsonProperty("TSB_ACCNO")
    private String tsbAccNo;

    @JsonProperty("TSB_AMOUNT")
    private Double tsbAmount; // Double for monetary values

    @JsonProperty("TSB_PURPOSE")
    private String tsbPurpose;
}
