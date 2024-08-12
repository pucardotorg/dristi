package org.pucar.dristi.web.models;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
=======
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;
>>>>>>> main

/**
 * Holds a logical grouping of cases
 */
@Schema(description = "Holds a logical grouping of cases")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-17T10:19:47.222225+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
<<<<<<< HEAD
public class CaseGroup {
	@JsonProperty("id")

	@Size(min = 2, max = 128)
	private String id = null;

	@JsonProperty("caseIds")
	@NotNull

	@Size(min = 1)
	private List<String> caseIds = new ArrayList<>();


	public CaseGroup addCaseIdsItem(String caseIdsItem) {
		this.caseIds.add(caseIdsItem);
		return this;
	}
=======
public class CaseGroup   {
        @JsonProperty("id")

        @Size(min=2,max=128)         private String id = null;

        @JsonProperty("caseIds")
          @NotNull

        @Size(min=1)         private List<String> caseIds = new ArrayList<>();


        public CaseGroup addCaseIdsItem(String caseIdsItem) {
        this.caseIds.add(caseIdsItem);
        return this;
        }
>>>>>>> main

}
