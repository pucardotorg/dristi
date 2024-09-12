package org.egov.eTreasury.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.egov.common.contract.response.ResponseInfo;

public class ReturnResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("Status")
    private String status;
}
