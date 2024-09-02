package org.pucar.dristi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Field {

    @JsonProperty("key")
    @NotNull
    @Size(min = 2, max = 64)
    private String key = null;

    @JsonProperty("value")
    @NotNull
    @Size(min = 2, max = 10000)
    private String value = null;

}
