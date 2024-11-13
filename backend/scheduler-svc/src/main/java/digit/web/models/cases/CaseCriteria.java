package digit.web.models.cases;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseCriteria {

    @JsonProperty("caseId")
    private String caseId;

    @JsonProperty("filingNumber")
    private String filingNumber;

    @JsonProperty("tenantId")
    private String tenantId;
}
