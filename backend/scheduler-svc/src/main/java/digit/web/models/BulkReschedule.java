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
public class BulkReschedule {

    @JsonProperty("judgeId")
    @NotNull
    private String judgeId;

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId;

    @JsonProperty("scheduleAfter")
    private Long scheduleAfter;

    @JsonProperty("courtId")
    @NotNull
    private String courtId;

    @JsonProperty("hearingIds")
    List<String> hearingIds;
}
