package digit.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import digit.models.coremodels.AuditDetails;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReScheduleHearing {

    @JsonProperty("rescheduledRequestId")
    @NotNull
    private String rescheduledRequestId;

    @JsonProperty("hearingBookingId")
    private String hearingBookingId;

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId;

    @JsonProperty("judgeId")
    private String judgeId;

    @JsonProperty("caseId")
    private String caseId;

    @JsonProperty("requesterId")
    private String requesterId;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("status")
    private String status;

    @JsonProperty("availableAfter")
    @NotNull
    private Long availableAfter;

    @JsonProperty("suggestedDates")
    private List<Long> suggestedDates;

    @JsonProperty("availableDates")
    private List<Long> availableDates;

    @JsonProperty("representatives")
    private Set<String> representatives;

    @JsonProperty("litigants")
    private Set<String> litigants;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails;

    @JsonProperty("rowVersion")
    private Integer rowVersion = null;


}
