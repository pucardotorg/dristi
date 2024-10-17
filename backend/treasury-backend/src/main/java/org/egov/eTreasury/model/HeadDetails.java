package org.egov.eTreasury.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeadDetails {

    @JsonProperty("AMOUNT")
    private String amount;

    @JsonProperty("HEADID")
    private String headId;

}
