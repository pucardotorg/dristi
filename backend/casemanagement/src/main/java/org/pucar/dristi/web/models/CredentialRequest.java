package org.pucar.dristi.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CredentialRequest {

    @JsonProperty("module")
    @Valid
    private String module;

    @JsonProperty("id")
    @Valid
    private String id;

    @JsonProperty("cnrNumber")
    @Valid
    private String cnrNumber;

    @JsonProperty("courtName")
    @Valid
    private String courtName;

    @JsonProperty("summonId")
    @Valid
    private String summonId;

    @JsonProperty("respondentName")
    @Valid
    private String respondentName;

    @JsonProperty("summonsIssueDate")
    @Valid
    private String summonsIssueDate;

    @JsonProperty("orderPdfSignature")
    @Valid
    private String orderPdfSignature;

    @JsonProperty("document")
    @Valid
    private String document;

}
