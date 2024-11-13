package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-29T13:38:04.562296+05:30[Asia/Calcutta]")
@Data
@Builder
public class NoticeDetails {

    @JsonProperty("noticeId")
    private String noticeId = null;

    @JsonProperty("issueDate")
    private Long issueDate;

    @JsonProperty("caseFilingDate")
    private Long caseFilingDate;

    @JsonProperty("docType")
    private String docType;

    @JsonProperty("docSubType")
    private String docSubType;

    @JsonProperty("partyType")
    private String partyType;

    @JsonProperty("noticeType")
    private String noticeType;
}
