package org.egov.eTreasury.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TreasuryPaymentData {

    private String tenantId;

    private String grn;

    private String challanTimestamp;

    private String bankRefNo;

    private String bankTimestamp;

    private String bankCode;

    private char status;

    private String cin;

    private BigDecimal amount;

    private String partyName;

    private String departmentId;

    private String remarkStatus;

    private String remarks;

    private String fileStoreId;

    private String billId;

    private double totalDue;

    private String mobileNumber;

    private String paidBy;

    private String businessService;


}