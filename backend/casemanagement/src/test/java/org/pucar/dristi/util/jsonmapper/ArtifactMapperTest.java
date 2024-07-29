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
import org.pucar.dristi.web.models.Artifact;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtifactMapperTest {

	@Mock
	private JsonMapperUtil jsonMapperUtil;

	@InjectMocks
	private ArtifactMapper artifactMapper;

	private JSONObject jsonObject;
	private JSONObject dataObject;
	private JSONObject artifactDetails;
	private JSONObject auditDetails;
	private Artifact artifact;
	private AuditDetails auditDetailsObj;

	@BeforeEach
	void setUp() throws JSONException {
		jsonObject = new JSONObject();
		dataObject = new JSONObject();
		artifactDetails = new JSONObject();
		auditDetails = new JSONObject();

		jsonObject.put("Data", dataObject);
		dataObject.put("artifactDetails", artifactDetails);
		dataObject.put("auditDetails", auditDetails);

		artifact = new Artifact();
		auditDetailsObj = new AuditDetails();
	}

	@Test
	void testGetArtifactSuccess() {
		when(jsonMapperUtil.map(artifactDetails, Artifact.class)).thenReturn(artifact);
		when(jsonMapperUtil.map(auditDetails, AuditDetails.class)).thenReturn(auditDetailsObj);

		Artifact result = artifactMapper.getArtifact(jsonObject);

		assertNotNull(result);
		assertEquals(artifact, result);
		assertEquals(auditDetailsObj, result.getAuditdetails());
		verify(jsonMapperUtil).map(artifactDetails, Artifact.class);
		verify(jsonMapperUtil).map(auditDetails, AuditDetails.class);
	}

	@Test
	void testGetArtifactDataObjectNull() {
		JSONObject jsonObjectWithoutData = new JSONObject();

		Artifact result = artifactMapper.getArtifact(jsonObjectWithoutData);

		assertNull(result);
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(Artifact.class));
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(AuditDetails.class));
	}

	@Test
	void testGetArtifactDetailsNull() {
		dataObject.remove("artifactDetails");
		when(jsonMapperUtil.map(null, Artifact.class)).thenReturn(null);

		Artifact result = artifactMapper.getArtifact(jsonObject);

		assertNull(result);
		verify(jsonMapperUtil).map(null, Artifact.class);
		verify(jsonMapperUtil, never()).map(any(JSONObject.class), eq(AuditDetails.class));
	}

	@Test
	void testGetAuditDetailsNull() {
		dataObject.remove("auditDetails");
		when(jsonMapperUtil.map(artifactDetails, Artifact.class)).thenReturn(artifact);
		when(jsonMapperUtil.map(null, AuditDetails.class)).thenReturn(null);

		Artifact result = artifactMapper.getArtifact(jsonObject);

		assertNotNull(result);
		assertEquals(artifact, result);
		assertNull(result.getAuditdetails());
		verify(jsonMapperUtil).map(artifactDetails, Artifact.class);
		verify(jsonMapperUtil).map(null, AuditDetails.class);
	}
}
