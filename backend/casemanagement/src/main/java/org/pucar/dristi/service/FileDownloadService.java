package org.pucar.dristi.service;


import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.commons.codec.binary.Hex;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import static org.pucar.dristi.config.ServiceConstants.TEMP_DIR;

import org.pucar.dristi.web.models.VcCredentialRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;

@Service
@Slf4j
public class FileDownloadService {
    private Configuration configuration;

    public FileDownloadService(Configuration configuration){
        this.configuration=configuration;
    }

    public String downloadAndExtractSignature(VcCredentialRequest vcCredentialRequest)  {
        String tenantId= vcCredentialRequest.getTenantId();
        String fileStoreId= vcCredentialRequest.getFileStoreId();
        StringBuilder fileStoreSearch = new StringBuilder();
        fileStoreSearch.append(configuration.getFileStoreHost()).append(configuration.getFileStorePath());
        String fileStoreSearchUrl= fileStoreSearch.toString()+"?fileStoreIds=" + fileStoreId + "&tenantId=" + tenantId;
        String authToken = vcCredentialRequest.getRequestInfo().getAuthToken();
        // Step 1: Call the API to get the S3 URL
        String s3Url = getS3Url(fileStoreSearchUrl, authToken, tenantId);

        // Step 2: Download the file from the S3 URL
        File downloadedFile = downloadFileFromS3(s3Url);
        log.info("downloaded file"+downloadedFile);
        // Step 3: Extract the signature from the downloaded file
        String signedHashValue=extractSignature(downloadedFile);

        // Step 4: Delete the temporary file
        Path path = Paths.get(downloadedFile.getAbsolutePath());
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (Exception e) {
                // Log the exception or handle it accordingly
                throw new CustomException("FILE_DELETE_FAILED","Failed to delete file: " + e.getMessage());
            }
        }

        return signedHashValue;
    }


    public String getS3Url(String apiUrl, String authToken, String tenantId) {
        String s3Url=null;
        try{
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
                log.info("s3 url response"+ responseString);
                s3Url = JsonPath.parse(responseString).read("$.fileStoreIds[0].url", String.class);
            }
        }
        catch (Exception e){
            throw new CustomException("S3_URL_SEARCH_FAIL","Failed to get S3 URL");
        }
        return s3Url;
    }

    private void createSecureTempDir() throws IOException {
        Path tempDirPath = Paths.get(TEMP_DIR);
        if (!Files.exists(tempDirPath)) {
            Files.createDirectory(tempDirPath);
            // Set directory permissions to be accessible only by the owner
            Files.setPosixFilePermissions(tempDirPath, PosixFilePermissions.fromString("rwx------"));
        }
    }

    public File downloadFileFromS3(String s3Url) {
        File tempFile = null;
        try {
            createSecureTempDir();
        } catch (Exception e) {
            throw new CustomException("DIRECTORY_CREATION_FAILED", "Failed to create secure temporary directory" + e.getMessage());
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse closeableHttpResponse = httpClient.execute(new HttpGet(s3Url))) {

            HttpEntity entity = closeableHttpResponse.getEntity();
            if (entity != null) {
                try (InputStream inputStream = entity.getContent()) {
                    Path tempFilePath = Files.createTempFile(Paths.get(TEMP_DIR), "downloaded-", ".pdf");
                    tempFile = tempFilePath.toFile();
                    try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new CustomException("FILE_DOWNLOAD_FAILED", "Failed to download file from S3 URL" + e.getMessage());
        }
        return tempFile;
    }

    public String extractSignature(File pdfFile) {
        String signedHashValue=null;
        try (PDDocument document = PDDocument.load(pdfFile)) {
            List<PDSignature> signatureDictionaries = document.getSignatureDictionaries();
            for (PDSignature signature : signatureDictionaries) {
                COSDictionary sigDict = signature.getCOSObject();
                COSString contents = (COSString) sigDict.getDictionaryObject(COSName.CONTENTS);
                byte[] signedContent = contents.getBytes();
                // Assuming the hash is a part of the signed content, which typically is the case
                byte[] first32Bytes = new byte[32];
                System.arraycopy(signedContent, 0, first32Bytes, 0, 32);

                // Convert the first 32 bytes to hexadecimal string
                signedHashValue = Hex.encodeHexString(first32Bytes);

            }
        }
        catch (Exception e){
            throw new CustomException("SIGNATURE_EXTRACTION_FAILED","extracting digital signature from the pdf failed:"+e.getMessage());
        }
        return signedHashValue;
    }

}

