package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.models.individual.Gender;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WitnessDeposition {

    @JsonProperty("hearingId")
    private String hearingId = null;

    @JsonProperty("courtId")
    private String courtId = null;

    @JsonProperty("caseName")
    private String caseName = null;

    @JsonProperty("filingNumber")
    private String filingNumber = null;

    @JsonProperty("caseYear")
    private String caseYear = null;

    @JsonProperty("caseNumber")
    private String caseNumber = null;

    @JsonProperty("hearingDate")
    private String hearingDate = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("age")
    @Valid Integer age = null;

    @JsonProperty("gender")
    private @Valid Gender gender = null;

    @JsonProperty("mobileNumber")
    private String mobileNumber = null;

    @JsonProperty("fatherName")
    private String fatherName = null;

    @JsonProperty("caste")
    private String caste = null;

    @JsonProperty("village")
    private String village = null;

    @JsonProperty("taluk")
    private String taluk = null;

    @JsonProperty("deposition")
    private String deposition = null;
}
