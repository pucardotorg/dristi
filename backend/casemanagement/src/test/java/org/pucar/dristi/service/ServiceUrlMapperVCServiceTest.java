package org.pucar.dristi.service;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.web.models.CredentialRequest;
import org.pucar.dristi.web.models.VcCredentialRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceUrlMapperVCServiceTest {

    @Mock
    private ServiceUrlEntityRequestService serviceUrlEntityRequestService;

    @Mock
    private Producer producer;

    @Mock
    private FileDownloadService fileDownloadService;

    @InjectMocks
    private ServiceUrlMapperVCService serviceUrlMapperVCService;

    private VcCredentialRequest vcCredentialRequest;

    @BeforeEach
    void setUp() {
        vcCredentialRequest = new VcCredentialRequest();
        vcCredentialRequest.setReferenceCode("summons-order");
    }

    @Test
    void testGenerateVc_validReferenceCode() {
        String signedHashValue = "signedHash";
        CredentialRequest credentialRequest = new CredentialRequest();

        when(fileDownloadService.downloadAndExtractSignature(any())).thenReturn(signedHashValue);
        when(serviceUrlEntityRequestService.getEntityDetails(signedHashValue, vcCredentialRequest)).thenReturn(credentialRequest);

        VcCredentialRequest result = serviceUrlMapperVCService.generateVc(vcCredentialRequest);

        assertEquals(vcCredentialRequest, result);
        verify(fileDownloadService).downloadAndExtractSignature(vcCredentialRequest);
        verify(serviceUrlEntityRequestService).getEntityDetails(signedHashValue, vcCredentialRequest);
        verify(producer).push("create-vc", credentialRequest);
    }

    @Test
    void testGenerateVc_invalidReferenceCode() {
        vcCredentialRequest.setReferenceCode("invalid-code");

        CustomException exception = assertThrows(CustomException.class, () -> {
            serviceUrlMapperVCService.generateVc(vcCredentialRequest);
        });

        assertEquals("INVALID_REFERENCE_CODE", exception.getCode());
        assertEquals("The reference code invalid-code is not recognized.", exception.getMessage());

        verify(fileDownloadService, never()).downloadAndExtractSignature(any());
        verify(serviceUrlEntityRequestService, never()).getEntityDetails(any(), any());
        verify(producer, never()).push(any(), any());
    }
}
