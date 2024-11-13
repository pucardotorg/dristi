package org.drishti.esign.service;

import jakarta.servlet.ServletContext;
import org.drishti.esign.cipher.Encryption;
import org.drishti.esign.util.FileStoreUtil;
import org.drishti.esign.util.XmlFormDataSetter;
import org.drishti.esign.web.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import jakarta.servlet.http.HttpServletRequest;
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

    private final ESignRequest request = new ESignRequest();
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
        when(formDataSetter.setFormXmlData(any() , any(ESignXmlData.class))).thenReturn(new ESignXmlData());
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

        ESignXmlForm result = eSignService.signDoc(request);

        assertNotNull(result);
        assertEquals("", result.getESignRequest());
        assertEquals("application/xml", result.getContentType());
    }

    @Test
    public void signDocWithDigitalSignature_HappyPath() throws IOException {
        SignDocRequest signDocRequest = new SignDocRequest();
        SignDocParameter signDocParameter = new SignDocParameter();
        signDocParameter.setFileStoreId("12345");
        signDocParameter.setTenantId("tenant1");
        signDocParameter.setResponse("response");
        signDocRequest.setESignParameter(signDocParameter);

        MultipartFile multipartFile = mock(MultipartFile.class);

        when(fileStoreUtil.fetchFileStoreObjectById(anyString(), anyString())).thenReturn(resource);
        when(pdfEmbedder.signPdfWithDSAndReturnMultipartFile(any(Resource.class), anyString())).thenReturn(multipartFile);
        when(fileStoreUtil.storeFileInFileStore(any(MultipartFile.class), anyString())).thenReturn("signedFileStoreId");

        String result = eSignService.signDocWithDigitalSignature(signDocRequest);

        assertNotNull(result);
        assertEquals("signedFileStoreId", result);
    }

    @Test
    public void signDocWithDigitalSignature_Exception() throws IOException {
        SignDocRequest signDocRequest = new SignDocRequest();
        SignDocParameter signDocParameter = new SignDocParameter();
        signDocParameter.setFileStoreId("12345");
        signDocParameter.setTenantId("tenant1");
        signDocParameter.setResponse("response");
        signDocRequest.setESignParameter(signDocParameter);

        when(fileStoreUtil.fetchFileStoreObjectById(anyString(), anyString())).thenReturn(resource);
        when(pdfEmbedder.signPdfWithDSAndReturnMultipartFile(any(Resource.class), anyString())).thenThrow(new IOException("Test Exception"));

        assertThrows(RuntimeException.class, () -> {
            eSignService.signDocWithDigitalSignature(signDocRequest);
        });
    }
}
