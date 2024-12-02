package org.pucar.dristi.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.v3.oas.annotations.media.Schema;
import org.egov.tracer.model.AuditDetails;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * CaseBundleTracker
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-11-07T14:43:02.680706949+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseBundleTracker   {

    @JsonProperty("id")
    private String id;

    @JsonProperty("startTime")
    private Long startTime = null;

    @JsonProperty("endTime")

    private Long endTime = null;

    @JsonProperty("pageCount")

    private Integer pageCount = null;

    @JsonProperty("errorLog")

    private String errorLog = null;

    @JsonProperty("auditDetails")

    @Valid
    private AuditDetails auditDetails = null;


}
