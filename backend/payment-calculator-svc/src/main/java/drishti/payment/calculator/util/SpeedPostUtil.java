package drishti.payment.calculator.util;

import drishti.payment.calculator.web.models.Range;
import drishti.payment.calculator.web.models.SpeedPost;
import drishti.payment.calculator.web.models.SpeedPostConfigParams;
import drishti.payment.calculator.web.models.WeightRange;
import drishti.payment.calculator.web.models.enums.Classification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
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


    public Double calculateEPostFee(Integer numberOfPages, Classification classification, SpeedPostConfigParams configParams) {

        Double weightPerPage = configParams.getPageWeight();
        Double printingFeePerPage = configParams.getPrintingFeePerPage();
        Double businessFee = configParams.getBusinessFee();


        Double envelopeFee = configParams.getEnvelopeChargeIncludingGst();
        Double gstPercentage = configParams.getGstPercentage();


        SpeedPost speedPost = configParams.getSpeedPost();
        // Total Weight in grams
        Double totalWeight = numberOfPages * weightPerPage;
        // Total Printing Fee
        Double totalPrintingFee = numberOfPages * printingFeePerPage;
        // Speed Post Fee
        Double speedPostFee = getSpeedPostFee(totalWeight, classification, speedPost);
        // Total Post Fee wihtout gst and envelope fee
        Double ePostFeeWithoutGST = totalPrintingFee + speedPostFee + businessFee;
        // gst on post Fee
        Double gstFee = ePostFeeWithoutGST * gstPercentage;
        // Total Fee including GST and envelope fee
        double totalFee = ePostFeeWithoutGST + gstFee + envelopeFee;

        return getRoundOffValue(totalFee);
    }

    private Double getRoundOffValue(double totalFee) {
        BigDecimal roundedTotalFee = BigDecimal.valueOf(totalFee).setScale(2, RoundingMode.HALF_UP);
        return roundedTotalFee.doubleValue();
    }
}
