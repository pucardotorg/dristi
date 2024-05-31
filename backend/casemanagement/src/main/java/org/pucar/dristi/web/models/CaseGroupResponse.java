package org.pucar.dristi.web.models;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CaseGroupResponse
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-17T10:19:47.222225+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseGroupResponse   {
        @JsonProperty("responseInfo")

          @Valid
                private ResponseInfo responseInfo = null;

        @JsonProperty("caseGroups")
          @Valid
                private List<CaseGroup> caseGroups = null;


        public CaseGroupResponse addCaseGroupsItem(CaseGroup caseGroupsItem) {
            if (this.caseGroups == null) {
            this.caseGroups = new ArrayList<>();
            }
        this.caseGroups.add(caseGroupsItem);
        return this;
        }

}
