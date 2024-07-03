package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Getter
@Setter
public class HearingCriteria {

    @JsonProperty("hearingId")
    @Valid
    private String hearingId = null;

    @JsonProperty("cnrNumber")
    @NotNull
    @Valid
    private String cnrNumber = null;

    @JsonProperty("filingNumber")
    @Valid
    private String filingNumber = null;

    @JsonProperty("tenantId")
    @Valid
    private String tenantId = null;

    @JsonProperty("applicationNumber")
    @Valid
    @NotNull
    private String applicationNumber = null;

    @JsonProperty("fromDate")
    @Valid
    private LocalDate fromDate = null;

    @JsonProperty("toDate")
    @Valid
    private LocalDate toDate = null;

    @JsonProperty("limit")
    @Valid
    private Integer limit = null;

    @JsonProperty("offset")
    @Valid
    private Integer offset = null;

    @JsonProperty("sortBy")
    @Valid
    private String sortBy = null;

}