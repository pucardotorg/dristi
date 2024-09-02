package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * CauseList Details for the Next day.
 */
@Schema(description = "CauseList Details for the Next day.")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-25T11:13:21.813391200+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CauseListResponse {

    @JsonProperty("ResponseInfo")
    @Valid
    private ResponseInfo responseInfo = null;

    @JsonProperty("CauseList")
    @Valid
    private List<CauseList> causeList = null;


    public CauseListResponse addCauseListItem(CauseList causeListItem) {
        if (this.causeList == null) {
            this.causeList = new ArrayList<>();
        }
        this.causeList.add(causeListItem);
        return this;
    }

}
