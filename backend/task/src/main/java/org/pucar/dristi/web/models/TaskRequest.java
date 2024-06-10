package org.pucar.dristi.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;

import org.pucar.dristi.web.models.Task;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * TaskRequest
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:50.003326400+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequest   {

        @JsonProperty("orderId")
        @Valid
        private UUID orderId = null;

        @JsonProperty("RequestInfo")
        @Valid
        private RequestInfo requestInfo = null;

        @JsonProperty("task")
        @Valid
        private Task task = null;


}
