package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Getter
@Setter
@Builder
public class HearingCriteria {

    @JsonProperty("hearingId")
    private String hearingId;

    @JsonProperty("hearingType")
    private String hearingType;

    @JsonProperty("cnrNumber")
    private String cnrNumber;

    @JsonProperty("filingNumber")
    private String filingNumber;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("applicationNumber")
    private String applicationNumber;

    @JsonProperty("fromDate")
    private Long fromDate;

    @JsonProperty("toDate")
    private Long toDate;

    @JsonProperty("attendeeIndividualId")
    private String attendeeIndividualId;

    @JsonProperty("courtId")
    private String courtId;

}