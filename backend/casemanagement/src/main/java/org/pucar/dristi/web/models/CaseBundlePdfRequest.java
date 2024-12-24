package org.pucar.dristi.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.egov.common.contract.request.RequestInfo;

/**
 * CaseBundleRequest
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseBundlePdfRequest   {
    @JsonProperty("RequestInfo")

    @Valid
    private RequestInfo requestInfo = null;

    @JsonProperty("index")
    @NonNull
    private Object index = null;

    @JsonProperty("caseObject")
    @NonNull
    private Object caseObject = null;

    @JsonProperty("caseNumber")
    @NotNull
    @Valid
    private String caseNumber = null;

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId = null;


}
