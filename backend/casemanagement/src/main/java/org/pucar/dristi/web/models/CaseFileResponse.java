package org.pucar.dristi.web.models;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonProperty;
=======
import java.util.ArrayList;
import java.util.List;

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
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
=======
>>>>>>> main

/**
 * CaseFileResponse
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-17T10:19:47.222225+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
<<<<<<< HEAD
public class CaseFileResponse {
	@JsonProperty("requestInfo")

	@Valid
	private ResponseInfo responseInfo = null;

	@JsonProperty("caseFiles")
	@Valid
	private List<CaseFile> caseFiles = null;


	public CaseFileResponse addCaseFilesItem(CaseFile caseFilesItem) {
		if (this.caseFiles == null) {
			this.caseFiles = new ArrayList<>();
		}
		this.caseFiles.add(caseFilesItem);
		return this;
	}
=======
public class CaseFileResponse   {
        @JsonProperty("requestInfo")

          @Valid
                private RequestInfo requestInfo = null;

        @JsonProperty("caseFiles")
          @Valid
                private List<CaseFile> caseFiles = null;


        public CaseFileResponse addCaseFilesItem(CaseFile caseFilesItem) {
            if (this.caseFiles == null) {
            this.caseFiles = new ArrayList<>();
            }
        this.caseFiles.add(caseFilesItem);
        return this;
        }
>>>>>>> main

}
