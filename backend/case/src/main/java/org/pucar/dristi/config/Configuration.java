package org.pucar.dristi.config;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Data
@Import({ TracerConfiguration.class })
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

	// Idgen Config
	@Value("${egov.idgen.host}")
	private String idGenHost;

	@Value("${egov.idgen.path}")
	private String idGenPath;

	@Value("${egov.idgen.caseFilingNumberCp}")
	private String caseFilingNumberCp;

	@Value("${egov.idgen.caseFilingNumberNia}")
	private String caseFilingNumberNia;

	@Value("${egov.idgen.caseNumberCc}")
	private String caseNumberCc;

	@Value("${egov.idgen.caseNumberWp}")
	private String caseNumberWp;


	// Filestore Config
	@Value("${egov.filestore.host}")
	private String fileStoreHost;

	@Value("${egov.filestore.path}")
	private String fileStorePath;

	// Advocate Config
	@Value("${egov.advocate.host}")
	private String advocateHost;

	@Value("${egov.advocate.path}")
	private String advocatePath;

    //Individual Service
    @Value("${egov.individual.host}")
    private String individualHost;

    @Value("${egov.individual.path}")
    private String individualPath;

    @Value("${egov.individual.create.path}")
    private String individualCreateEndpoint;

    @Value("${egov.individual.search.path}")
    private String individualSearchEndpoint;

    @Value("${egov.individual.update.path}")
    private String individualUpdateEndpoint;

	// Workflow Config
	@Value("${egov.workflow.host}")
	private String wfHost;

	@Value("${egov.workflow.transition.path}")
	private String wfTransitionPath;

	@Value("${egov.workflow.businessservice.search.path}")
	private String wfBusinessServiceSearchPath;

	@Value("${egov.workflow.processinstance.search.path}")
	private String wfProcessInstanceSearchPath;

	// MDMS
	@Value("${egov.mdms.host}")
	private String mdmsHost;

	@Value("${egov.mdms.search.endpoint}")
	private String mdmsEndPoint;

	// HRMS
	@Value("${egov.hrms.host}")
	private String hrmsHost;

	@Value("${egov.hrms.search.endpoint}")
	private String hrmsEndPoint;

	// URLShortening
	@Value("${egov.url.shortner.host}")
	private String urlShortnerHost;

	@Value("${egov.url.shortner.endpoint}")
	private String urlShortnerEndpoint;

	// SMSNotification
	@Value("${egov.sms.notification.topic}")
	private String smsNotificationTopic;

	//Case
	@Value("${case.kafka.update.topic}")
	private String caseUpdateTopic;

	@Value("${case.kafka.create.topic}")
	private String caseCreateTopic;

	@Value("${case.kafka.status.update.topic}")
	private String caseUpdateStatusTopic;

	@Value("${witness.kafka.create.topic}")
	private String witnessCreateTopic;

	@Value("${witness.kafka.update.topic}")
	private String witnessUpdateTopic;

	@Value("${egov.workflow.case.business.name}")
	private String caseBusinessName;

	@Value("${egov.workflow.case.business.service.name}")
	private String caseBusinessServiceName;

	//Billing
	@Value("${egov.billing.host}")
	private String billingHost;

	@Value("${egov.demand.create.endpoint}")
	private String demandCreateEndPoint;

	//Join a Case
	@Value("${egov.litigant.join.case.kafka.topic}")
	private String litigantJoinCaseTopic;

	@Value("${egov.representative.join.case.kafka.topic}")
	private String representativeJoinCaseTopic;

	@Value("${egov.update.representative.join.case.kafka.topic}")
	private String updateRepresentativeJoinCaseTopic;

	@Value("${egov.additional.join.case.kafka.topic}")
	private String additionalJoinCaseTopic;

	//Mdms

	@Value("${mdms.case.module.name}")
	private String caseModule;

	@Value("${egov.localization.statelevel}")
	private Boolean isLocalizationStateLevel;

	//Dristi Case Pdf Service
	@Value("${egov.dristi.case.pdf.host}")
	private String dristiCasePdfHost;

	@Value("${egov.dristi.case.pdf.path}")
	private String dristiCasePdfPath;

	@Value("${egov.file.store.save.endpoint}")
	private String fileStoreSaveEndPoint;

	@Value("${egov.filestore.case.module}")
	private String fileStoreCaseModule;


	@Value("${egov.sms.notification.template.id}")
	private String smsNotificationTemplateId;

	@Value("${egov.sms.notification.payment.pending.template.id}")
	private String smsNotificationPaymentPendingTemplateId;

	@Value("${egov.sms.notification.esign.pending.template.id}")
	private String smsNotificationEsignPendingTemplateId;

	@Value("${egov.sms.notification.advocate.esign.pending.template.id}")
	private String smsNotificationAdvocateEsignPendingTemplateId;

	@Value("${notification.sms.enabled}")
	private Boolean isSMSEnabled;

	//Localization
	@Value("${egov.localization.host}")
	private String localizationHost;

	@Value("${egov.localization.context.path}")
	private String localizationContextPath;

	@Value("${egov.localization.search.endpoint}")
	private String localizationSearchEndpoint;

	// Default User
	@Value("${egov.default.user.username}")
	private String defaultUserUserName;

	@Value("${egov.default.user.password}")
	private String defaultUserPassword;

	@Value("${egov.user.notification.period}")
	private String userNotificationPeriod;

	@Value("${user.oauth.url}")
	private String userOauthUrl;

	@Value("${spring.redis.timeout}")
	private Long redisTimeout;

	//	Models for encryption decryption in MDMS
	@Value("${egov.enc.mdms.security.policy.court.case}")
	private String courtCaseEncrypt;

	@Value("${egov.enc.mdms.security.policy.case.decrypt.self}")
	private String caseDecryptSelf;

	@Value("${egov.enc.mdms.security.policy.court.decrypt.other}")
	private String caseDecryptOther;

	//Idgen updated
	@Value("${egov.idgen.caseFilingConfig}")
	private String caseFilingConfig;

	@Value("${egov.idgen.caseFilingFormat}")
	private String caseFilingFormat;

	@Value("${egov.idgen.caseCNRConfig}")
	private String caseCNRConfig;

	@Value("${egov.idgen.caseCNRFormat}")
	private String caseCNRFormat;

	@Value("${egov.idgen.courtCaseConfig}")
	private String courtCaseConfig;

	@Value("${egov.idgen.courtCaseSTFormat}")
	private String courtCaseSTFormat;

	@Value("${egov.idgen.cmpConfig}")
	private String cmpConfig;

	@Value("${egov.idgen.cmpFormat}")
	private String cmpFormat;

	//Indexer
	@Value("${indexer.join.case.kafka.topic}")
	private String joinCaseTopicIndexer;
}
