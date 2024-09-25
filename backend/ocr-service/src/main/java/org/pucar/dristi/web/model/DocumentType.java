package org.pucar.dristi.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DocumentType {

    @JsonProperty("id")
    private int id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("keyWords")
    private List<String> keyWords;
}
