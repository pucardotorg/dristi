package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartySummary {

    @JsonProperty("partyCategory")
    private String partyCategory;

    @JsonProperty("partyType")
    private String partyType;

    @JsonProperty("individualId")
    private String individualId;

    @JsonProperty("individualName")
    private String individualName;

    @JsonProperty("organisationId")
    private String organisationId;

    @JsonProperty("isPartyInPerson")
    private Boolean isPartyInPerson;
}
