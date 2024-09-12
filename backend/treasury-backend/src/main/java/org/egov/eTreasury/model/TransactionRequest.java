package org.egov.eTreasury.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.common.contract.request.RequestInfo;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {


    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("TransactionDetails")
    private TransactionDetails transactionDetails;
}
