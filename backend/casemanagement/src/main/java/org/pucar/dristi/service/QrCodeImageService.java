package org.pucar.dristi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.protocol.types.Field;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.jsoup.Jsoup;

import org.pucar.dristi.web.models.PdfRequest;
import org.pucar.dristi.web.models.QrCodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@Service
public class QrCodeImageService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${egov.credential.host}")
    private String credentialHost;
    @Value("${egov.credential.url}")
    private String credentialUrl;


    @Autowired
    private ObjectMapper objectMapper;
    public String getQrCodeImage(PdfRequest pdfRequestObject) {
        String referenceId= pdfRequestObject.getReferenceId();
        RequestInfo requestInfo= pdfRequestObject.getRequestInfo();
        StringBuilder requestQrImageUrl = new StringBuilder();
        requestQrImageUrl.append(credentialHost).append(credentialUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        QrCodeRequest qrCodeRequest= QrCodeRequest.builder()
                .code("Pucar.SummonsOrder1")
                .uuid(referenceId)
                .requestInfo(requestInfo)
                .build();

        String qrImage=null;
        try{
            HttpEntity<QrCodeRequest> entity = new HttpEntity<>(qrCodeRequest, headers);
            ResponseEntity<String> response = restTemplate.exchange(requestQrImageUrl.toString(), HttpMethod.POST, entity, String.class);
            qrImage=extractImageFromResponse(response.getBody());
        }
        catch (Exception e){
            throw new CustomException("QR_CODE_RESPONSE_ERROR","there was an issue fetching the qr code");
        }
        return qrImage;
    }

    public String extractImageFromResponse(String qrResponse){
        try{
            Document doc = Jsoup.parse(qrResponse);
            Element img = doc.select("img").first();
            String src = img.attr("src");
            return src;
        }
        catch(Exception e){
            throw new CustomException("ERROR_PARSING_QRCODE_RESPONSE","there was error fetching image from the qr response");
        }

    }
}
