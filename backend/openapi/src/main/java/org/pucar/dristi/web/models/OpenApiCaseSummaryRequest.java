package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OpenApiCaseSummaryRequest {

    @NotNull
    @NotEmpty
    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("cnrNumber")
    private String cnrNumber;

    @JsonProperty("caseType")
    private String caseType;

    @JsonProperty("year")
    @Min(2024)
    private Integer year;

    @JsonProperty("startYear")
    private Long startYear;

    @JsonProperty("endYear")
    private Long endYear;

    @JsonProperty("caseNumber")
    private Integer caseNumber;

    @JsonProperty("pagination")
    private Pagination pagination;
}
