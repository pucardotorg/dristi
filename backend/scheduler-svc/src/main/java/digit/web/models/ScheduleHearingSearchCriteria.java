package digit.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleHearingSearchCriteria {


    @JsonProperty("hearingIds")
    private List<String> hearingIds;

    @JsonProperty("judgeId")
    private String judgeId;

    @JsonProperty("courtId")
    private String courtId;

    @JsonProperty("hearingType")
    private String hearingType;

    @JsonProperty("caseId")
    private String caseId;

    @JsonProperty("tenantId")
    private String tenantId;
    // to search in a one date between hours (to make it more flexible search)
    @JsonProperty("startDateTime")
    private Long startDateTime;

    @JsonProperty("endDateTime")
    private Long endDateTime;

    @JsonProperty("status")
    private List<String> status;

    @JsonProperty("rescheduleId")
    private String rescheduleId;


}
