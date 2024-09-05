package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.models.Address;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-29T13:38:04.562296+05:30[Asia/Calcutta]")
@Data
@Builder
public class CaseDetails {

    @JsonProperty("caseId")
    private String caseId;

    @JsonProperty("courtId")
    private String courtId;

    @JsonProperty("caseTitle")
    private String caseTitle;

    @JsonProperty("caseYear")
    private String caseYear;

    @JsonProperty("caseCharge")
    private String caseCharge;

    @JsonProperty("hearingDate")
    private Long hearingDate;

    @JsonProperty("judgeName")
    private String judgeName;

    @JsonProperty("courtName")
    private String courtName;

//    @JsonProperty("courtAddress")
//    private String courtAddress;
}