package drishti.payment.calculator.helper;

import drishti.payment.calculator.web.models.Range;
import drishti.payment.calculator.web.models.SpeedPost;
import drishti.payment.calculator.web.models.SpeedPostConfigParams;
import drishti.payment.calculator.web.models.WeightRange;

import java.util.Arrays;
import java.util.Map;

public class SpeedPostConfigParamsTestBuilder {


    private final SpeedPostConfigParams.SpeedPostConfigParamsBuilder builder;

    public SpeedPostConfigParamsTestBuilder() {
        this.builder = SpeedPostConfigParams.builder();
    }

    public static SpeedPostConfigParamsTestBuilder builder() {
        return new SpeedPostConfigParamsTestBuilder();
    }

    public SpeedPostConfigParams build() {
        return this.builder.build();
    }

    public SpeedPostConfigParamsTestBuilder withConfig() {
        this.builder.pageWeight(4.0)
                .weightUnit("grams")
                .printingFeePerPage(5.0)
                .businessFee(2.0)
                .envelopeChargeIncludingGst(10.0)
                .gstPercentage(0.18)
                .courtFee(10.0)
                .applicationFee(5.0)
                .build();
        return this;
    }

    public SpeedPostConfigParamsTestBuilder withSpeedPost() {
        this.builder.speedPost(SpeedPost.builder()
                .weightRanges(Arrays.asList(
                        WeightRange.builder()
                                .minWeight(0)
                                .maxWeight(50)
                                .weightUnit("grams")
                                .distanceRanges(Map.of(
                                        "0-50", Range.builder()
                                                .min(0.0)
                                                .max(50.0)
                                                .Unit("KM")
                                                .classificationCode("LTD")
                                                .fee(15.0)
                                                .build(),
                                        "51-200", Range.builder()
                                                .min(51.0)
                                                .max(200.0)
                                                .Unit("KM")
                                                .classificationCode("ROC")
                                                .fee(35.0)
                                                .build(),
                                        "201-1000", Range.builder()
                                                .min(201.0)
                                                .max(1000.0)
                                                .Unit("KM")
                                                .classificationCode("")
                                                .fee(35.0)
                                                .build(),
                                        "1001-2000", Range.builder()
                                                .min(1001.0)
                                                .max(2000.0)
                                                .Unit("KM")
                                                .classificationCode("")
                                                .fee(35.0)
                                                .build(),
                                        "2000-3500", Range.builder()
                                                .min(2001.0)
                                                .max(3500.0)
                                                .Unit("KM")
                                                .classificationCode("")
                                                .fee(35.0)
                                                .build()
                                ))
                                .build(),
                        WeightRange.builder()
                                .minWeight(51)
                                .maxWeight(200)
                                .weightUnit("grams")
                                .distanceRanges(Map.of(
                                        "0-50", Range.builder()
                                                .min(0.0)
                                                .max(50.0)
                                                .Unit("KM")
                                                .classificationCode("LTD")
                                                .fee(25.0)
                                                .build(),
                                        "51-200", Range.builder()
                                                .min(51.0)
                                                .max(200.0)
                                                .Unit("KM")
                                                .classificationCode("ROC")
                                                .fee(35.0)
                                                .build(),
                                        "201-1000", Range.builder()
                                                .min(201.0)
                                                .max(1000.0)
                                                .Unit("KM")
                                                .classificationCode("")
                                                .fee(40.0)
                                                .build(),
                                        "1001-2000", Range.builder()
                                                .min(1001.0)
                                                .max(2000.0)
                                                .Unit("KM")
                                                .classificationCode("")
                                                .fee(60.0)
                                                .build(),
                                        "2000-3500", Range.builder()
                                                .min(2001.0)
                                                .max(3500.0)
                                                .Unit("KM")
                                                .classificationCode("")
                                                .fee(70.0)
                                                .build()
                                ))
                                .build(),
                        WeightRange.builder()
                                .minWeight(201)
                                .maxWeight(500)
                                .weightUnit("grams")
                                .distanceRanges(Map.of(
                                        "0-50", Range.builder()
                                                .min(0.0)
                                                .max(50.0)
                                                .Unit("KM")
                                                .classificationCode("LTD")
                                                .fee(30.0)
                                                .build(),
                                        "51-200", Range.builder()
                                                .min(51.0)
                                                .max(200.0)
                                                .Unit("KM")
                                                .classificationCode("ROC")
                                                .fee(50.0)
                                                .build(),
                                        "201-1000", Range.builder()
                                                .min(201.0)
                                                .max(1000.0)
                                                .Unit("KM")
                                                .classificationCode("")
                                                .fee(60.0)
                                                .build(),
                                        "1001-2000", Range.builder()
                                                .min(1001.0)
                                                .max(2000.0)
                                                .Unit("KM")
                                                .classificationCode("")
                                                .fee(80.0)
                                                .build(),
                                        "2000-3500", Range.builder()
                                                .min(2001.0)
                                                .max(3500.0)
                                                .Unit("KM")
                                                .classificationCode("")
                                                .fee(90.0)
                                                .build()
                                ))
                                .build(),
                        WeightRange.builder()
                                .minWeight(501)
                                .maxWeight(10000)
                                .weightUnit("grams")
                                .distanceRanges(Map.of(
                                        "0-50", Range.builder()
                                                .min(0.0)
                                                .max(50.0)
                                                .Unit("KM")
                                                .classificationCode("LTD")
                                                .fee(10.0)
                                                .build(),
                                        "51-200", Range.builder()
                                                .min(51.0)
                                                .max(200.0)
                                                .Unit("KM")
                                                .classificationCode("ROC")
                                                .fee(15.0)
                                                .build(),
                                        "201-1000", Range.builder()
                                                .min(201.0)
                                                .max(1000.0)
                                                .Unit("KM")
                                                .classificationCode("")
                                                .fee(30.0)
                                                .build(),
                                        "1001-2000", Range.builder()
                                                .min(1001.0)
                                                .max(2000.0)
                                                .Unit("KM")
                                                .classificationCode("")
                                                .fee(40.0)
                                                .build(),
                                        "2000-3500", Range.builder()
                                                .min(2001.0)
                                                .max(3500.0)
                                                .Unit("KM")
                                                .classificationCode("")
                                                .fee(50.0)
                                                .build()
                                ))
                                .build()
                ))
                .build()).build();
        return this;
    }
}