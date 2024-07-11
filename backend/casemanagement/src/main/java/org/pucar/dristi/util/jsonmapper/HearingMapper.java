package org.pucar.dristi.util.jsonmapper;

import org.json.JSONObject;
import org.pucar.dristi.web.models.Hearing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HearingMapper {

	private final JsonMapperUtil jsonMapperUtil;

	@Autowired
	public HearingMapper(JsonMapperUtil jsonMapperUtil) {
		this.jsonMapperUtil = jsonMapperUtil;
	}

	public Hearing getHearing(JSONObject jsonObject) {
		JSONObject hearingDetails = jsonObject.optJSONObject("Data").optJSONObject("hearing");

		return jsonMapperUtil.map(hearingDetails, Hearing.class);
	}
}
