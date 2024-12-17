package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.User;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * a case can have multiple hearings. this represents one of the many hearings related to the case
 */
@Schema(description = "a case can have multiple hearings. this represents one of the many hearings related to the case")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:11.072458+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PendingTask {

    @JsonProperty("id")
    @Valid
    private String id = null;

    @JsonProperty("name")
    @Valid
    private String name = null;

    @JsonProperty("referenceId")
    @NotNull

    private String referenceId = null;

    @JsonProperty("entityType")
    @Valid
    private String entityType = null;

    @JsonProperty("status")
    @NotNull
    private String status = null;

    @JsonProperty("assignedTo")

    private List<User> assignedTo = new ArrayList<>();

    @JsonProperty("assignedRole")

    private List<String> assignedRole = new ArrayList<>();

    @JsonProperty("cnrNumber")

    private String cnrNumber = null;

    @JsonProperty("filingNumber")
    @NotNull
    private String filingNumber = null;

    @JsonProperty("isCompleted")
    @Valid
    private Boolean isCompleted = null;

    @JsonProperty("stateSla")
    @Valid
    private Long stateSla = null;

    @JsonProperty("businessServiceSla")
    @Valid
    private Long businessServiceSla = null;

    @JsonProperty("additionalDetails")
    private Object additionalDetails = null;

}
