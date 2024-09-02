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
public class HearingCalendar {

    @JsonProperty("judgeId")
    private String judgeId;

    @JsonProperty("date")
    private Long date;

    @JsonProperty("isHoliday")
    private Boolean isHoliday;

    @JsonProperty("isOnLeave")
    private Boolean isOnLeave;

    @JsonProperty("description")
    private String description;

    @JsonProperty("notes")
    private String notes;

    @JsonProperty("hearings")
    private List<ScheduleHearing> hearings;


}
