package drishti.payment.calculator.web.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Range {

    private Double min;
    private Double max;
    private String Unit;
    private String classificationCode;
    private Double fee;
}
