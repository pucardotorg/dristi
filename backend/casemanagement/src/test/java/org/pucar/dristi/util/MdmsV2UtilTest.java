package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.Mdms;
import org.pucar.dristi.web.models.MdmsCriteriaReqV2;
import org.pucar.dristi.web.models.MdmsCriteriaV2;
import org.pucar.dristi.web.models.MdmsResponseV2;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

class MdmsV2UtilTest {

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private ObjectMapper mapper;

	@Mock
	private Configuration configs;

	@InjectMocks
	private MdmsV2Util mdmsV2Util;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFetchMdmsV2Data_Success() {
		// Arrange
		RequestInfo requestInfo = new RequestInfo();
		String tenantId = "tenantId";
		Set<String> ids = new HashSet<>();
		ids.add("id1");
		Set<String> uniqueIdentifiers = new HashSet<>();
		uniqueIdentifiers.add("uniqueId1");
		String schemaCode = "schemaCode";
		Boolean isActive = true;

		// Adding filters object
		HashMap<String, String> filters = new HashMap<>();
		filters.put("section", "Complaint");
		filters.put("name", "complainant");

		List<Mdms> mockMdmsList = List.of(new Mdms());
		MdmsResponseV2 mockMdmsResponseV2 = new MdmsResponseV2();
		mockMdmsResponseV2.setMdms(mockMdmsList);

		doReturn("http://mdms-host").when(configs).getMdmsHost();
		doReturn("/mdms-endpoint").when(configs).getMdmsEndPoint();
		doReturn(new HashMap<>()).when(restTemplate).postForObject(anyString(), any(MdmsCriteriaReqV2.class), any(Class.class));
		doReturn(mockMdmsResponseV2).when(mapper).convertValue(any(), any(Class.class));

		// Act
		List<Mdms> result = mdmsV2Util.fetchMdmsV2Data(requestInfo, tenantId, ids, uniqueIdentifiers, schemaCode, isActive, filters);

		// Assert
		assertNotNull(result);
		assertTrue(result.containsAll(mockMdmsList));
	}

	@Test
	void testGetMdmsV2Request_AllParameters() {
		// Arrange
		RequestInfo requestInfo = new RequestInfo();
		String tenantId = "tenantId";
		Set<String> ids = new HashSet<>();
		ids.add("id1");
		Set<String> uniqueIdentifiers = new HashSet<>();
		uniqueIdentifiers.add("uniqueId1");
		String schemaCode = "schemaCode";
		Boolean isActive = true;

		// Adding filters object
		HashMap<String, String> filters = new HashMap<>();
		filters.put("section", "Complaint");
		filters.put("name", "complainant");

		MdmsCriteriaV2 expectedMdmsCriteriaV2 = new MdmsCriteriaV2();
		expectedMdmsCriteriaV2.setTenantId(tenantId);
		expectedMdmsCriteriaV2.setIds(ids);
		expectedMdmsCriteriaV2.setUniqueIdentifiers(uniqueIdentifiers);
		expectedMdmsCriteriaV2.setSchemaCode(schemaCode);
		expectedMdmsCriteriaV2.setIsActive(isActive);
		expectedMdmsCriteriaV2.setFilterMap(filters); // Adding filters to the expected criteria

		MdmsCriteriaReqV2 expected = MdmsCriteriaReqV2.builder()
				.requestInfo(requestInfo)
				.mdmsCriteria(expectedMdmsCriteriaV2)
				.build();

		// Act
		MdmsCriteriaReqV2 result = mdmsV2Util.getMdmsV2Request(requestInfo, tenantId, ids, uniqueIdentifiers, schemaCode, isActive, filters);

		// Assert
		assertEquals(expected, result);
	}

	@Test
	void testGetMdmsV2Request_NoParameters() {
		// Arrange
		RequestInfo requestInfo = new RequestInfo();

		// Adding filters object
		HashMap<String, String> filters = new HashMap<>();
		filters.put("section", "Complaint");
		filters.put("name", "complainant");

		MdmsCriteriaReqV2 expected = MdmsCriteriaReqV2.builder()
				.requestInfo(requestInfo)
				.mdmsCriteria(new MdmsCriteriaV2())
				.build();

		// Act
		MdmsCriteriaReqV2 result = mdmsV2Util.getMdmsV2Request(requestInfo, null, null, null, null, null, null);

		// Assert
		assertEquals(expected, result);
	}
}
