package org.pucar.dristi.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-11-07T14:43:02.680706949+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseNumberResponse {

    @JsonProperty("caseNumber")
    @NotNull
    @Valid
    private String caseNumber;


    @JsonProperty("caseResponse")
    @NonNull
    private Object caseResponse;

}
