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
public class OptOutSearchCriteria {


    @JsonProperty("ids")
    private List<String> ids;

    @JsonProperty("judgeId")
    private String judgeId;

    @JsonProperty("caseId")
    private String caseId;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("rescheduleRequestId")
    private String rescheduleRequestId;

    @JsonProperty("individualId")
    private String individualId;
}
