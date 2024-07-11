package org.pucar.dristi.util.jsonmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JsonMapperUtil {

	private final ObjectMapper mapper;

	@Autowired
	public JsonMapperUtil(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public <T> T map(JSONObject jsonObject, Class<T> clazz) {
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		if (jsonObject == null) {
			return null;
		}

		try {
			return mapper.readValue(jsonObject.toString(), clazz);
		} catch (Exception e) {
			log.error("Error mapping JSON to {}: {}", clazz.getSimpleName(), e.getMessage(), e);
			return null;
		}
	}
}
