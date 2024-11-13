package org.pucar.dristi.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class Ocr {

    @JsonProperty("id")
    @Valid
    private UUID id = null;

    @JsonProperty("tenantId")
    @NotNull
    //@Size(min = 2, max = 64)
    private String tenantId = null;

    @JsonProperty("filingNumber")
    //@Size(min = 2, max = 64)
    private String filingNumber = null;

    @JsonProperty("fileStoreId")
    private String fileStoreId = null;

    @JsonProperty("documentType")
    private String documentType;

    @JsonProperty("message")
    private String message = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("extractedData")
    private Object extractedData = null;
}
