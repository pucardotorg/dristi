package org.drishti.esign.service;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.drishti.esign.web.models.ESignXmlData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class XmlGeneratorTest {

    @InjectMocks
    private XmlGenerator xmlGenerator;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ServletContext servletContext;

    private final ESignXmlData eSignXmlData = new ESignXmlData();

    @BeforeEach
    public void setUp() {
        eSignXmlData.setVer("1.0");
        eSignXmlData.setSc("someSc");
        eSignXmlData.setTs("timestamp");
        eSignXmlData.setTxn("txn123");
        eSignXmlData.setEkycId("ekycId123");
        eSignXmlData.setEkycIdType("type");
        eSignXmlData.setAspId("aspId123");
        eSignXmlData.setAuthMode("authMode");
        eSignXmlData.setResponseSigType("responseSigType");
        eSignXmlData.setResponseUrl("responseUrl");
        eSignXmlData.setId("id123");
        eSignXmlData.setHashAlgorithm("SHA-256");
        eSignXmlData.setDocInfo("docInfo");
        eSignXmlData.setDocHashHex("docHashHex");
    }

    @Test
    public void generateXml_HappyPath() {
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRealPath(anyString())).thenReturn("upload");

        String xml = xmlGenerator.generateXml(eSignXmlData);

        assertNotNull(xml);
        assertTrue(xml.contains("<Esign"));
        assertTrue(xml.contains("ver=\"1.0\""));
        assertTrue(xml.contains("hashAlgorithm=\"SHA-256\""));
        assertTrue(xml.contains("<InputHash"));
    }

    @Test
    public void writeToXmlFile_HappyPath() throws Exception {
        String xmlContent = "<root><element>value</element></root>";
        String fileName = "test.xml";

        // Create temporary file
        File tempFile = Files.createTempFile(fileName, ".xml").toFile();
        tempFile.deleteOnExit();

        xmlGenerator.writeToXmlFile(xmlContent, tempFile.getAbsolutePath());

        assertTrue(tempFile.exists());

        // Read back and verify content
        String content = new String(Files.readAllBytes(tempFile.toPath()));
        assertTrue(content.contains("<root>"));
        assertTrue(content.contains("<element>value</element>"));
    }

    @Test
    public void generateXml_EmptyFields() {
        ESignXmlData emptyData = new ESignXmlData();
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRealPath(anyString())).thenReturn("upload");

        String xml = xmlGenerator.generateXml(emptyData);

        assertNotNull(xml);
        assertTrue(xml.contains("<Esign"));
        assertTrue(xml.contains("<InputHash"));
    }



    @Test
    public void writeToXmlFile_NullInput() throws Exception {
        String fileName = "test.xml";

        File tempFile = Files.createTempFile(fileName, ".xml").toFile();
        tempFile.deleteOnExit();

        xmlGenerator.writeToXmlFile(null, tempFile.getAbsolutePath());


    }

    @Test
    public void writeToXmlFile_EmptyXml() throws Exception {
        String fileName = "test.xml";

        File tempFile = Files.createTempFile(fileName, ".xml").toFile();
        tempFile.deleteOnExit();

        xmlGenerator.writeToXmlFile("", tempFile.getAbsolutePath());

        assertTrue(tempFile.exists());
        String content = new String(Files.readAllBytes(tempFile.toPath()));
        assertTrue(content.isEmpty());
    }

    @Test
    public void writeToXmlFile_InvalidPath() throws Exception {
        String xmlContent = "<root><element>value</element></root>";
        String invalidFileName = "/invalid/path/test.xml";

        xmlGenerator.writeToXmlFile(xmlContent, invalidFileName);

        File invalidFile = new File(invalidFileName);
        assertFalse(invalidFile.exists());
    }
}
