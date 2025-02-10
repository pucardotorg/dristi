package drishti.payment.calculator.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JoinCaseCalculationCriteria {

    @JsonProperty("filingNumber")
    private String filingNumber;

    @JsonProperty("joiningFor")
    private List<JoiningFor> joiningFor = new ArrayList<>();

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("caseId")
    private String caseId;


}
