package com.egov.icops_integrationkerala.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelMessage {

    @JsonProperty("acknowledgeUniquenumber")
    private String acknowledgeUniqueNumber;

    @JsonProperty("failureMsg")
    private String failureMsg;

    @JsonProperty("acknowledgementStatus")
    private String acknowledgementStatus;
}