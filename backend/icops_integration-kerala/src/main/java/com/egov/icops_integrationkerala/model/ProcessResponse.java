package com.egov.icops_integrationkerala.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.egov.common.contract.response.ResponseInfo;

@Data
@Builder
public class ProcessResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("channelMessage")
    private ChannelMessage channelMessage;
}
