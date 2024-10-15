package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepresentativeSummary {

    @JsonProperty("partyId")
    private String partyId;

    @JsonProperty("advocateId")
    private String advocateId;

    @JsonProperty("advocateType")
    private String advocateType;
}
