package org.egov.eTreasury.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDetails {

    @JsonProperty("GRN")
    private String grn;

    @JsonProperty("DEPARTMENT_ID")
    private String departmentId;

    @JsonProperty("CHALLANTIMESTAMP")
    private String challanTimestamp;

    @JsonProperty("BANKREFNO")
    private String bankRefNo;

    @JsonProperty("CIN")
    private String cin;

    @JsonProperty("BANKTIMESTAMP")
    private String bankTimestamp;

    @JsonProperty("AMOUNT")
    private String amount;

    @JsonProperty("STATUS")
    private String status;

    @JsonProperty("BANK_CODE")
    private String bankCode;

    @JsonProperty("REMARKS")
    private String remarks;

    @JsonProperty("REMARK_STATUS")
    private String remarkStatus;

    @JsonProperty("PARTYNAME")
    private String partyName;

    @JsonProperty("OFFICECODE")
    private String officeCode;

    @JsonProperty("DEFACE_FLAG")
    private String defaceFlag;

    @JsonProperty("ERROR")
    private String error;

    @JsonProperty("SERVICE_DEPT_CODE")
    private String serviceDeptCode;
}
