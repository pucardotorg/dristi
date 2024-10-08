package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * CaseSearchRequest
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseSearchRequest {

    @JsonProperty("RequestInfo")
    @Valid
    private RequestInfo requestInfo = null;

    @JsonProperty("criteria")
    @Valid
    private List<CaseCriteria> criteria = new ArrayList<>();

	public CaseSearchRequest addCriteriaItem(CaseCriteria criteriaItem) {
		this.criteria.add(criteriaItem);
		return this;
	}

}
