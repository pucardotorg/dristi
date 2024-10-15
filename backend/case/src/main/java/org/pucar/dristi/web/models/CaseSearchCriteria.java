package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pucar.dristi.annotation.OneOf;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@OneOf
public class CaseSearchCriteria {

    @NotNull
    @NotEmpty
    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("caseId")
    private List<String> caseId;

    @JsonProperty("filingNumber")
    private List<String> filingNumber;

    @JsonProperty("cnrNumber")
    private List<String> cnrNumber;
}
