package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;
import org.pucar.dristi.web.models.PendingTask;
import org.springframework.validation.annotation.Validated;

/**
 * This object holds information about the hearing response
 */
@Schema(description = "This object holds information about the hearing response")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:11.072458+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PendingTaskResponse {

    @JsonProperty("responseInfo")
    @Valid
    private ResponseInfo responseInfo = null;

    @JsonProperty("pendingTask")
    @Valid
    private PendingTask pendingTask = null;


}
