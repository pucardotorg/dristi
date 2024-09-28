package org.pucar.dristi.web.models.analytics;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;


@Schema(description = "Pending task type to be fetched from mdms")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:11.072458+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CaseOutcomeType {

    @JsonProperty("id")
    @Valid
    private String id = null;

    @JsonProperty("orderType")
    @Valid
    private String orderType = null;

    @JsonProperty("outcome")
    @Valid
    private String outcome = null;

    @JsonProperty("isJudgement")
    @Valid
    private Boolean isJudgement = null;

    @JsonProperty("judgementList")
    @Valid
    private List<String> judgementList = new ArrayList<>();
}
