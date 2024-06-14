package org.pucar.dristi.web.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * CaseCriteria
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseCriteria {
    @JsonProperty("caseId")
    private String caseId = null;

    @JsonProperty("defaultFields")
    private Boolean defaultFields = false;

    @JsonProperty("cnrNumber")
    private String cnrNumber = null;

    @JsonProperty("filingNumber")
    private String filingNumber = null;

    @JsonProperty("courtCaseNumber")
    private String courtCaseNumber = null;

    @JsonProperty("filingFromDate")
    @Valid
    private LocalDate filingFromDate = null;

    @JsonProperty("filingToDate")
    @Valid
    private LocalDate filingToDate = null;

    @JsonProperty("registrationFromDate")
    @Valid
    private LocalDate registrationFromDate = null;

    @JsonProperty("registrationToDate")
    @Valid
    private LocalDate registrationToDate = null;

    @JsonProperty("litigantId")
    @Valid
    private String litigantId = null;

    @JsonProperty("advocateId")
    @Valid
    private String advocateId = null;

    @JsonProperty("status")
    @Valid
    private String status = null;

    @JsonProperty("responseList")
    @Valid
    private List<CourtCase> responseList = null;

    public CaseCriteria addResponseListItem(CourtCase responseListItem) {
        if (this.responseList == null) {
            this.responseList = new ArrayList<>();
        }
        this.responseList.add(responseListItem);
        return this;
    }

}
