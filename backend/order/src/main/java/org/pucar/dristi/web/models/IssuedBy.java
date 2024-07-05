package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Schema(description = "details of issuer of order")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-18T11:14:11.072458+05:30[Asia/Calcutta]")
@Getter
@Setter
public class IssuedBy {

    @JsonProperty("benchID")
    private String benchID = null;

    @JsonProperty("judgeID")
    private List<UUID> judgeID = null;

    @JsonProperty("courtID")
    private String courtID = null;

}
