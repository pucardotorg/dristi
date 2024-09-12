package org.egov.eTreasury.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class HeadDetails {

    @JsonProperty("AMOUNT")
    private String amount;

    @JsonProperty("HEADID")
    private String headId;

}
