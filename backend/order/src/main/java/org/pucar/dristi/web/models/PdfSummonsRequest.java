package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import org.egov.common.contract.request.RequestInfo;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PdfSummonsRequest {

    @JsonProperty("RequestInfo")
    private Object requestInfo;

    @JsonProperty("TaskSummon")
    private List<SummonsRequest> TaskSummon;

}
