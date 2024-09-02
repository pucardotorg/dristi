package drishti.payment.calculator.web.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistanceRange {

    private Double minDistance;
    private Double maxDistance;
    private String distanceUnit;
    private Double fee;
}
