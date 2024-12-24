package org.pucar.dristi.web.models;



import com.fasterxml.jackson.annotation.JsonProperty;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * CaseSummaryResponse
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-12-03T13:11:23.212020900+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseSummaryResponse {
    @JsonProperty("responseInfo")

    @Valid
    private ResponseInfo responseInfo = null;

    @JsonProperty("caseSummary")

    @Valid
    private CaseSummary caseSummary = null;


}
