package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkRescheduleResponse {

    @JsonProperty("ResponseInfo")
    @Valid
    private ResponseInfo ResponseInfo = null;

    @JsonProperty("Hearings")
    @Valid
    private List<ScheduleHearing> reScheduleHearings = null;

}
