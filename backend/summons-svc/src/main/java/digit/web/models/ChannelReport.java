package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelReport {

    @JsonProperty("taskNumber")
    private String taskNumber;

    @JsonProperty("processNumber")
    private String processNumber;

    @JsonProperty("deliveryStatus")
    private DeliveryStatus deliveryStatus;

    @JsonProperty("remarks")
    private String remarks;

    @JsonProperty("additionalFields")
    private AdditionalFields additionalFields;
}
