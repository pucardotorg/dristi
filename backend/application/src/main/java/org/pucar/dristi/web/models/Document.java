package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    @JsonProperty("id")
    private String id = null;
    @JsonProperty("documentType")
    private String documentType = null;
    @JsonProperty("fileStore")
    private String fileStore = null;
    @JsonProperty("documentUid")
    private String documentUid = null;

    @JsonProperty("documentOrder")
    private Long documentOrder = null;
    @JsonProperty("additionalDetails")
    private Object additionalDetails = null;
}
