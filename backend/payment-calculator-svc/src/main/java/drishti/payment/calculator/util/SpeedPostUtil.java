package drishti.payment.calculator.util;

import drishti.payment.calculator.web.models.EPostConfigParams;
import drishti.payment.calculator.web.models.Range;
import drishti.payment.calculator.web.models.SpeedPost;
import drishti.payment.calculator.web.models.WeightRange;
import drishti.payment.calculator.web.models.enums.Classification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SpeedPostUtil {



    public Double getSpeedPostFee(Double weight, Double distance, SpeedPost speedPost) {
        WeightRange weightRange = getWeightRange(weight, speedPost.getWeightRanges());

        assert weightRange != null;
        Range distanceRange = calculateDistanceRange(distance, weightRange);

        assert distanceRange != null;
        return distanceRange.getFee();
    }


    public Double getSpeedPostFee(Double weight, Classification classification, SpeedPost speedPost) {
        WeightRange weightRange = getWeightRange(weight, speedPost.getWeightRanges());

        assert weightRange != null;
        Range distanceRange = calculateDistanceRange(classification, weightRange);

        assert distanceRange != null;
        return distanceRange.getFee();
    }

    private WeightRange getWeightRange(Double weight, List<WeightRange> weightRanges) {
        for (WeightRange range : weightRanges) {
            int lowerBound = range.getMinWeight();
            int upperBound = range.getMaxWeight();
            if (weight >= lowerBound && weight <= upperBound) {
                return range;
            }
        }
        return null; // Invalid weight range
    }
    private Range calculateDistanceRange(Double distance, WeightRange weightRange) {
        Map<String, Range> distanceMap = weightRange.getDistanceRanges();
        for (Range range : distanceMap.values()) {
            Double lowerBound = range.getMin();
            Double upperBound = range.getMax();

            if (distance >= lowerBound && distance <= upperBound) {
                return range;
            }
        }

        return null; // Invalid distance range
    }

    private Range calculateDistanceRange(Classification classification, WeightRange weightRange) {
        Map<String, Range> distanceMap = weightRange.getDistanceRanges();
        for (Range range : distanceMap.values()) {
            if (classification.equals(Classification.fromValue(range.getClassificationCode()))) {
                return range;
            }
        }

        return null; // Invalid distance range
    }



}
