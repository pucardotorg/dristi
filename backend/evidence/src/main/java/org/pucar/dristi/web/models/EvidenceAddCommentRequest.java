package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvidenceAddCommentRequest {

    @JsonProperty("RequestInfo")
    @Valid
    private RequestInfo requestInfo = new RequestInfo();;

    @JsonProperty("evidenceAddComment")
    @Valid
    private EvidenceAddComment evidenceAddComment = new EvidenceAddComment();;
}