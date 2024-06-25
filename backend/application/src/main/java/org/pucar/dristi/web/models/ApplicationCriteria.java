package org.pucar.dristi.web.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationCriteria {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("cnrNumber")
    private String cnrNumber = null;

    @JsonProperty("filingNumber")
    private String filingNumber = null;

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId = null;

    @JsonProperty("applicationNumber")
    @Valid
    private String applicationNumber = null;

    @JsonProperty("status")
    @Valid
    private String status = null;

    @JsonProperty("responseList")
    @Valid
    private List<Application> responseList = null;

    public ApplicationCriteria addResponseListItem(Application responseListItem) {
        if (this.responseList == null) {
            this.responseList = new ArrayList<>();
        }
        this.responseList.add(responseListItem);
        return this;
    }

}