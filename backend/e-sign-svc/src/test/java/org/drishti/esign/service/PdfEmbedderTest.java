package org.drishti.esign.service;

import com.itextpdf.text.pdf.PdfSignatureAppearance;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    public void setUp() {
        sampleResponse = "<UserX509Certificate>MIICertificateContentNA</UserX509Certificate>";
    }

@Test
    public void signPdfWithDSAndReturnMultipartFile_ExceptionThrown() throws IOException {
        // Arrange
        when(resource.getInputStream()).thenThrow(new IOException("Test Exception"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            pdfEmbedder.signPdfWithDSAndReturnMultipartFile(resource, sampleResponse,"field Name");
        });
    }

    @Test
    public void generateHash_HappyPath() throws IOException {
        // Arrange
        String sampleContent = "Sample content";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(sampleContent.getBytes());
        when(resource.getInputStream()).thenReturn(inputStream);

        // Act
        String result = pdfEmbedder.generateHash(resource);

        // Assert
        assertNotNull(result);
        assertEquals(64, result.length()); // SHA-256 hash is always 64 characters long
        assertEquals(DigestUtils.sha256Hex(sampleContent), result);
    }

    @Test
    public void generateHash_ExceptionThrown() throws IOException {
        // Arrange
        when(resource.getInputStream()).thenThrow(new IOException("Test Exception"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            pdfEmbedder.generateHash(resource);
        });
    }
}
