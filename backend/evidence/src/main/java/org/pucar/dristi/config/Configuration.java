package org.pucar.dristi.config;

import lombok.Getter;
import lombok.Setter;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;


@Component
@Import({ TracerConfiguration.class })
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

	// Evidence
	@Value("${evidence.kafka.create.topic}")
	private String evidenceCreateTopic;

	@Value("${evidence.kafka.create.withoutWorkflow.topic}")
	private String evidenceCreateWithoutWorkflowTopic;

	@Value("${evidence.kafka.update.topic}")
	private String updateEvidenceKafkaTopic;

	@Value("${evidence.kafka.update.withoutWorkflow.topic}")
	private String updateEvidenceWithoutWorkflowKafkaTopic;

	// Workflow/Business Module name
	@Value("${egov.workflow.businessservice.module}")
	private String businessServiceModule;

	@Value("${egov.workflow.businessservice.name}")
	private String businessServiceName;

	// Case Config
	@Value("${egov.case.host}")
	private String caseHost;

	@Value("${egov.case.path}")
	private String caseExistsPath;

	// Application Config
	@Value("${egov.application.host}")
	private String applicationHost;

	@Value("${egov.application.path}")
	private String applicationExistsPath;

	// Order Config
	@Value("${egov.order.host}")
	private String orderHost;

	@Value("${egov.order.path}")
	private String orderExistsPath;

	// Hearing Config
	@Value("${egov.hearing.host}")
	private String hearingHost;

	@Value("${egov.hearing.path}")
	private String hearingExistsPath;

	@Value("${evidence.kafka.comments.update.topic}")
	private String evidenceUpdateCommentsTopic;

	//Idgen
	@Value("${egov.idgen.prosecutionConfig}")
	private String prosecutionConfig;

	@Value("${egov.idgen.prosecutionFormat}")
	private String prosecutionFormat;

	//Idgen
	@Value("${egov.idgen.defenceConfig}")
	private String defenceConfig;

	@Value("${egov.idgen.defenceFormat}")
	private String defenceFormat;

	//Idgen
	@Value("${egov.idgen.courtConfig}")
	private String courtConfig;

	@Value("${egov.idgen.courtFormat}")
	private String courtFormat;

	//Idgen
	@Value("${egov.idgen.defenceWitnessConfig}")
	private String defenceWitnessConfig;

	@Value("${egov.idgen.defenceWitnessFormat}")
	private String defenceWitnessFormat;

	//Idgen
	@Value("${egov.idgen.prosecutionWitnessConfig}")
	private String prosecutionWitnessConfig;

	@Value("${egov.idgen.prosecutionWitnessFormat}")
	private String prosecutionWitnessFormat;

	//Idgen
	@Value("${egov.idgen.courtWitnessConfig}")
	private String courtWitnessConfig;

	@Value("${egov.idgen.courtWitnessFormat}")
	private String courtWitnessFormat;
}
