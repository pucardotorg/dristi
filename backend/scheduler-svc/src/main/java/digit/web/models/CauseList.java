package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import digit.web.models.hearing.Attendee;
import digit.web.models.hearing.PresidedBy;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.models.Workflow;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * CauseList
 */
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CauseList {

    @JsonProperty("id")
    @Valid
    private UUID id = null;

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId = null;

    @JsonProperty("hearingId")
    @Size(min = 2, max = 64)
    @Valid
    private String hearingId = null;

    @JsonProperty("filingNumber")
    private String filingNumber = null;

    @JsonProperty("cnrNumbers")
    private List<String> cnrNumbers = new ArrayList<>();

    @JsonProperty("applicationNumbers")
    private List<String> applicationNumbers = new ArrayList<>();

    @JsonProperty("hearingType")
    @NotNull
    private String hearingType = null;

    @JsonProperty("status")
    @NotNull
    private String status = null;

    @JsonProperty("startTime")
    @Valid
    private Long startTime = null;

    @JsonProperty("endTime")
    @Valid
    private Long endTime = null;

    @JsonProperty("presidedBy")
    private PresidedBy presidedBy = null;

    @JsonProperty("attendees")
    private List<Attendee> attendees = new ArrayList<>();

    @JsonProperty("transcript")
    private List<String> transcript = new ArrayList<>();

    @JsonProperty("vcLink")
    private String vcLink = null;

    @JsonProperty("isActive")
    private Boolean isActive = null;

    @JsonProperty("documents")
    @Valid
    private List<Document> documents = new ArrayList<>();

    @JsonProperty("additionalDetails")
    private Object additionalDetails = null;

    @JsonProperty("auditDetails")
    @Valid
    private AuditDetails auditDetails = null;

    @JsonProperty("workflow")
    @Valid
    private Workflow workflow = null;

    @JsonProperty("notes")
    private String notes = null;

    @JsonProperty("courtId")
    private String courtId = null;

    @JsonProperty("caseId")
    private String caseId = null;

    @JsonProperty("caseType")
    private String caseType = null;

    @JsonProperty("caseTitle")
    private String caseTitle = null;

    @JsonProperty("caseRegistrationDate")
    private Long caseRegistrationDate = null;

    @JsonProperty("caseNumber")
    private String caseNumber = null;

    @JsonProperty("cmpNumber")
    private String cmpNumber = null;

    @JsonProperty("litigants")
    @Valid
    private List<Party> litigants = new ArrayList<>();

    @JsonProperty("representatives")
    @Valid
    private List<AdvocateMapping> representatives = new ArrayList<>();

    @JsonProperty("judgeId")
    private String judgeId = null;

    @JsonProperty("judgeName")
    private String judgeName = null;

    @JsonProperty("judgeDesignation")
    private String judgeDesignation = null;

    @JsonProperty("advocateNames")
    private List<String> advocateNames = new ArrayList<>();

    @JsonProperty("complainantAdvocates")
    private List<String> complainantAdvocates = null;

    @JsonProperty("respondentAdvocates")
    private List<String> respondentAdvocates = null;

    @JsonProperty("slot")
    private String slot = null;

    @JsonProperty("hearingDate")
    private String hearingDate = null;

    @JsonProperty("hearingTimeInMinutes")
    private Integer hearingTimeInMinutes = null;
}
