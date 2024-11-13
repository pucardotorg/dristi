package drishti.payment.calculator.web.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BreakDown {

    private String type;
    private Double amount;
    private Map<String, Double> additionalParams;
}
