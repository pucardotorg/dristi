package org.pucar.dristi.web.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class OcrRequest {

    private String filingNumber;

    private String fileStoreId;

    @NotNull
    private String documentType;

    private List<String> keywords;

    private Boolean extractData;

    private Integer distanceCutoff;
}
