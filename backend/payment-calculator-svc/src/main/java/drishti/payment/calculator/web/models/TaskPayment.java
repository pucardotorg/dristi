package drishti.payment.calculator.web.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskPayment {

    private String type;
    private double courtfee;
    private Boolean issuanceFeeEnable;
    private List<IssuedEntity> issuedEntities;
    private List<String> channelAllowed;
}
