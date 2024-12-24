package org.pucar.dristi.config;

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

	@Value("${egov.mdms.schema.search.endpoint}")
	private String mdmsSchemaEndPoint;

	@Value("${schemacode.state.master}")
	private String stateMasterSchema;


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

	@Value("${dristi.dev.file.search.host}")
	private String fileStoreHost;

	@Value("${dristi.dev.file.search.path}")
	private String fileStorePath;

	@Value("${dristi.dev.order.search.host}")
	private String orderSearchHost;

	@Value("${dristi.dev.order.search.url}")
	private String orderSearchPath;

	@Value("${egov.pdf.create}")
	private String generatePdfUrl;

	@Value("${egov.pdf.host}")
	private String generatePdfHost;

	@Value("${egov.credential.host}")
	private String credentialHost;

	@Value("${egov.credential.url}")
	private String credentialUrl;

	@Value("${dristi.dev.task.search.host}")
	private String taskSearchHost;

	@Value("${dristi.dev.task.search.url}")
	private String taskSearchPath;


	@Value("${egov.dristi.pdf.host}")
	private String caseBundlePdfHost;

	@Value("${egov.dristi.pdf.bundle}")
	private String caseBundlePdfPath;

	@Value("${egov.dristi.pdf.process.bundle}")
	private String processCaseBundlePdfPath;

	@Value("${dristi.case.host}")
	private String caseHost;

	@Value("${dristi.case.search.url}")
	private String caseSearchUrl;

	//ElasticSearch Config
	@Value("${egov.infra.indexer.host}")
	private String esHostUrl;

	@Value("${egov.indexer.es.username}")
	private String esUsername;

	@Value("${egov.indexer.es.password}")
	private String esPassword;

	@Value("${dristi.case.index}")
	private String caseIndex;

	@Value("${dristi.bundle.index}")
	private String caseBundleIndex;

	@Value("${dristi.hearing.index}")
	private String hearingIndex;

	@Value("${dristi.witness.index}")
	private String witnessIndex;

	@Value("${dristi.order.index}")
	private String orderIndex;

	@Value("${dristi.task.index}")
	private String taskIndex;

	@Value("${dristi.application.index}")
	private String applicationIndex;

	@Value("${dristi.artifact.index}")
	private String artifactIndex;

	@Value("${dristi.search.index.path}")
	private String searchPath;

	//Kafka
	@Value("${casemanagement.kafka.vc.create.topic}")
	private String createVc;

	@Value("${casemanagement.kafka.bundle.create.topic}")
	private String bundleCreateTopic;

	@Value("${generate.vc.code}")
	private String vcCode;

}
