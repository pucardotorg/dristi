package org.pucar.dristi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;

import org.pucar.dristi.web.models.QrCodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@Service
public class QrCodeImageService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    public String getQrCodeImage(String referenceId) throws JsonProcessingException {
        String url = "http://localhost:8080/sunbirdrc-credential-service/qrcode/_get";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        QrCodeRequest qrCodeRequest= QrCodeRequest.builder()
                .code("Pucar.SummonsOrder")
                .uuid(referenceId)
                .build();
        String qrstring= objectMapper.writeValueAsString(qrCodeRequest);
        System.out.println("qr code request object"+qrstring);
        HttpEntity<QrCodeRequest> entity = new HttpEntity<>(qrCodeRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        String qrImage=extractImageFromResponse(response.getBody());
        System.out.println("qr image response is"+ qrImage);
        return qrImage;
    }

    public String extractImageFromResponse(String qrResponse){
        Document doc = Jsoup.parse(qrResponse);
        Element img = doc.select("img").first();
            String src = img.attr("src");
            return src;
    }
}
