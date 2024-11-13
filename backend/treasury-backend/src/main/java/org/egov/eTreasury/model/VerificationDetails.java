package org.egov.eTreasury.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationDetails {

    @JsonProperty("AMOUNT")
    private Double amount;

    @JsonProperty("OFFICE_CODE")
    private String officeCode;

    @JsonProperty("DEPARTMENT_ID")
    private String departmentId;

    @JsonProperty("SERVICE_DEPT_CODE")
    private String serviceDeptCode;
}
