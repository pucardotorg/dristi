package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * CauseListSearchCriteria
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-04-25T11:13:21.813391200+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CauseListSearchCriteria {

    @JsonProperty("judgeIds")
    private List<String> judgeIds = null;

    @JsonProperty("caseIds")
    private List<String> caseIds = null;

    @JsonProperty("courtId")
    private String courtId = null;

    @JsonProperty("searchDate")
    private LocalDate searchDate = null;

    public CauseListSearchCriteria addJudgeIdsItem(String judgeIdsItem) {
        if (this.judgeIds == null) {
            this.judgeIds = new ArrayList<>();
        }
        this.judgeIds.add(judgeIdsItem);
        return this;
    }

    public CauseListSearchCriteria addCaseIdsItem(String caseIdsItem) {
        if (this.caseIds == null) {
            this.caseIds = new ArrayList<>();
        }
        this.caseIds.add(caseIdsItem);
        return this;
    }

}
