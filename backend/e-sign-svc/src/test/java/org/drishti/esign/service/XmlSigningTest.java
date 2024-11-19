package org.drishti.esign.service;

import org.drishti.esign.cipher.Encryption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.w3c.dom.Document;

import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collections;

import static org.drishti.esign.config.ServiceConstants.PUBLIC_KEY_FILE_NAME;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class XmlSigningTest {

    @InjectMocks
    private XmlSigning xmlSigning;

    @Mock
    private Document document;

    @Mock
    private XMLSignatureFactory xmlSignatureFactory;

    @Mock
    private PublicKey publicKey;

    @Mock
    private Encryption encryption;

    @Mock
    private KeyInfoFactory keyInfoFactory;

    @Mock
    private KeyValue keyValue;

    @Mock
    private TransformerFactory transformerFactory;

    @Mock
    private Transformer transformer;

    @Test
    public void signXmlStringNew_NullInput() {
        PrivateKey privateKey = mock(PrivateKey.class);

        Exception exception = assertThrows(Exception.class, () -> {
            xmlSigning.signXmlStringNew(null, privateKey);
        });

        Assertions.assertNotNull(exception);
    }

    @Test
    public void signXmlStringNew_InvalidXml() {
        String invalidXml = "<root><element>value</element>";
        PrivateKey privateKey = mock(PrivateKey.class);

        Exception exception = assertThrows(Exception.class, () -> {
            xmlSigning.signXmlStringNew(invalidXml, privateKey);
        });

        Assertions.assertNotNull(exception);
    }

    @Test
    public void parseXml_ValidResponse() throws Exception {
        String esignResponse = "<root><DocSignature>signature</DocSignature></root>";

        String signature = xmlSigning.parseXml(esignResponse);

        Assertions.assertEquals("signature", signature);
    }

    @Test
    public void parseXml_InvalidResponse() throws Exception {
        String invalidResponse = "<root><DocSignature>signature</DocSignature>";

        Exception exception = assertThrows(Exception.class, () -> {
            xmlSigning.parseXml(invalidResponse);
        });

        Assertions.assertNotNull(exception);
    }

    @Test
    public void parseXml_EmptyResponse() {
        String emptyResponse = "";

        Exception exception = assertThrows(Exception.class, () -> {
            xmlSigning.parseXml(emptyResponse);
        });

        Assertions.assertNotNull(exception);
    }

    @Test
    public void storeSignedDoc_Test() {
        xmlSigning.storeSignedDoc(document,"signed.xml");
    }

    @Test
    public void testGetKeyInfoSuccess() throws Exception {
        when(encryption.getPublicKey(anyString())).thenReturn(publicKey);
        when(xmlSignatureFactory.getKeyInfoFactory()).thenReturn(keyInfoFactory);
        when(keyInfoFactory.newKeyValue(publicKey)).thenReturn(keyValue);
        when(keyInfoFactory.newKeyInfo(Collections.singletonList(keyValue))).thenReturn(mock(KeyInfo.class));

        xmlSigning.getKeyInfo(xmlSignatureFactory, PUBLIC_KEY_FILE_NAME);
        verify(encryption).getPublicKey(anyString());
        verify(keyInfoFactory).newKeyValue(publicKey);
        verify(keyInfoFactory).newKeyInfo(Collections.singletonList(keyValue));
    }


}
