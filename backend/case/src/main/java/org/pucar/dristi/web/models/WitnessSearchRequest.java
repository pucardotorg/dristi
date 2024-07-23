package org.pucar.dristi.web.models;

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
 * WitnessSearchRequest
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-19T15:42:53.131831400+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WitnessSearchRequest {

    @JsonProperty("RequestInfo")
    @Valid
    private RequestInfo requestInfo = null;

    @JsonProperty("searchCriteria")
    @Valid
    private List<WitnessSearchCriteria> searchCriteria = null;


    public WitnessSearchRequest addSearchCriteriaItem(WitnessSearchCriteria searchCriteriaItem) {
        if (this.searchCriteria == null) {
            this.searchCriteria = new ArrayList<>();
        }
        this.searchCriteria.add(searchCriteriaItem);
        return this;
    }

}
