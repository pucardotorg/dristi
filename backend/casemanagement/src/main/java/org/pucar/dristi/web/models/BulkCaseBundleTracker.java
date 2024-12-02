package org.pucar.dristi.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * BulkCaseBundleTracker
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkCaseBundleTracker   {

    @JsonProperty("id")
    private String id;

    @JsonProperty("startTime")

    private Long startTime = null;

    @JsonProperty("endTime")

    private Long endTime = null;

    @JsonProperty("caseCount")

    private Integer caseCount = null;


}