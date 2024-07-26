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
import org.pucar.dristi.web.models.CourtCase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourtCaseMapperTest {

	@Mock
	private JsonMapperUtil jsonMapperUtil;

	@InjectMocks
	private CourtCaseMapper courtCaseMapper;

	private JSONObject jsonObject;
	private JSONObject dataObject;
	private JSONObject caseDetails;
	private CourtCase courtCase;
	private AuditDetails auditDetails;

	@BeforeEach
	void setUp() throws JSONException {
		jsonObject = new JSONObject();
		dataObject = new JSONObject();
		caseDetails = new JSONObject();

		jsonObject.put("Data", dataObject);
		dataObject.put("caseDetails", caseDetails);

		courtCase = new CourtCase();
		auditDetails = new AuditDetails();
	}

	@Test
	void testGetCourtCaseSuccess() {
		when(jsonMapperUtil.map(caseDetails, CourtCase.class)).thenReturn(courtCase);
		when(jsonMapperUtil.map(caseDetails, AuditDetails.class)).thenReturn(auditDetails);

		CourtCase result = courtCaseMapper.getCourtCase(jsonObject);

		assertNotNull(result);
		assertEquals(courtCase, result);
		assertEquals(auditDetails, result.getAuditdetails());
		verify(jsonMapperUtil).map(caseDetails, CourtCase.class);
		verify(jsonMapperUtil).map(caseDetails, AuditDetails.class);
	}

	@Test
	void testGetCourtCaseDataObjectNull() {
		JSONObject jsonObjectWithoutData = new JSONObject();

		CourtCase result = courtCaseMapper.getCourtCase(jsonObjectWithoutData);

		assertNull(result);
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(CourtCase.class));
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(AuditDetails.class));
	}

	@Test
	void testGetCourtCaseDetailsNull() {
		dataObject.remove("caseDetails");
		when(jsonMapperUtil.map(null, CourtCase.class)).thenReturn(null);

		CourtCase result = courtCaseMapper.getCourtCase(jsonObject);

		assertNull(result);
		verify(jsonMapperUtil).map(null, CourtCase.class);
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(AuditDetails.class));
	}
}

