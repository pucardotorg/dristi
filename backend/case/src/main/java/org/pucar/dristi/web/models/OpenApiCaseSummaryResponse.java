package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.web.OpenApiCaseSummary;

import javax.validation.Valid;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpenApiCaseSummaryResponse {

    @JsonProperty("responseInfo")
    @Valid
    private ResponseInfo responseInfo;

    @JsonProperty("caseSummary")
    @Valid
    private OpenApiCaseSummary caseSummary;

}
