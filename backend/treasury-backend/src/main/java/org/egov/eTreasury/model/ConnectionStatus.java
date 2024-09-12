package org.egov.eTreasury.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConnectionStatus {

    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("REQUEST_TIME")
    private LocalDateTime requestTime;

    @JsonIgnore
    @JsonProperty("QUERY_TIME")
    private double queryTime;

    @JsonProperty("STATUS")
    private String status;

    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("COMPLETION_TIME")
    private LocalDateTime completionTime;

}
