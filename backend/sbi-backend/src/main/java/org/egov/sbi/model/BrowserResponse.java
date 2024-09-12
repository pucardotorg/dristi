package org.egov.sbi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrowserResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("TransactionDetails")
    private TransactionDetails transactionDetails;
}
