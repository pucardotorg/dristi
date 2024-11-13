package org.pucar.dristi.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class OcrResponse {
    @JsonProperty("UID")
    private String uid;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Contains Keywords")
    private Map<String, Integer> keywordCounts;

    @JsonProperty("Extracted Data")
    private Map<String, String> extractedData;
}
