package org.pucar.dristi.config;

import lombok.*;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;


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

	// Idgen Config
	@Value("${egov.idgen.host}")
	private String idGenHost;

	@Value("${egov.idgen.path}")
	private String idGenPath;

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

	//AvdClerk Var
	@Value("${advClerk.kafka.create.topic}")
	private String advClerkcreateTopic;

	// Update Advocate Clerk kafka topic
	@Value("${advClerk.kafka.update.topic}")
	private String advClerkUpdateTopic;

	// Create Advocate kafka topic
	@Value("${advocate.kafka.create.topic}")
	private String advocateCreateTopic;

	// Update Advocate kafka topic
	@Value("${advocate.kafka.update.topic}")
	private String advocateUpdateTopic;

	// Advocate Workflow/Business name
	@Value("${egov.workflow.advocate.business.name}")
	private String advocateBusinessName;

	// Advocate Workflow/Business Service name
	@Value("${egov.workflow.advocate.business.service.name}")
	private String advocateBusinessServiceName;

	// Advocate-clerk Workflow/Business name
	@Value("${egov.workflow.advocate-clerk.business.name}")
	private String advocateClerkBusinessName;

	// Advocate-clerk Workflow/Business Service name
	@Value("${egov.workflow.advocate-clerk.business.service.name}")
	private String advocateClerkBusinessServiceName;

	// Advocate application number Id name
	@Value("${egov.idgen.advocate.application.number.id.name}")
	private String advApplicationNumberConfig;

	// Advocate clerk application number Id name
	@Value("${egov.idgen.advocate.clerk.application.number.id.name}")
	private String advClerkApplicationNumberConfig;

	@Value("${egov.idgen.advConfig}")
	private String advConfig;

	@Value("${egov.idgen.advFormat}")
	private String advFormat;

	@Value("${egov.idgen.clerkConfig}")
	private String clerkConfig;

	@Value("${egov.idgen.clerkFormat}")
	private String clerkFormat;

	@Value("${egov.sms.notification.advocate.registered.template.id}")
	private String smsNotificationAdvocateRegisteredTemplateId;

	//Localization
	@Value("${egov.localization.host}")
	private String localizationHost;

	@Value("${egov.localization.context.path}")
	private String localizationContextPath;

	@Value("${egov.localization.search.endpoint}")
	private String localizationSearchEndpoint;
}
