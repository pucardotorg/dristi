package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pucar.dristi.annotation.CombineRequiredFields;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

/**
 * TaskExists
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:50.003326400+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CombineRequiredFields(fields = {"referenceId", "state"}, message = "combination of referenceId and state is required.")
public class TaskExists {

    @JsonProperty("taskId")
    @Valid
    private UUID taskId = null;

    @JsonProperty("filingNumber")
    private String filingNumber = null;

    @JsonProperty("cnrNumber")
    private String cnrNumber = null;

    @JsonProperty("referenceId")
    private String referenceId;

    @JsonProperty("state")
    private String state;

    @JsonProperty("exists")
    private Boolean exists = null;
}
