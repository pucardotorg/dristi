package org.pucar.dristi.util.jsonmapper;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.Hearing;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HearingMapperTest {

	@Mock
	private JsonMapperUtil jsonMapperUtil;

	@InjectMocks
	private HearingMapper hearingMapper;

	private JSONObject jsonObject;
	private JSONObject dataObject;
	private JSONObject hearingObject;
	private Hearing hearing;

	@BeforeEach
	void setUp() throws JSONException {
		jsonObject = new JSONObject();
		dataObject = new JSONObject();
		hearingObject = new JSONObject();

		jsonObject.put("Data", dataObject);
		dataObject.put("hearing", hearingObject);

		hearing = new Hearing();
	}

	@Test
	void testGetHearingSuccess() {
		when(jsonMapperUtil.map(hearingObject, Hearing.class)).thenReturn(hearing);

		Hearing result = hearingMapper.getHearing(jsonObject);

		assertNotNull(result);
		assertEquals(hearing, result);
		verify(jsonMapperUtil).map(hearingObject, Hearing.class);
	}

	@Test
	void testGetHearingDataObjectNull() {
		JSONObject jsonObjectWithoutData = new JSONObject();

		Hearing result = hearingMapper.getHearing(jsonObjectWithoutData);

		assertNull(result);
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(Hearing.class));
	}

	@Test
	void testGetHearingHearingObjectNull() {
		dataObject.remove("hearing");
		when(jsonMapperUtil.map(null, Hearing.class)).thenReturn(null);

		Hearing result = hearingMapper.getHearing(jsonObject);

		assertNull(result);
		verify(jsonMapperUtil).map(null, Hearing.class);
	}
}
