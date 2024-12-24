package org.pucar.dristi.web.models;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;

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

    @JsonProperty("tenantId")
    @NonNull
    private String tenantId = null;

    @JsonProperty("flow")
    private String flow;

    public CaseSearchRequest addCriteriaItem(CaseCriteria criteriaItem) {
        this.criteria.add(criteriaItem);
        return this;
    }

}