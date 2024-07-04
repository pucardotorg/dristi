package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssuedBy {
    @JsonProperty("benchId")
    private String benchId = null;

    @JsonProperty("judgeId")
    private List<UUID> judgeId = null;

    @JsonProperty("courtId")
    private String courtId = null;
}
