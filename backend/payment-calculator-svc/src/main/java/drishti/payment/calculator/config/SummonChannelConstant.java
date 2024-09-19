package drishti.payment.calculator.config;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class SummonChannelConstant {

    public static final String EPOST = "drishti.payment.calculator.service.summons.EPostSummonFeeService";
    public static final String POLICE = "drishti.payment.calculator.service.summons.PoliceSummonFeeService";
    public static final String EMAIL = "drishti.payment.calculator.service.summons.EmailSummonFeeService";
    public static final String SMS = "drishti.payment.calculator.service.summons.SmsSummonFeeService";

}
