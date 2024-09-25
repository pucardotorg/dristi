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

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptOut {

    @JsonProperty("id")
    private String id;

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId;

    @JsonProperty("individualId")
    @NotNull
    private String individualId;

    @JsonProperty("caseId")
    private String caseId;

    @JsonProperty("rescheduleRequestId")
    @NotNull
    private String rescheduleRequestId;

    @JsonProperty("judgeId")
    private String judgeId;

    @JsonProperty("optOutDates")             // additional details
    private List<Long> optoutDates;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails;

    @JsonProperty("rowVersion")
    private Integer rowVersion = null;

}
