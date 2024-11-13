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
public class AvailabilityDTO {

    @JsonProperty("date")
    private String date;

    @JsonProperty("occupiedBandwidth")
    private Double occupiedBandwidth;
}
