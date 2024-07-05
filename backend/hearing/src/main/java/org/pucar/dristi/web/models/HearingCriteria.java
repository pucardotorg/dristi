package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Getter
@Setter
@Builder
public class HearingCriteria {

    @JsonProperty("hearingId")
    private String hearingId = null;

    @JsonProperty("cnrNumber")
    private String cnrNumber = null;

    @JsonProperty("filingNumber")
    private String filingNumber = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("applicationNumber")
    private String applicationNumber = null;

    @JsonProperty("fromDate")
    private LocalDate fromDate = null;

    @JsonProperty("toDate")
    private LocalDate toDate = null;

    @JsonProperty("limit")
    private Integer limit = null;

    @JsonProperty("offset")
    private Integer offset = null;

    @JsonProperty("sortBy")
    private String sortBy = null;

}