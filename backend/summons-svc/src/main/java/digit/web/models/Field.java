package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Field
 */
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
