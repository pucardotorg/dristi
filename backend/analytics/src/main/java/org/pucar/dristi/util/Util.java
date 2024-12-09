package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Util {


	private final ObjectMapper mapper;
	@Autowired
    public Util(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JSONArray constructArray(String kafkaJson, String jsonPath) throws Exception {
		JSONArray kafkaJsonArray = null;
		try {
			// Validating if the request is a valid json array.
			if (null != jsonPath) {
				if (JsonPath.read(kafkaJson, jsonPath) instanceof net.minidev.json.JSONArray) {
					String inputArray = mapper.writeValueAsString(JsonPath.read(kafkaJson, jsonPath));
					kafkaJsonArray = new JSONArray(inputArray);
				}
			} else if (pullArrayOutOfString(kafkaJson).startsWith("[")
					&& pullArrayOutOfString(kafkaJson).endsWith("]")) {
				kafkaJsonArray = new JSONArray(pullArrayOutOfString(kafkaJson));
			} else {
				log.info("Invalid request for a json array!");
				return null;
			}
		} catch (Exception e) {
			log.error("Exception while constructing json array for bulk index: ", e);
			log.error("Object: " + kafkaJson);
			throw e;
		}
		return kafkaJsonArray;
	}

	public String pullArrayOutOfString(String jsonString) {
		String[] array = jsonString.split(":");
		StringBuilder jsonArray = new StringBuilder();
		for (int i = 1; i < array.length; i++) {
			jsonArray.append(array[i]);
			if (i != array.length - 1)
				jsonArray.append(":");
		}
		jsonArray.deleteCharAt(jsonArray.length() - 1);

		return jsonArray.toString();
	}

}
