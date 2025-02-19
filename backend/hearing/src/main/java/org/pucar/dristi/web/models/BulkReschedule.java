package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkReschedule {

    @JsonProperty("judgeId")
    @NotNull
    private String judgeId;

    @JsonProperty("courtId")
    @NotNull
    private String courtId;

    @JsonProperty("startTime")
    @NotNull
    private Long startTime;

    @JsonProperty("endTime")
    @NotNull
    private Long endTime;

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId;

    @JsonProperty("slotIds")
    private Set<Integer> slotIds = new HashSet<>();

    @JsonProperty("hearingIds")
    private List<String> hearingIds;

    @JsonProperty("scheduleAfter")
    @NotNull
    private Long scheduleAfter;
}
