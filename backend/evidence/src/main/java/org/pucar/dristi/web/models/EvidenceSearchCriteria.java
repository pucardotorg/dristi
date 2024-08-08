package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-16T15:17:16.225735+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvidenceSearchCriteria {
    private String id;
    private String caseId;
    private String applicationNumber;
    private String artifactType;
    private Boolean evidenceStatus;
    private String hearing;
    private String order;
    private String sourceId;
    private String sourceName;
    private List<String> status;
    private String artifactNumber;
    private String filingNumber;
    private UUID owner;
}
