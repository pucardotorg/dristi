package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.request.RequestInfo;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestInfoBody {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
    @JsonProperty("tenantId")
    private String tenantId;

}