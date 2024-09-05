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

    //Case
    @Value("${task.kafka.update.topic}")
    private String taskUpdateTopic;

    @Value("${task.kafka.create.topic}")
    private String taskCreateTopic;

    @Value("${task.kafka.summon.topic}")
    private String taskIssueSummonTopic;

    @Value("${egov.workflow.task.business.name}")
    private String taskBusinessName;

    @Value("${egov.workflow.task.business.service.name}")
    private String taskBusinessServiceName;

    @Value("${egov.workflow.task.bail.business.name}")
    private String taskBailBusinessName;

    @Value("${egov.workflow.task.bail.business.service.name}")
    private String taskBailBusinessServiceName;

    @Value("${egov.workflow.task.summon.business.name}")
    private String taskSummonBusinessName;

    @Value("${egov.workflow.task.summon.business.service.name}")
    private String taskSummonBusinessServiceName;

    @Value("${egov.workflow.task.warrant.business.name}")
    private String taskWarrantBusinessName;

    @Value("${egov.workflow.task.warrant.business.service.name}")
    private String taskWarrantBusinessServiceName;

    @Value("${egov.idgen.taskNumber}")
    private String taskNumber;

    // Case Config
    @Value("${egov.case.host}")
    private String caseHost;

    @Value("${egov.case.path}")
    private String casePath;

    // Order Config
    @Value("${egov.order.host}")
    private String orderHost;

    @Value("${egov.order.path}")
    private String orderPath;

    @Value("${summons.court.fees.sufix}")
    private String summonsCourtFeesSufix;

    @Value("${summons.epost.fees.sufix}")
    private String summonsEpostFeesSufix;


    @Value("${egov.billingservice.host}")
    private String billingServiceHost;

    @Value("${egov.billingservice.search.bill}")
    private String searchBillEndpoint;

    @Value(("${task.business.service}"))
    private String taskBusinessService;
}
