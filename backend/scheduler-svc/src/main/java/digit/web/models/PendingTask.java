package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PendingTask {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("referenceId")
    private String referenceId = null;

    @JsonProperty("entityType")
    private String entityType = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("assignedTo")
    private List<User> assignedTo = new ArrayList<>();

    @JsonProperty("assignedRole")
    private List<String> assignedRole = new ArrayList<>();

    @JsonProperty("cnrNumber")
    private String cnrNumber = null;

    @JsonProperty("filingNumber")
    private String filingNumber = null;

    @JsonProperty("isCompleted")
    private Boolean isCompleted = null;

    @JsonProperty("stateSla")
    private Long stateSla = null;

    @JsonProperty("additionalDetails")
    private Object additionalDetails = null;

    @JsonProperty("tenantId")
    private String tenantId = null;
}
