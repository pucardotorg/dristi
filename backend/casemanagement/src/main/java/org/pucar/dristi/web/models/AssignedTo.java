package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Schema(description = "details of person this task is assigned to. For example in case of document upload, this could be litigant or lawyer. in case of summon task, this will be person to whom summon is issued")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:11.072458+05:30[Asia/Calcutta]")
@Getter
@Setter
public class AssignedTo {

	@JsonProperty("individualId")
	private UUID individualId = null;

	@JsonProperty("name")
	private String name = null;

}