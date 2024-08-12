package org.pucar.dristi.web.models;

<<<<<<< HEAD
import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
=======
import java.time.LocalDate;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;
>>>>>>> main

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
<<<<<<< HEAD
    @JsonProperty("caseId")
    private String caseId = null;

    @JsonProperty("defaultFields")
    private Boolean defaultFields = false;

    @JsonProperty("cnrNumber")
    private String cnrNumber = null;

    @JsonProperty("filingNumber")
    private String filingNumber = null;

    @JsonProperty("outcome")
    private List<String> outcome = null;

    @JsonProperty("courtCaseNumber")
    private String courtCaseNumber = null;

    @JsonProperty("filingFromDate")
    @Valid
    private Long filingFromDate = null;

    @JsonProperty("filingToDate")
    @Valid
    private Long filingToDate = null;

    @JsonProperty("registrationFromDate")
    @Valid
    private Long registrationFromDate = null;

    @JsonProperty("registrationToDate")
    @Valid
    private Long registrationToDate = null;
    //todo judgeid, stage, substage

    @JsonProperty("judgeId")
    private String judgeId = null;

    @JsonProperty("stage")
    private List<String> stage = null;

    @JsonProperty("substage")
    private String substage = null;

    @JsonProperty("litigantId")
    @Valid
    private String litigantId = null;

    @JsonProperty("advocateId")
    @Valid
    private String advocateId = null;

    @JsonProperty("status")
    @Valid
    private List<String> status = null;

    @JsonProperty("responseList")
    @Valid
    private List<CourtCase> responseList = null;


    @JsonProperty("pagination")

    @Valid
    private Pagination pagination = null;
=======
	@JsonProperty("caseId")

	private String caseId = null;

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
>>>>>>> main

}
