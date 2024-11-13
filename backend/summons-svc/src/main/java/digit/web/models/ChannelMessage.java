package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-05-29T13:38:04.562296+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelMessage {

    @JsonProperty("acknowledgeUniquenumber")
    private String acknowledgeUniqueNumber;

    @JsonProperty("failureMsg")
    private String failureMsg;

    @JsonProperty("acknowledgementStatus")
    private String acknowledgementStatus;
}
