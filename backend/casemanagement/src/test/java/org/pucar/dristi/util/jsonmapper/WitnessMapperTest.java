package org.pucar.dristi.util.jsonmapper;

import org.egov.common.contract.models.AuditDetails;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.Witness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WitnessMapperTest {

	@Mock
	private JsonMapperUtil jsonMapperUtil;

	@InjectMocks
	private WitnessMapper witnessMapper;

	private JSONObject jsonObject;
	private JSONObject dataObject;
	private JSONObject witnessDetailsObject;
	private Witness witness;
	private AuditDetails auditDetails;

	@BeforeEach
	void setUp() throws JSONException {
		jsonObject = new JSONObject();
		dataObject = new JSONObject();
		witnessDetailsObject = new JSONObject();

		jsonObject.put("Data", dataObject);
		dataObject.put("witnessDetails", witnessDetailsObject);

		witness = new Witness();
		auditDetails = new AuditDetails();
	}

	@Test
	void testGetWitnessSuccess() {
		when(jsonMapperUtil.map(witnessDetailsObject, Witness.class)).thenReturn(witness);
		when(jsonMapperUtil.map(witnessDetailsObject, AuditDetails.class)).thenReturn(auditDetails);

		Witness result = witnessMapper.getWitness(jsonObject);

		assertNotNull(result);
		assertEquals(witness, result);
		assertEquals(auditDetails, result.getAuditDetails());
		verify(jsonMapperUtil).map(witnessDetailsObject, Witness.class);
		verify(jsonMapperUtil).map(witnessDetailsObject, AuditDetails.class);
	}

	@Test
	void testGetWitnessDataObjectNull() {
		JSONObject jsonObjectWithoutData = new JSONObject();

		Witness result = witnessMapper.getWitness(jsonObjectWithoutData);

		assertNull(result);
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(Witness.class));
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(AuditDetails.class));
	}

	@Test
	void testGetWitnessDetailsNull() {
		dataObject.remove("witnessDetails");
		when(jsonMapperUtil.map(null, Witness.class)).thenReturn(null);

		Witness result = witnessMapper.getWitness(jsonObject);

		assertNull(result);
		verify(jsonMapperUtil).map(null, Witness.class);
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(AuditDetails.class));
	}

	@Test
	void testGetWitnessWitnessDetailsNotNullAuditDetailsNull() {
		when(jsonMapperUtil.map(witnessDetailsObject, Witness.class)).thenReturn(witness);
		when(jsonMapperUtil.map(witnessDetailsObject, AuditDetails.class)).thenReturn(null);

		Witness result = witnessMapper.getWitness(jsonObject);

		assertNotNull(result);
		assertEquals(witness, result);
		assertNull(result.getAuditDetails());
		verify(jsonMapperUtil).map(witnessDetailsObject, Witness.class);
		verify(jsonMapperUtil).map(witnessDetailsObject, AuditDetails.class);
	}
}
