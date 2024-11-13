package org.pucar.dristi.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchemaDefCriteria {

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("codes")
    private Set<String> codes;
}
