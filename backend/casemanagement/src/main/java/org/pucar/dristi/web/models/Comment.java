package org.pucar.dristi.web.models;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonProperty;
=======
import java.util.UUID;

import org.egov.common.contract.models.AuditDetails;
import org.springframework.validation.annotation.Validated;

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
import org.egov.common.contract.models.AuditDetails;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;
=======
>>>>>>> main

/**
 * Comment
 */
@Validated
<<<<<<< HEAD
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-16T15:17:16.225735+05:30[Asia/Kolkata]")
=======
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-17T10:19:47.222225+05:30[Asia/Kolkata]")
>>>>>>> main
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
<<<<<<< HEAD
public class Comment {
	@JsonProperty("id")

	@Valid
	private UUID id = null;

	@JsonProperty("tenantId")
	@NotNull

	private String tenantId = null;

	@JsonProperty("artifactId")
	@NotNull

	private String artifactId = null;

	@JsonProperty("individualId")
	@NotNull

	private String individualId = null;

	@JsonProperty("comment")
	@NotNull

	@Size(min = 2, max = 2048)
	private String comment = null;

	@JsonProperty("isActive")

	private Boolean isActive = true;

	@JsonProperty("additionalDetails")

	private Object additionalDetails = null;

	@JsonProperty("auditdetails")

	@Valid
	private AuditDetails auditdetails = null;
=======
public class Comment   {
        @JsonProperty("id")

          @Valid
                private UUID id = null;

        @JsonProperty("tenantId")
          @NotNull

                private String tenantId = null;

        @JsonProperty("artifactId")
          @NotNull

                private String artifactId = null;

        @JsonProperty("individualId")
          @NotNull

                private String individualId = null;

        @JsonProperty("comment")
          @NotNull

        @Size(min=2,max=2048)         private String comment = null;

        @JsonProperty("isActive")

                private Boolean isActive = true;

        @JsonProperty("additionalDetails")

                private String additionalDetails = null;

        @JsonProperty("auditdetails")

          @Valid
                private AuditDetails auditdetails = null;

>>>>>>> main

}
