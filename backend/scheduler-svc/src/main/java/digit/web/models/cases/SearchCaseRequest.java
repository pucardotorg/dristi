package digit.web.models.cases;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestInfo;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchCaseRequest {

    @JsonProperty("RequestInfo")
    @Valid
    private RequestInfo RequestInfo = null;

    @JsonProperty("criteria")
    @Valid
    private List<CaseCriteria> criteria = null;

    @JsonProperty("tenantId")
    @Valid
    private String tenantId;

    @JsonProperty("flow")
    private String flow;

}
