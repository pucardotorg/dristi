package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import java.util.UUID;

@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationCriteria {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("cnrNumber")
    private String cnrNumber = null;

    @JsonProperty("filingNumber")
    private String filingNumber = null;
    @JsonProperty("applicationType")
    private String applicationType = null;
    @JsonProperty("tenantId")
    @NotNull
    private String tenantId = null;

    @JsonProperty("applicationNumber")
    @Valid
    private String applicationNumber = null;

    @JsonProperty("applicationCMPNumber")
    private String applicationCMPNumber = null;

    @JsonProperty("owner")
    @Valid
    private UUID owner = null;

    @JsonProperty("status")
    @Valid
    private String status = null;

}