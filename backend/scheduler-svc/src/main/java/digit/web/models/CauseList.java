package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * CauseList
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-25T11:13:21.813391200+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CauseList {

    @JsonProperty("courtId")
    private String courtId = null;

    @JsonProperty("caseId")
    private String caseId = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("judgeId")
    private String judgeId = null;

    @JsonProperty("typeOfHearing")
    private String typeOfHearing = null;

    @JsonProperty("litigantNames")
    private List<String> litigantNames = null;

    @JsonProperty("tentativeSlot")
    private String tentativeSlot = null;

    @JsonProperty("caseTitle")
    private String caseTitle = null;

    @JsonProperty("caseDate")
    private String caseDate = null;

    public CauseList addLitigantNamesItem(String litigantNamesItem) {
        if (this.litigantNames == null) {
            this.litigantNames = new ArrayList<>();
        }
        this.litigantNames.add(litigantNamesItem);
        return this;
    }

}
