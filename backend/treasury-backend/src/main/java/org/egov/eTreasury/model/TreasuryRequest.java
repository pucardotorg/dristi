package org.egov.eTreasury.model;

import lombok.Data;
import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreasuryRequest {
    
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("TreasuryParams")
    private TreasuryParams treasuryParams;
}
