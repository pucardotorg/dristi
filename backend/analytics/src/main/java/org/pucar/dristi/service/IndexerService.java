package org.pucar.dristi.service;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IndexerUtils;
import org.pucar.dristi.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

import static org.pucar.dristi.config.ServiceConstants.*;

@Service
@Slf4j
public class IndexerService {

	private final IndexerUtils indexerUtils;

	private final Configuration config;

    private final Util util;

    @Autowired
    public IndexerService(IndexerUtils indexerUtils, Configuration config, Util util) {
        this.indexerUtils = indexerUtils;
        this.config = config;
        this.util = util;
    }

    public void esIndexer(String topic, String kafkaJson) {
		log.info("Inside indexer service:: Topic: {}, kafkaJson: {}", topic, kafkaJson);
		try {
			JSONArray kafkaJsonArray = util.constructArray(kafkaJson, PROCESS_INSTANCE_PATH);
			LinkedHashMap<String, Object> requestInfoMap = JsonPath.read(kafkaJson, REQUEST_INFO_PATH);
			JSONObject requestInfo = new JSONObject(requestInfoMap);
			JSONObject userInfo = requestInfo.getJSONObject("userInfo");
			JSONArray roles = userInfo.getJSONArray("roles");

			// Create the new role
			JSONObject newRole = new JSONObject();
			newRole.put("code", "INTERNAL_MICROSERVICE_ROLE");
			// Append the new role to the roles array
			roles.put(newRole);
			StringBuilder bulkRequest = buildBulkRequest(kafkaJsonArray,requestInfo);
			if (!bulkRequest.isEmpty()) {
				String uri = config.getEsHostUrl() + config.getBulkPath();
				indexerUtils.esPost(uri, bulkRequest.toString());
			}
		} catch (Exception e) {
			log.error("Error while processing Kafka JSON for indexing", e);
		}
	}

	StringBuilder buildBulkRequest(JSONArray kafkaJsonArray, JSONObject requestInfo) {
		StringBuilder bulkRequest = new StringBuilder();
		try {
			for (int i = 0; i < kafkaJsonArray.length(); i++) {
				JSONObject jsonObject = kafkaJsonArray.optJSONObject(i);
				if (jsonObject != null) {
					processJsonObject(jsonObject, bulkRequest,requestInfo);
				}
			}
		} catch (JSONException e){
			log.error("Error processing JSON array", e);
		}

		return bulkRequest;
	}

	void processJsonObject(JSONObject jsonObject, StringBuilder bulkRequest, JSONObject requestInfo) {
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
