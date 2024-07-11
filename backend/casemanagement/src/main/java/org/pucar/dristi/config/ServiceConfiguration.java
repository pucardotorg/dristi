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
public class ServiceConfiguration {

	//ElasticSearch Config
	@Value("${egov.infra.indexer.host}")
	private String esHostUrl;

	@Value("${egov.indexer.es.username}")
	private String esUsername;

	@Value("${egov.indexer.es.password}")
	private String esPassword;

	@Value("${egov.case.index}")
	private String caseIndex;

	@Value("${egov.hearing.index}")
	private String hearingIndex;

	@Value("${egov.witness.index}")
	private String witnessIndex;

	@Value("${egov.order.index}")
	private String orderIndex;

	@Value("${egov.task.index}")
	private String taskIndex;

	@Value("${egov.application.index}")
	private String applicationIndex;

	@Value("${egov.artifact.index}")
	private String artifactIndex;

	@Value("${egov.search.index.path}")
	private String searchPath;

}
