package drishti.payment.calculator.factory;


import drishti.payment.calculator.config.ApplicationContextProvider;
import drishti.payment.calculator.config.ChannelConstant;
import drishti.payment.calculator.service.SummonPayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class SummonFactory {


    public SummonPayment getChannelById(String channelId) {
        try {

            String clazzName = ChannelConstant.getConstant(channelId);
            Class<?> klass = Class.forName(clazzName);
            ApplicationContext context = ApplicationContextProvider.getApplicationContext();
            return (SummonPayment) context.getBean(klass);

        } catch (Exception e) {
            log.error("Error occurred while fetching object for class" + e.getMessage());
            return null;
        }

    }
}
