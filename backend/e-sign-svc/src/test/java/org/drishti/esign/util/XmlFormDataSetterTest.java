package org.drishti.esign.util;

import org.drishti.esign.config.Configuration;
import org.drishti.esign.web.models.ESignXmlData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class XmlFormDataSetterTest {

    @Mock
    private Configuration configuration;

    private XmlFormDataSetter xmlFormDataSetter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        xmlFormDataSetter = new XmlFormDataSetter(configuration);
    }

    @Test
    public void testSetFormXmlData() {
        // Setup mock configuration
        when(configuration.getVersion()).thenReturn("1.0");
        when(configuration.getConsent()).thenReturn("consent");
        when(configuration.getEkycIdType()).thenReturn("ekycType");
        when(configuration.getAspId()).thenReturn("aspId");
        when(configuration.getAuthMode()).thenReturn("authMode");
        when(configuration.getResponseSigType()).thenReturn("responseSigType");
        when(configuration.getResponseUrl()).thenReturn("responseUrl");
        when(configuration.getId()).thenReturn("id");
        when(configuration.getHashAlgorithm()).thenReturn("SHA-256");
        when(configuration.getDocInfo()).thenReturn("docInfo");

        String fileHash = "fileHash";
        ESignXmlData xmlData = new ESignXmlData();

        ESignXmlData result = xmlFormDataSetter.setFormXmlData(fileHash, xmlData);

        // Assert the values set in the ESignXmlData object
        assertEquals("1.0", result.getVer());
        assertEquals("consent", result.getSc());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String nowFormatted = dateFormat.format(new Date());
        assertEquals(nowFormatted, result.getTs());

        assertEquals("", result.getEkycId());
        assertEquals("ekycType", result.getEkycIdType());
        assertEquals("aspId", result.getAspId());
        assertEquals("authMode", result.getAuthMode());
        assertEquals("responseSigType", result.getResponseSigType());
        assertEquals("responseUrl", result.getResponseUrl());
        assertEquals("id", result.getId());
        assertEquals("SHA-256", result.getHashAlgorithm());
        assertEquals("docInfo", result.getDocInfo());
        assertEquals(fileHash, result.getDocHashHex());
    }
}
