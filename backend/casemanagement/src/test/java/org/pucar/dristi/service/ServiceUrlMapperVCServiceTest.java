package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.util.MdmsV2Util;
import org.pucar.dristi.web.models.CredentialRequest;
import org.pucar.dristi.web.models.Mdms;
import org.pucar.dristi.web.models.VcCredentialRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ServiceUrlMapperVCServiceTest {

	@InjectMocks
	private ServiceUrlMapperVCService service;

	@Mock
	private ServiceUrlEntityRequestService serviceUrlEntityRequestService;

	@Mock
	private Producer producer;

	@Mock
	private FileDownloadService fileDownloadService;

	@Mock
	private Configuration configuration;

	@Mock
	private MdmsV2Util mdmsV2Util;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void generateVc_emptyMdmsList_throwsCustomException() throws Exception {
		// Arrange
		VcCredentialRequest vcCredentialRequest = new VcCredentialRequest();
		vcCredentialRequest.setModuleName("validModule");
		vcCredentialRequest.setRequestInfo(new RequestInfo());
		vcCredentialRequest.setTenantId("tenantId");

		when(mdmsV2Util.fetchMdmsV2Data(any(), any(), any(), any(), any(), any()))
				.thenReturn(new ArrayList<>());

		// Act & Assert
		CustomException thrown = assertThrows(CustomException.class, () ->
				service.generateVc(vcCredentialRequest)
		);
		assertEquals("INVALID_MODULE_NAME", thrown.getCode());
	}

}
