package org.egov.transformer.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

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
