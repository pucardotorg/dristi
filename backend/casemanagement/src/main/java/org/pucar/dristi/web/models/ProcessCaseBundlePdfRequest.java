package org.pucar.dristi.web.models;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.egov.common.contract.request.RequestInfo;


@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessCaseBundlePdfRequest {
    @JsonProperty("requestInfo")

    @Valid
    private RequestInfo requestInfo = null;

    @JsonProperty("index")
    @NonNull
    private Object index = null;

    @JsonProperty("tenantId")
    @NonNull
    private String tenantId = null;

    @JsonProperty("state")
    @NonNull
    private String state = null;

    @JsonProperty("caseId")
    @NotNull
    @Valid
    private String caseId = null;
}
