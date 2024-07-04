package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;


@Schema(description = "Case stage substage object")
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseStageSubStage {

    @JsonProperty("caseOverallStatus")
    private CaseOverallStatus caseOverallStatus = null;

}
