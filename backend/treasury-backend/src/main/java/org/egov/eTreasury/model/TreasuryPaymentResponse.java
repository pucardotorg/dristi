package org.egov.eTreasury.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.response.ResponseInfo;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TreasuryPaymentResponse {

    @JsonProperty
    private ResponseInfo responseInfo;

    @JsonProperty
    private TreasuryPaymentData treasuryPaymentData;
}
