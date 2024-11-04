package org.pucar.dristi.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.egov.common.contract.request.RequestInfo;

import java.util.List;

@Data
@Accessors(chain = true)
public class OcrRequest {

    @JsonProperty("RequestInfo")
    @Valid
    private RequestInfo requestInfo = null;

    private String filingNumber;

    private String fileStoreId;

    @NotNull
    private String documentType;

    private List<String> keywords;

    private Boolean extractData;

    private Integer distanceCutoff;
}
