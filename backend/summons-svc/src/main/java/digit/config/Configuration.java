package digit.config;

import lombok.*;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@Import({TracerConfiguration.class})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Configuration {

    //Tenant Id
    @Value("${egov-state-level-tenant-id}")
    private String egovStateTenantId;

    //Idgen Config
    @Value("${egov.idgen.host}")
    private String idGenHost;

    @Value("${egov.idgen.path}")
    private String idGenPath;

    @Value("${summons.idgen.format}")
    private String summonsIdFormat;

    //Pdf Service Config
    @Value("${summons.pdf.template.key}")
    private String summonsPdfTemplateKey;

    @Value("${non.bailable.warrant.pdf.template.key}")
    private String nonBailableWarrantPdfTemplateKey;

    @Value("${bailable.warrant.pdf.template.key}")
    private String bailableWarrantPdfTemplateKey;

    //MDMS
    @Value("${egov.mdms.host}")
    private String mdmsHost;

    @Value("${egov.mdms.search.endpoint}")
    private String mdmsEndPoint;

    //SMSNotification
    @Value("${egov.sms.notification.topic}")
    private String smsNotificationTopic;

    @Value("${egov.pdf.service.host}")
    private String pdfServiceHost;

    @Value("${egov.pdf.service.create.endpoint}")
    private String pdfServiceEndpoint;

    // File Store Service
    @Value("${egov.file.store.summons.module}")
    private String summonsFileStoreModule;

    @Value("${egov.file.store.host}")
    private String fileStoreHost;

    @Value("${egov.file.store.save.endpoint}")
    private String fileStoreEndPoint;

    // task service

    @Value("${egov.task.service.host}")
    private String taskServiceHost;

    @Value("${egov.task.service.update.endpoint}")
    private String taskServiceUpdateEndpoint;

    @Value("${egov.task.service.search.endpoint}")
    private String taskServiceSearchEndpoint;

    @Value("${egov.task.service.update.document.endpoint}")
    private String taskServiceUpdateDocumentEndpoint;

    // ICops

    @Value("${egov.icops.host}")
    private String iCopsHost;

    @Value("${egov.icops.request.endpoint}")
    private String iCopsRequestEndPoint;

    // ESummons

    @Value("${egov.esummons.host}")
    private String eSummonsHost;

    @Value("${egov.esummons.request.endpoint}")
    private String ESummonsRequestEndPoint;

    //Billing Service

    @Value("${egov.billingservice.host}")
    private String billingServiceHost;

    @Value("${egov.demand.create.endpoint}")
    private String demandCreateEndpoint;

    @Value("${egov.billingservice.fetch.bill}")
    private String fetchBillEndpoint;

    @Value("${task.taxhead.master.code}")
    private String taskTaxHeadMasterCode;

    @Value("${task.taxhead.master.court.code}")
    private String taskTaxHeadCourtMasterCode;

    @Value("${task.taxhead.master.epost.code}")
    private String taskTaxHeadEPostMasterCode;

    @Value("${egov.tax.period.to}")
    private Long taxPeriodTo;

    @Value("${egov.tax.period.from}")
    private Long taxPeriodFrom;

    @Value("${egov.tax.consumer.type}")
    private String taxConsumerType;

    @Value("${task.module.code}")
    private String taskModuleCode;

    @Value(("${task.business.service}"))
    private String taskBusinessService;

    //Payment Calculator Service

    @Value("${payment.calculator.host}")
    private String paymentCalculatorHost;

    @Value("${payment.calculator.calculate.endpoint}")
    private String paymentCalculatorCalculateEndpoint;

    @Value(("${kafka.topic.save.task.application}"))
    private String saveTaskApplicationTopic;

    @Value("${kafka.topic.insert.summons}")
    private String insertSummonsTopic;

    @Value("${kafka.topic.update.summons}")
    private String updateSummonsTopic;

    @Value("${kafka.topic.issue.summons.application}")
    private String issueSummonsTopic;

    // EPost

    @Value("${egov.epost.host}")
    private String ePostHost;

    @Value("${egov.epost.request.endpoint}")
    private String ePostRequestEndPoint;

    @Value("${egov.mdms.payment.business.service.name}")
    private String paymentBusinessServiceName;

    @Value("${egov.is.test}")
    private boolean isTest;
}
