package org.pucar.dristi.web.models;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonProperty;
=======
import org.egov.common.contract.request.RequestInfo;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

>>>>>>> main
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
import org.egov.common.contract.request.RequestInfo;
import org.springframework.validation.annotation.Validated;
=======
>>>>>>> main

/**
 * CaseRequest
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-17T10:19:47.222225+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
<<<<<<< HEAD
public class CaseRequest {
	@JsonProperty("RequestInfo")

	@Valid
	private RequestInfo requestInfo = null;

	@JsonProperty("caseId")

	private String caseId = null;

	@JsonProperty("filingNumber")

	private String filingNumber = null;

	@JsonProperty("caseNumber")

	private String caseNumber = null;
=======
public class CaseRequest   {
        @JsonProperty("requestInfo")

          @Valid
                private RequestInfo requestInfo = null;

        @JsonProperty("caseId")

                private String caseId = null;

        @JsonProperty("filingNumber")

                private String filingNumber = null;

        @JsonProperty("caseNumber")

                private String caseNumber = null;
>>>>>>> main


}
