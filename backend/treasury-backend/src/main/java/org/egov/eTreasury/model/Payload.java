package org.egov.eTreasury.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payload {

    @JsonProperty("url")
    private String url;

    @JsonProperty("data")
    private String data;

    @JsonProperty("headers")
    private String headers;
}
