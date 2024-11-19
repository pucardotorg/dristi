package org.drishti.esign.web.controllers;

import org.drishti.esign.service.ESignService;
import org.drishti.esign.util.ResponseInfoFactory;
import org.drishti.esign.web.models.ESignRequest;
import org.drishti.esign.web.models.ESignResponse;
import org.drishti.esign.web.models.ESignXmlForm;
import org.drishti.esign.web.models.SignDocRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EsignApiControllerTest {

    @Mock
    private ESignService eSignService;

    @InjectMocks
    private EsignApiController esignApiController;

    @BeforeEach
    public void setup() {
        esignApiController = new EsignApiController(eSignService);
    }

    @Test
    public void testSignDoc() {
        ESignRequest request = new ESignRequest();
        ESignXmlForm eSignXmlForm = new ESignXmlForm();
        ESignResponse expectedResponse = ESignResponse.builder()
                .responseInfo(ResponseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true))
                .eSignForm(eSignXmlForm).build();

        when(eSignService.signDoc(request)).thenReturn(eSignXmlForm);

        ResponseEntity<ESignResponse> responseEntity = esignApiController.eSignDoc(request);

        assertEquals(ResponseEntity.accepted().body(expectedResponse), responseEntity);
        verify(eSignService, times(1)).signDoc(request);
    }

    @Test
    public void testSignedDoc() {
        SignDocRequest request = new SignDocRequest();
        String fileStoreId = "testFileStoreId";

        when(eSignService.signDocWithDigitalSignature(request)).thenReturn(fileStoreId);

        ResponseEntity<String> responseEntity = esignApiController.signedDoc(request);

        assertEquals(ResponseEntity.accepted().body(fileStoreId), responseEntity);
        verify(eSignService, times(1)).signDocWithDigitalSignature(request);
    }

}
