package org.pucar.dristi.web.models;

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
public class SmsTemplateData {

    private String tenantId;

    private String courtCaseNumber;

    private String applicationType;

    private String originalHearingDate;

    private String reScheduledHearingDate;

    private String submissionDate;

    private String cmpNumber;
}
