package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MdmsHearing {

    @JsonProperty("id")
    private int id;

    @JsonProperty("hearingType")
    private String hearingType;

    @JsonProperty("hearingName")
    private String hearingName;

    @JsonProperty("hearingTime")
    private Integer hearingTime;

    @JsonProperty("priority")
    private Integer priority;

    @JsonProperty("unitOfMeasurement")
    private String unitOfMeasurement;
}
