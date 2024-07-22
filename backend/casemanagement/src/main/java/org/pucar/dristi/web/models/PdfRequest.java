package org.pucar.dristi.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PdfRequest {

    @JsonProperty("RequestInfo")
    @Valid
    private RequestInfo requestInfo;

    @JsonProperty("referenceId")
    @Valid
    private String referenceId;

    @JsonProperty("referenceCode")
    @Valid
    private String referenceCode;

    @JsonProperty("tenantId")
    @Valid
    private String tenantId;


    @JsonProperty("moduleName")
    @Valid
    private String moduleName;

}
