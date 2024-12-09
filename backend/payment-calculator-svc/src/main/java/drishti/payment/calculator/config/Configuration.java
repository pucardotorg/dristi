package drishti.payment.calculator.config;

import lombok.*;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@Data
@Import({TracerConfiguration.class})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Configuration {

    //MDMS
    @Value("${egov.mdms.host}")
    private String mdmsHost;

    @Value("${egov.mdms.search.endpoint}")
    private String mdmsEndPoint;

    //SMSNotification
    @Value("${egov.sms.notification.topic}")
    private String smsNotificationTopic;

    //Postal Hub
    @Value("${drishti.postalhub.create.topic}")
    private String postalHubCreateTopic;

    @Value("${drishti.postalhub.update.topic}")
    private String postalHubUpdateTopic;

    @Value("${drishti.summon.pages.count}")
    private Integer numberOfPgOfSummon;

    // Case Config
    @Value("${egov.case.host}")
    private String caseHost;

    @Value("${egov.case.path}")
    private String caseExistsPath;

    @Value("${egov.case.search.path}")
    private String caseSearchPath;

}
