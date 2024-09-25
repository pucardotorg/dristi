package org.egov.persistence.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.egov.domain.model.Category;

@Getter
@AllArgsConstructor
@Builder
public class SMSRequest {
    private String mobileNumber;
    private String message;
    private Category category;
    private long expiryTime;

    private String templateId;
    private String locale;
    private String tenantId;
    private String contentType;
}