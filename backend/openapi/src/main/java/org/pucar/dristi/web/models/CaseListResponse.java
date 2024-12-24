package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * CaseListResponse
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-12-03T13:11:23.212020900+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseListResponse {
    @JsonProperty("responseInfo")

    @Valid
    private ResponseInfo responseInfo;

    @JsonProperty("caseList")
    @Valid
    private List<CaseListLineItem> caseList;

    @JsonProperty("pagination")

    @Valid
    private Pagination pagination;


    public CaseListResponse addCaseListItem(CaseListLineItem caseListItem) {
        if (this.caseList == null) {
            this.caseList = new ArrayList<>();
        }
        this.caseList.add(caseListItem);
        return this;
    }

}
