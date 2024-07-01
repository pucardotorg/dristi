package org.pucar.dristi.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IndexerUtils;
import org.pucar.dristi.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.pucar.dristi.config.ServiceConstants.PROCESS_INSTANCE_PATH;

@Service
@Slf4j
public class IndexerService {

	@Autowired
	private IndexerUtils indexerUtils;

	@Autowired
	private Configuration config;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Util util;

	public void esIndexer(String topic, String kafkaJson) {
		log.info("Inside indexer service:: Topic: "+topic + ", kafkaJson: " + kafkaJson);
		JSONArray kafkaJsonArray;
		StringBuilder bulkRequest = new StringBuilder();
		try {
			kafkaJsonArray = util.constructArray(kafkaJson, PROCESS_INSTANCE_PATH);
			for (int i = 0; i < kafkaJsonArray.length(); i++) {
				if (null != kafkaJsonArray.get(i)) {
					String stringifiedObject = indexerUtils.buildString(kafkaJsonArray.get(i));
					String payload = indexerUtils.buildPayload(stringifiedObject);
					bulkRequest.append(payload);
				}
			}
		} catch (Exception e) {
			log.error("Error while building curl for indexing", e);
		}
		String uri = config.getEsHostUrl() + config.getBulkPath();
		indexerUtils.esPost(uri, bulkRequest.toString());
	}
}
