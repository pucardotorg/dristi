package drishti.payment.calculator.config;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class SummonChannelConstant {

    public static final String POST = "drishti.payment.calculator.service.IPostFeesCalculation";

    public static final String POLICE = "drishti.payment.calculator.service.EPoliceFeesCalculation";

}
