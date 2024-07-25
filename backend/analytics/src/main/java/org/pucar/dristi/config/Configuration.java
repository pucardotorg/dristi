package org.pucar.dristi.config;

import jakarta.annotation.PostConstruct;
import lombok.*;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

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

	// MDMS Config
	@Value("${egov.mdms.host}")
	private String mdmsHost;

	@Value("${egov.mdms.search.endpoint}")
	private String mdmsEndPoint;

	// MDMS Hearing module name
	@Value("${egov.mdms.module.name}")
	private String mdmsModuleName;

	// MDMS Hearing Type Master List name
	@Value("${egov.mdms.master.name}")
	private String mdmsMasterName;

	@Value("${egov.hearing.bussiness.services}")
	private String hearingBussinessServices;

	private List<String> hearingBussinessServiceList;

	@Value("${egov.case.bussiness.services}")
	private String caseBussinessServices;

	private List<String> caseBussinessServiceList;

	@Value("${egov.evidence.bussiness.services}")
	private String evidenceBussinessServices;

	private List<String> evidenceBussinessServiceList;

	@Value("${egov.task.bussiness.services}")
	private String taskBussinessServices;

	private List<String> taskBussinessServiceList;

	@Value("${egov.application.bussiness.services}")
	private String applicationBussinessServices;

	private List<String> applicationBussinessServiceList;

	@Value("${egov.order.bussiness.services}")
	private String orderBussinessServices;

	private List<String> orderBussinessServiceList;

	@PostConstruct
	public void init() {
		hearingBussinessServiceList = Arrays.asList(hearingBussinessServices.split(","));
		caseBussinessServiceList = Arrays.asList(caseBussinessServices.split(","));
		evidenceBussinessServiceList = Arrays.asList(evidenceBussinessServices.split(","));
		taskBussinessServiceList = Arrays.asList(taskBussinessServices.split(","));
		applicationBussinessServiceList = Arrays.asList(applicationBussinessServices.split(","));
		orderBussinessServiceList = Arrays.asList(orderBussinessServices.split(","));
	}
}
