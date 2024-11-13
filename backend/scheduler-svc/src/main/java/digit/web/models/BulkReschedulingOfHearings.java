package digit.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;


@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkReschedulingOfHearings {

    @JsonProperty("judgeId")
    @NotNull
    private String judgeId;

    @JsonProperty("startTime")
    private Long startTime;

    @JsonProperty("endTime")
    private Long endTime;

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId;

    @JsonProperty("hearingIds")
    private List<String> hearingIds;

    @JsonProperty("scheduleAfter")
    @NotNull
    private Long scheduleAfter;
}
