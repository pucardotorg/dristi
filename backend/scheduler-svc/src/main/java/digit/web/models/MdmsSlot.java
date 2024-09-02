package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class MdmsSlot {

    @JsonProperty("id")
    private int id;

    @JsonProperty("slotName")
    private String slotName;

    @JsonProperty("slotStartTime")
    private String slotStartTime;

    @JsonProperty("slotEndTime")
    private String slotEndTime;

    @JsonProperty("slotDuration")
    private Integer slotDuration;

    @JsonProperty("unitOfMeasurement")
    private String unitOfMeasurement;
}

