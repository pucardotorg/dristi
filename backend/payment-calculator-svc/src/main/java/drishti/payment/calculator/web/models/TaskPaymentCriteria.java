package drishti.payment.calculator.web.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskPaymentCriteria {


    @JsonProperty("channelId")
    @NotNull(message = " channelId cannot be null")
    @NotBlank(message = " channelId cannot be blank")
    private String channelId;

    @JsonProperty("receiverPincode")
    private String receiverPincode;

    @JsonProperty("tenantId")
    @NotNull(message = " tenantId cannot be null")
    @NotBlank(message = " tenantId cannot be blank")
    private String tenantId = null;

    @JsonProperty("taskType")
    @NotNull(message = " taskType cannot be null")
    @NotBlank(message = " taskType cannot be blank")
    private String taskType;

    @JsonProperty("id")
    private String id;
}
