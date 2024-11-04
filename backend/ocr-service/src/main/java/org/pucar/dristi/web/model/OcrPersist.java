package org.pucar.dristi.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.experimental.Accessors;
import org.egov.common.contract.request.RequestInfo;

@Data
@Accessors(chain = true)
public class OcrPersist {

    @JsonProperty("RequestInfo")
    @Valid
    private RequestInfo requestInfo;

    @JsonProperty("ocr")
    @Valid
    private Ocr ocr;
}
