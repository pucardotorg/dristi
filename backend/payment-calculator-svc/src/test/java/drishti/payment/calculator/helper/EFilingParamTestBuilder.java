package drishti.payment.calculator.helper;

import drishti.payment.calculator.web.models.EFilingParam;
import drishti.payment.calculator.web.models.Range;

import java.util.Map;


public class EFilingParamTestBuilder {

    private final EFilingParam.EFilingParamBuilder builder;

    public EFilingParamTestBuilder() {
        this.builder = EFilingParam.builder();
    }

    public static EFilingParamTestBuilder builder() {
        return new EFilingParamTestBuilder();
    }

    public EFilingParam build() {
        return this.builder.build();
    }

    public EFilingParamTestBuilder withConfig() {

        this.builder.applicationFee(2.0)
                .vakalathnamaFee(6.0)
                .advocateWelfareFund(25.0)
                .advocateClerkWelfareFund(12.0)
                .delayCondonationPeriod(2629800000L)  // Assuming it's in milliseconds (around 1 month)
                .delayCondonationFee(2.0).build();
        return this;
    }

    public EFilingParamTestBuilder withPetitionFee() {

        this.builder.petitionFee(Map.of(
                        "0-50000", Range.builder()
                                .min(0.0)
                                .max(50000.0)
                                .Unit(null)
                                .classificationCode(null)
                                .fee(250.0)
                                .build(),
                        "50000-200000", Range.builder()
                                .min(50000.0)
                                .max(200000.0)
                                .Unit(null)
                                .classificationCode(null)
                                .fee(500.0)
                                .build(),
                        "200000-500000", Range.builder()
                                .min(200000.0)
                                .max(500000.0)
                                .Unit(null)
                                .classificationCode(null)
                                .fee(750.0)
                                .build(),
                        "500000-1000000", Range.builder()
                                .min(500000.0)
                                .max(1000000.0)
                                .Unit(null)
                                .classificationCode(null)
                                .fee(1000.0)
                                .build(),
                        "1000000-2000000", Range.builder()
                                .min(1000000.0)
                                .max(2000000.0)
                                .Unit(null)
                                .classificationCode(null)
                                .fee(2000.0)
                                .build(),
                        "2000000-5000000", Range.builder()
                                .min(2000000.0)
                                .max(5000000.0)
                                .Unit(null)
                                .classificationCode(null)
                                .fee(5000.0)
                                .build(),
                        "5000000-50000000000", Range.builder()
                                .min(5000000.0)
                                .max(5.0E10)
                                .Unit(null)
                                .classificationCode(null)
                                .fee(10000.0)
                                .build()
                ))
                .build();
        return this;
    }
}
