package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

/**
 * a case can have multiple hearings. this represents one of the many hearings related to the case
 */
@Schema(description = "A hearing is mostly presided by a Judge, but there is discussion on some hearing being presided by an Admin person. Hence this field will store the ID of the specific person")
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
