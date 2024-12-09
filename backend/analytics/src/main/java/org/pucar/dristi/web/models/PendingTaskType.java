package org.pucar.dristi.web.models;

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
public class PendingTaskType {

    @JsonProperty("id")
    @Valid
    private String id = null;

    @JsonProperty("pendingTask")
    @Valid
    private String pendingTask = null;

    @JsonProperty("actor")
    @Valid
    private String actor = null;

    @JsonProperty("isgeneric")
    @Valid
    private Boolean isgeneric = null;

    @JsonProperty("triggerAction")
    @Valid
    private List<String> triggerAction = null;

    @JsonProperty("state")
    @Valid
    private String state = null;

    @JsonProperty("workflowModule")
    @Valid
    private String workflowModule = null;

    @JsonProperty("closerAction")
    @Valid
    private List<String> closerAction = new ArrayList<>();
}
