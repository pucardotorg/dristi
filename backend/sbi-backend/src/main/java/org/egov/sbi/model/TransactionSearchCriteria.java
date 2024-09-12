package org.egov.sbi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionSearchCriteria {

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("MerchantOrderNumber")
    private String merchantOrderNumber;

    @JsonProperty("billId")
    private String billId;
}
