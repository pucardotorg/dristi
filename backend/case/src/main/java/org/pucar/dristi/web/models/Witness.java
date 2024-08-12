package org.pucar.dristi.web.models;

<<<<<<< HEAD
import java.util.UUID;

import org.egov.common.contract.models.AuditDetails;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

=======
import com.fasterxml.jackson.annotation.JsonProperty;
>>>>>>> main
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
=======
import org.egov.common.contract.models.AuditDetails;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;
>>>>>>> main

/**
 * Witness
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-19T15:42:53.131831400+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Witness {
<<<<<<< HEAD

    @JsonProperty("id")
=======
    @JsonProperty("id")

>>>>>>> main
    @Valid
    private UUID id = null;

    @JsonProperty("caseId")
    @NotNull
<<<<<<< HEAD
    private String caseId = null;

    @JsonProperty("filingNumber")
    private String filingNumber = null;

    @JsonProperty("cnrNumber")
    private String cnrNumber = null;

    @JsonProperty("witnessIdentifier")
=======

    private String caseId = null;

    @JsonProperty("filingNumber")

    private String filingNumber = null;

    @JsonProperty("cnrNumber")

    private String cnrNumber = null;

    @JsonProperty("witnessIdentifier")

>>>>>>> main
    private String witnessIdentifier = null;

    @JsonProperty("individualId")
    @NotNull
<<<<<<< HEAD
    private String individualId = null;

    @JsonProperty("remarks")
=======

    private String individualId = null;

    @JsonProperty("remarks")

>>>>>>> main
    @Size(min = 10, max = 5000)
    private String remarks = null;

    @JsonProperty("isActive")
<<<<<<< HEAD
    private Boolean isActive = true;

    @JsonProperty("auditDetails")
=======

    private Boolean isActive = true;

    @JsonProperty("auditDetails")

>>>>>>> main
    @Valid
    private AuditDetails auditDetails = null;

    @JsonProperty("additionalDetails")

    private Object additionalDetails = null;

<<<<<<< HEAD
=======

>>>>>>> main
}
