package digit.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.validation.annotation.Validated;

@Schema(description = "Rescheduling request search criteria and Request info")
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkReScheduleHearingRequest {

    @JsonProperty("RequestInfo")
    @Valid
    private RequestInfo requestInfo = null;

    @JsonProperty("BulkRescheduling")
    @Valid
    private BulkReschedulingOfHearings bulkRescheduling = null;
}
