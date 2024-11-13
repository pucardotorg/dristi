package org.drishti.esign.web.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
public class StorageResponse {

    @JsonProperty("files")
    private List<File> files;

    @JsonCreator
    public StorageResponse(@JsonProperty("files") List<File> files) {
        this.files = files;
    }
}
