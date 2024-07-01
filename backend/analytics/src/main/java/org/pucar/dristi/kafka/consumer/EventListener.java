package org.pucar.dristi.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.config.ServiceConstants;
import org.pucar.dristi.service.IndexerService;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventListener implements MessageListener<String, String> {

	@Autowired
	private IndexerService indexerService;

	@Autowired
	private Configuration config;

	@Override
	/**
	 * Messages listener which acts as consumer. This message listener is injected
	 * inside a kafkaContainer. This consumer is a start point to the following
	 * index jobs: 1. Re-index 2. Legacy Index 3. PGR custom index 4. PT custom
	 * index 5. Core indexing
	 */
	public void onMessage(ConsumerRecord<String, String> data) {
		log.info("Topic from CoreIndexMessageListener: " + data.topic());
		// Adding in MDC so that tracer can add it in header
		MDC.put(ServiceConstants.TENANTID_MDC_STRING, config.getStateLevelTenantId());
		try {
			indexerService.esIndexer(data.topic(), data.value());
		} catch (Exception e) {
			log.error("error while updating ES document: ", e);
		}
	}

}
