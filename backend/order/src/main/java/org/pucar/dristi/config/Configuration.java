package org.pucar.dristi.config;

import lombok.Getter;
import lombok.Setter;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@Import({TracerConfiguration.class})
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

    // Case Config
    @Value("${egov.case.host}")
    private String caseHost;

    @Value("${egov.case.path}")
    private String casePath;

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


    //URLShortening
    @Value("${egov.url.shortner.host}")
    private String urlShortnerHost;

    @Value("${egov.url.shortner.endpoint}")
    private String urlShortnerEndpoint;


    //SMSNotification
    @Value("${egov.sms.notification.topic}")
    private String smsNotificationTopic;

    //save order kafka topic
    @Value("${egov.kafka.order.save.topic}")
    private String saveOrderKafkaTopic;

    //update order kafka topic
    @Value("${egov.kafka.order.update.topic}")
    private String updateOrderKafkaTopic;

    @Value("${egov.workflow.order.business.name}")
    private String orderBusinessName;

    // Order Workflow/Business Service name
    @Value("${egov.workflow.order.business.service.name}")
    private String orderBusinessServiceName;

    @Value("${egov.workflow.order.judgement.business.name}")
    private String orderJudgementBusinessName;

    // Order Workflow/Business Service name
    @Value("${egov.workflow.order.judgement.business.service.name}")
    private String orderJudgementBusinessServiceName;

    //MDMS validation
    @Value("${mdms.order.type.path}")
    private String orderTypePath;

    @Value("${mdms.order.category.path}")
    private String orderCategoryPath;

    @Value("${mdms.order.module.name}")
    private String orderModule;

    // Filestore Config
    @Value("${egov.filestore.host}")
    private String fileStoreHost;

    @Value("${egov.filestore.path}")
    private String fileStorePath;

    //Idgen
    @Value("${egov.idgen.orderConfig}")
    private String orderConfig;

    @Value("${egov.idgen.orderFormat}")
    private String orderFormat;

    @Value("${egov.documenttype.path}")
    private String documentTypePath;
}
