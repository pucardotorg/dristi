package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvidenceAddComment {

    @JsonProperty("artifactNumber")
    private String artifactNumber = null;

    @JsonProperty("comment")
    @Valid
    List<Comment> comment =  new ArrayList<>();;

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId = null;
}