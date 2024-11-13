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
public class AmountDetails {

    @JsonProperty("accountIdentifier")
    private String accountIdentifier;

    @JsonProperty("merchantCurrency")
    private String merchantCurrency;

    @JsonProperty("postingAmount")
    private double postingAmount;

}
