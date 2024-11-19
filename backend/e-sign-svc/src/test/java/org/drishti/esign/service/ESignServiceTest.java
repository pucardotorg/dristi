package org.drishti.esign.service;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.drishti.esign.cipher.Encryption;
import org.drishti.esign.config.Configuration;
import org.drishti.esign.kafka.Producer;
import org.drishti.esign.repository.EsignRequestRepository;
import org.drishti.esign.util.FileStoreUtil;
import org.drishti.esign.util.XmlFormDataSetter;
import org.drishti.esign.web.models.*;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.PrivateKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ESignServiceTest {

    @InjectMocks
    private ESignService eSignService;

    @Mock
    private PdfEmbedder pdfEmbedder;

    @Mock
    private XmlSigning xmlSigning;

    @Mock
    private Encryption encryption;

    @Mock
    private XmlFormDataSetter formDataSetter;

    @Mock
    private XmlGenerator xmlGenerator;

    @Mock
    private HttpServletRequest servletRequest;

    @Mock
    private FileStoreUtil fileStoreUtil;

    @Mock
    private ServletContext servletContext;

    @Mock
    private Producer producer;

    @Mock
    private Configuration configuration;

    @Mock
    private EsignRequestRepository repository;


    private final ESignRequest request = new ESignRequest(RequestInfo.builder().userInfo(User.builder().build()).build(), ESignParameter.builder().build());
    private Resource resource;
    private PrivateKey privateKey;


    @BeforeEach
    public void setUp() {

        ESignParameter eSignParameter = new ESignParameter();
        eSignParameter.setFileStoreId("12345");
        eSignParameter.setTenantId("tenant1");
        eSignParameter.setPageModule("module1");
        request.setESignParameter(eSignParameter);

        resource = mock(Resource.class);
        privateKey = mock(PrivateKey.class);
    }

    @Test
    public void signDoc_HappyPath() throws Exception {
        String fileHash = "fileHash";
        String strToEncrypt = "strToEncrypt";
        String signedXml = "signedXml";

        when(fileStoreUtil.fetchFileStoreObjectById(anyString(), anyString())).thenReturn(resource);
        when(pdfEmbedder.generateHash(any(Resource.class))).thenReturn(fileHash);
        when(formDataSetter.setFormXmlData(any(), any(ESignXmlData.class))).thenReturn(new ESignXmlData());
        when(xmlGenerator.generateXml(any(ESignXmlData.class))).thenReturn(strToEncrypt);
        when(encryption.getPrivateKey(anyString())).thenReturn(privateKey);
        when(servletRequest.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRealPath(anyString())).thenReturn("upload");
        when(xmlSigning.signXmlStringNew(anyString(), any(PrivateKey.class))).thenReturn(signedXml);

        ESignXmlForm result = eSignService.signDoc(request);

        assertNotNull(result);
        verify(xmlGenerator, times(1)).writeToXmlFile(any(), any());
    }

    @Test
    public void signDoc_Exception() throws Exception {
        when(fileStoreUtil.fetchFileStoreObjectById(anyString(), anyString())).thenReturn(resource);
        when(pdfEmbedder.generateHash(any(Resource.class))).thenReturn("fileHash");
        when(formDataSetter.setFormXmlData(anyString(), any(ESignXmlData.class))).thenReturn(new ESignXmlData());
        when(xmlGenerator.generateXml(any(ESignXmlData.class))).thenReturn("strToEncrypt");
        when(encryption.getPrivateKey(anyString())).thenThrow(new RuntimeException("Test Exception"));
        when(configuration.getEsignCreateTopic()).thenReturn("testTopic");
        doNothing().when(producer).push(anyString(), any());

        ESignXmlForm result = eSignService.signDoc(request);

        assertNotNull(result);
        assertEquals("", result.getESignRequest());
        assertEquals("application/xml", result.getContentType());
    }

    @Test
    @DisplayName("Test successful signing of document with digital signature")
    void testSignDocWithDigitalSignature_Success() throws IOException {
        // Arrange

        SignDocParameter eSignParameter = SignDocParameter.builder().txnId("123").build();
        ESignParameter eSignDetails = ESignParameter.builder().auditDetails(AuditDetails.builder().build()).build();
        MultipartFile multipartFile = mock(MultipartFile.class);
        SignDocRequest request = SignDocRequest.builder().eSignParameter(eSignParameter).build();

        when(repository.getESignDetails(anyString())).thenReturn(eSignDetails);
        when(fileStoreUtil.fetchFileStoreObjectById(any(), any())).thenReturn(resource);
        when(pdfEmbedder.signPdfWithDSAndReturnMultipartFile(any(), any(), any())).thenReturn(multipartFile);
        when(fileStoreUtil.storeFileInFileStore(any(), any())).thenReturn("signedFileStoreId");

        // Act
        String result = eSignService.signDocWithDigitalSignature(request);

        // Assert
        assertEquals("signedFileStoreId", result);

    }

    @Test
    @DisplayName("Test exception handling during file signing")
    void testSignDocWithDigitalSignature_Exception() throws IOException {
        // Arrange
        SignDocRequest request = mock(SignDocRequest.class);
        SignDocParameter eSignParameter = mock(SignDocParameter.class);
        ESignParameter eSignDetails = mock(ESignParameter.class);

        when(request.getESignParameter()).thenReturn(eSignParameter);
        when(eSignParameter.getTxnId()).thenReturn("txn123");
        when(repository.getESignDetails("txn123")).thenReturn(eSignDetails);
        when(eSignDetails.getFileStoreId()).thenReturn("fileStoreId");
        when(eSignParameter.getTenantId()).thenReturn("tenantId");
        when(fileStoreUtil.fetchFileStoreObjectById("fileStoreId", "tenantId")).thenReturn(resource);
        when(pdfEmbedder.signPdfWithDSAndReturnMultipartFile(any(), any(), any())).thenThrow(new IOException("File signing failed"));

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            eSignService.signDocWithDigitalSignature(request);
        });
        assertEquals("java.io.IOException: File signing failed", thrown.getMessage());
    }
}
