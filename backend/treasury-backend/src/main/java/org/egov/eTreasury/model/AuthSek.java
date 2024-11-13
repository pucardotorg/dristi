package org.egov.eTreasury.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthSek {

    private String authToken;
    private String decryptedSek;
    private String billId;
    private String businessService;
    private String serviceNumber;
    private double totalDue;
    private String mobileNumber;
    private String paidBy;
    private long sessionTime;
    private String departmentId;
}