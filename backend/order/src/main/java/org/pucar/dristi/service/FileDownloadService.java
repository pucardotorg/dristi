package org.pucar.dristi.service;


import com.jayway.jsonpath.JsonPath;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.List;

@Service
public class FileDownloadService {

    @Autowired
    private ServiceUrlMapperVCService serviceUrlMapperService;

    @Autowired
    private ServiceUrlEntityRequestService serviceUrlEntityRequestService;

    public String downloadAndExtractSignature() throws Exception {
        String url = "https://unified-dev.digit.org/filestore/v1/files/url?fileStoreIds=3fa283c5-0c3a-4912-9e57-54ca03bcebc0&tenantId=pg";
        String authToken = "e43eb826-c4d0-4c5b-b0b3-337b25d004d6";
        String tenantId = "pg";

        // Step 1: Call the API to get the S3 URL
        String s3Url = getS3Url(url, authToken, tenantId);

        // Step 2: Download the file from the S3 URL
        File downloadedFile = downloadFileFromS3(s3Url);
        System.out.println("downloaded file"+downloadedFile);
        // Step 3: Extract the signature from the downloaded file
        Calendar signDate=extractSignature(downloadedFile);

        // Step 4: Delete the temporary file
        if (downloadedFile.exists()) {
            downloadedFile.delete();
        }
        String referenceId="33ed5611-6c08-4d43-a78f-4eb7ad486a7f";
        String refCode="summon";
        String serviceUrl=serviceUrlMapperService.getSVcUrlMapping(refCode);
        serviceUrlEntityRequestService.getEntityDetails(signDate,serviceUrl, referenceId);
        return "VC generated";
    }


    private String getS3Url(String apiUrl, String authToken, String tenantId) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(apiUrl);
        request.setHeader("auth-token", authToken);
        request.setHeader("tenantId", tenantId);

        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            InputStream inputStream = entity.getContent();
            String responseString = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            inputStream.close();
            // Parse the JSON response using JsonPath to get the S3 URL
            String s3Url = JsonPath.parse(responseString).read("$.3fa283c5-0c3a-4912-9e57-54ca03bcebc0", String.class);
            return s3Url;
        }

        throw new Exception("Failed to get S3 URL");
    }

    private File downloadFileFromS3(String s3Url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(s3Url);

        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            InputStream inputStream = entity.getContent();
            File tempFile = Files.createTempFile("downloaded", ".pdf").toFile();
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            int read;
            byte[] buffer = new byte[1024];
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }

            outputStream.close();
            inputStream.close();
            return tempFile;
        }

        throw new Exception("Failed to download file from S3 URL");
    }

    private Calendar extractSignature(File pdfFile) throws Exception {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            List<PDSignature> signatures = document.getSignatureDictionaries();
            Calendar signDate=null;
            if (signatures.isEmpty()) {
                throw new Exception("No digital signatures found in the PDF.");
            }
            for (PDSignature signature : signatures) {
                // Print out details of each signature
                System.out.println("Signature found:");
                System.out.println("Name: " + signature.getName());
                System.out.println("Contact Info: " + signature.getContactInfo());
                System.out.println("Location: " + signature.getLocation());
                System.out.println("Reason: " + signature.getReason());
                System.out.println("Signature Date: " + signature.getSignDate());
                signDate=signature.getSignDate();
            }
            return signDate;
        }
    }
}

