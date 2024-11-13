package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SchedulerConfig {

    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("description")
    private String description;

    @JsonProperty("uom")
    private String uom;

    @JsonProperty("unit")
    private int unit;
}
