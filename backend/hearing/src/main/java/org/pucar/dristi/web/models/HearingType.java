package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;


/**
 * a case can have multiple hearings. this represents one of the many hearings related to the case
 */
@Schema(description = "A hearing can be of different types that are specified while creating new hearing. Hearing Type are defined in MDMS")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:11.072458+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HearingType {
    @JsonProperty("id")

    private int id ;

    @JsonProperty("type")

    private String type = null;

    @JsonProperty("isactive")

    private Boolean isactive = null;

}
