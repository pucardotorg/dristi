package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.pucar.dristi.annotation.CombineRequiredFields;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Getter
@Setter
@Builder
@CombineRequiredFields(fields = {"referenceId", "state"}, message = "combination of referenceId and state is required.")
public class TaskCriteria {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("cnrNumber")
    private String cnrNumber = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("orderId")
    private UUID orderId = null;

    @JsonProperty("taskNumber")
    private String taskNumber = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("referenceId")
    private String referenceId;

    @JsonProperty("state")
    private String state;


}