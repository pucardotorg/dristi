package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CauseListPdf {

    @JsonProperty("fileStoreId")
    private String fileStoreId;

    @JsonProperty("hearingDate")
    private String date;

    @JsonProperty("courtId")
    private String courtId;

    @JsonProperty("judgeId")
    private String judgeId;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("createdTime")
    private Long createdTime;

    @JsonProperty("createdBy")
    private String createdBy;
}
