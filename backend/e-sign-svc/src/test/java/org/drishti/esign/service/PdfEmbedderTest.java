package org.drishti.esign.service;

import com.itextpdf.text.pdf.PdfSignatureAppearance;
import org.drishti.esign.util.FileStoreUtil;
import org.drishti.esign.web.models.ESignParameter;
import org.egov.common.contract.models.AuditDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class PdfEmbedderTest {

    @InjectMocks
    private PdfEmbedder pdfEmbedder;

    @Mock
    private Resource resource;

    @Mock
    private PdfSignatureAppearance signatureAppearance;

    private String sampleResponse;

    @Mock
    private XmlSigning xmlSigning;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private FileStoreUtil fileStoreUtil;

    ESignParameter eSignParameter;

    @BeforeEach
    public void setUp() {
        sampleResponse = "<UserX509Certificate>MIICertificateContentNA</UserX509Certificate>";

        eSignParameter = ESignParameter.builder()
                .filePath("file://")
                .id("123")
                .authType("A")
                .fileStoreId("file-456")
                .tenantId("tenant-k")
                .auditDetails(new AuditDetails("user","user",123456L,123456L)).build();
    }


    @Test
    @DisplayName("should prepare pdf for sign")
    void shouldPreparePdfForSign() {


//        pdfEmbedder.pdfSignerV2()

    }


    @Test
    @DisplayName("should sign dummy sign pdf")
    void shouldSignDummySignPdf() {

//
//        pdfEmbedder.signPdfWithDSAndReturnMultipartFileV2()

    }


}
