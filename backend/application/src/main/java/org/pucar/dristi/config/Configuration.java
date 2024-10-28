package org.pucar.dristi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@Component
@Data
@Import({TracerConfiguration.class})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Configuration {


    // User Config
    @Value("${egov.user.host}")
    private String userHost;

    @Value("${egov.user.context.path}")
    private String userContextPath;

    @Value("${egov.user.create.path}")
    private String userCreateEndpoint;

    @Value("${egov.user.search.path}")
    private String userSearchEndpoint;

    @Value("${egov.user.update.path}")
    private String userUpdateEndpoint;


    //Idgen Config
    @Value("${egov.idgen.host}")
    private String idGenHost;

    @Value("${egov.idgen.path}")
    private String idGenPath;


    //Workflow Config
    @Value("${egov.workflow.host}")
    private String wfHost;

    @Value("${egov.workflow.transition.path}")
    private String wfTransitionPath;

    @Value("${egov.workflow.businessservice.search.path}")
    private String wfBusinessServiceSearchPath;

    @Value("${egov.workflow.processinstance.search.path}")
    private String wfProcessInstanceSearchPath;


    //MDMS
    @Value("${egov.mdms.host}")
    private String mdmsHost;

    @Value("${egov.mdms.search.endpoint}")
    private String mdmsEndPoint;


    //HRMS
    @Value("${egov.hrms.host}")
    private String hrmsHost;

    @Value("${egov.hrms.search.endpoint}")
    private String hrmsEndPoint;

    // Order Config
    @Value("${egov.order.host}")
    private String orderHost;

    @Value("${egov.order.path}")
    private String orderExistsPath;

    //URLShortening
    @Value("${egov.url.shortner.host}")
    private String urlShortnerHost;

    @Value("${egov.url.shortner.endpoint}")
    private String urlShortnerEndpoint;


    //SMSNotification
    @Value("${egov.sms.notification.topic}")
    private String smsNotificationTopic;

    // Case Config
    @Value("${egov.case.host}")
    private String caseHost;

    @Value("${egov.case.path}")
    private String caseExistsPath;

    //Application topic
    @Value("${application.kafka.create.topic}")
    private String applicationCreateTopic;

    @Value("${application.kafka.update.topic}")
    private String applicationUpdateTopic;

    @Value("${application.kafka.status.update.topic}")
    private String applicationUpdateStatusTopic;

    //Workflow
    @Value("${egov.workflow.async.order.submission.business.name}")
    private String asyncOrderSubBusinessName;

    @Value("${egov.workflow.async.order.submission.business.service.name}")
    private String asyncOrderSubBusinessServiceName;

    @Value("${egov.workflow.async.order.submission.withresponse.business.name}")
    private String asyncOrderSubWithResponseBusinessName;

    @Value("${egov.workflow.async.order.submission.withresponse.business.service.name}")
    private String asyncOrderSubWithResponseBusinessServiceName;

    @Value("${egov.workflow.async.voluntary.submission.business.name}")
    private String asyncVoluntarySubBusinessName;

    @Value("${egov.workflow.async.voluntary.submission.business.service.name}")
    private String asyncVoluntarySubBusinessServiceName;

    @Value("${application.kafka.comments.update.topic}")
    private String applicationUpdateCommentsTopic;

    // Filestore Config
    @Value("${egov.filestore.host}")
    private String fileStoreHost;

    @Value("${egov.filestore.path}")
    private String fileStorePath;

    //Idgen
    @Value("${egov.idgen.applicationConfig}")
    private String applicationConfig;

    @Value("${egov.idgen.applicationFormat}")
    private String applicationFormat;

    @Value("${egov.idgen.cmpConfig}")
    private String cmpConfig;

    @Value("${egov.idgen.cmpFormat}")
    private String cmpFormat;

    // Path for searching cases
    @Value("${egov.case.search.path}")
    private String caseSearchPath;

    @Value("${egov.workflow.delay.condonation.business.service.name}")
    private String delayCondonationBusinessServiceName;

}
