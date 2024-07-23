package org.pucar.dristi.service;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IndexerUtils;
import org.pucar.dristi.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.pucar.dristi.config.ServiceConstants.PROCESS_INSTANCE_PATH;
import static org.pucar.dristi.config.ServiceConstants.RES_MSG_ID;

@Service
@Slf4j
public class IndexerService {

	private final IndexerUtils indexerUtils;

	private final Configuration config;

	private final RestTemplate restTemplate;

	private final Util util;

	@Autowired
    public IndexerService(IndexerUtils indexerUtils, Configuration config, RestTemplate restTemplate, Util util) {
        this.indexerUtils = indexerUtils;
        this.config = config;
        this.restTemplate = restTemplate;
        this.util = util;
    }

    public void esIndexer(String topic, String kafkaJson) {
		log.info("Inside indexer service:: Topic: {}, kafkaJson: {}", topic, kafkaJson);
		try {
			JSONArray kafkaJsonArray = util.constructArray(kafkaJson, PROCESS_INSTANCE_PATH);
			JSONObject requestInfo = JsonPath.read(kafkaJson,RES_MSG_ID);
			StringBuilder bulkRequest = buildBulkRequest(kafkaJsonArray,requestInfo);
			if (!bulkRequest.isEmpty()) {
				String uri = config.getEsHostUrl() + config.getBulkPath();
				indexerUtils.esPost(uri, bulkRequest.toString());
			}
		} catch (Exception e) {
			log.error("Error while processing Kafka JSON for indexing", e);
		}
	}

	private StringBuilder buildBulkRequest(JSONArray kafkaJsonArray, JSONObject requestInfo) {
		StringBuilder bulkRequest = new StringBuilder();
		for (int i = 0; i < kafkaJsonArray.length(); i++) {
			JSONObject jsonObject = kafkaJsonArray.optJSONObject(i);
			if (jsonObject != null) {
				processJsonObject(jsonObject, bulkRequest,requestInfo);
			}
		}
		return bulkRequest;
	}

	private void processJsonObject(JSONObject jsonObject, StringBuilder bulkRequest, JSONObject requestInfo) {
		try {
			String stringifiedObject = indexerUtils.buildString(jsonObject);
			String payload = indexerUtils.buildPayload(stringifiedObject,requestInfo);
			if(payload!=null && !payload.isEmpty())
				bulkRequest.append(payload);
		} catch (Exception e) {
			log.error("Error while processing JSON object: {}", jsonObject, e);
		}
	}
}
