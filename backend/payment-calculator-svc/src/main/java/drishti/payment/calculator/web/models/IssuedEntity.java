package drishti.payment.calculator.web.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssuedEntity {

    private String type;
    private String entity;
    private double issuanceFee;
}
