package org.pucar.dristi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ChannelMessage {

    @JsonProperty("processNumber")
    private String processNumber;

    @JsonProperty("failureMsg")
    private String failureMsg;

    @JsonProperty("acknowledgementStatus")
    private String acknowledgementStatus;
}
