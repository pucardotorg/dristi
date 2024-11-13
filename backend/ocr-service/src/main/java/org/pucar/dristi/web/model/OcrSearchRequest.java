package org.pucar.dristi.web.model;

import lombok.Data;
import org.egov.common.contract.request.RequestInfo;

@Data
public class OcrSearchRequest {

    private RequestInfo requestInfo;

    private String filingNumber;

}
