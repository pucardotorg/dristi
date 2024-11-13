package org.drishti.esign.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ESignParameter {

    @JsonProperty("uidToken")
    private String uidToken;

    @JsonProperty("consent")
    private String consent;  // user consent

    @JsonProperty("authType")
    private String authType;  // otp=1, fingerprint=2, iris=3

    @JsonProperty("fileStoreId")
    private String fileStoreId;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("pageModule")
    private String pageModule;

}
