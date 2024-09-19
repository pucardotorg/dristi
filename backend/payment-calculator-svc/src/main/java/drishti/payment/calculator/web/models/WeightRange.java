package drishti.payment.calculator.web.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WeightRange {

    private int minWeight;
    private int maxWeight;
    private String weightUnit;
    private Map<String, Range> distanceRanges;
}
