package org.pucar.dristi.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-15T11:31:40.281899+05:30[Asia/Kolkata]")
@Getter
@Setter
@Builder
public class TaskCaseSearchCriteria {

    @JsonProperty("completeStatus")
    private List<String> completeStatus = null;

    @JsonProperty("orderType")
    private List<String> orderType = null;

    @JsonProperty("applicationStatus")
    private String applicationStatus = null;

    @JsonProperty("searchText")
    private String searchText = null;


}
