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

	//ElasticSearch Config
	@Value("${egov.infra.indexer.host}")
	private String esHostUrl;

	@Value("${elasticsearch.poll.interval.seconds}")
	private String pollInterval;

	@Value("${egov.bulk.index}")
	private String index;

	@Value("${egov.case.overall.status.topic}")
	private String caseOverallStatusTopic;

	@Value("${egov.bulk.index.path}")
	private String bulkPath;

	@Value("${egov.indexer.es.username}")
	private String esUsername;

	@Value("${egov.indexer.es.password}")
	private String esPassword;

	@Value("${id.timezone}")
	private String timezone;

	@Value("${egov.statelevel.tenantId}")
	private String stateLevelTenantId;

	//Hearing Config
	@Value("${egov.hearing.host}")
	private String hearingHost;

	@Value("${egov.hearing.search.endpoint}")
	private String hearingSearchPath;

	//Case Config
	@Value("${egov.case.host}")
	private String caseHost;

	@Value("${egov.case.search.endpoint}")
	private String caseSearchPath;

	//Evidence Config
	@Value("${egov.evidence.host}")
	private String evidenceHost;

	@Value("${egov.evidence.search.endpoint}")
	private String evidenceSearchPath;

	//Task Config
	@Value("${egov.task.host}")
	private String taskHost;

	@Value("${egov.task.search.endpoint}")
	private String taskSearchPath;

	//Application Config
	@Value("${egov.application.host}")
	private String applicationHost;

	@Value("${egov.application.search.endpoint}")
	private String applicationSearchPath;

	//Order Config
	@Value("${egov.order.host}")
	private String orderHost;

	@Value("${egov.order.search.endpoint}")
	private String orderSearchPath;

	@Value("${api.call.delay.in.seconds}")
	private Integer apiCallDelayInSeconds;
}
