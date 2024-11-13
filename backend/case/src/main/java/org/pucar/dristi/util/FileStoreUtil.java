package org.pucar.dristi.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.models.Document;
import org.egov.common.models.individual.AdditionalFields;
import org.egov.common.models.individual.Field;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

import static org.pucar.dristi.config.ServiceConstants.FILE_STORE_UTILITY_EXCEPTION;


@Component
@Slf4j
public class FileStoreUtil {

    private static final String FILE_STORE_ID_KEY = "fileStoreId";
    private static final String FILES_KEY = "files";
    private static final String DOCUMENT_TYPE_PDF = "application/pdf";

    private Configuration configs;

    private RestTemplate restTemplate;

    private final ObjectMapper mapper;

    @Autowired
    public FileStoreUtil(RestTemplate restTemplate, Configuration configs, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.configs = configs;
        this.mapper = mapper;
    }

    /**
     * Returns whether the file exists or not in the filestore.
     * @param tenantId
     * @param fileStoreId
     * @return
     */
    public boolean doesFileExist(String tenantId,  String fileStoreId) {
    		boolean fileExists = false;
        try{
            StringBuilder uri = new StringBuilder(configs.getFileStoreHost()).append(configs.getFileStorePath());
            uri.append("tenantId=").append(tenantId).append("&").append("fileStoreId=").append(fileStoreId);
            ResponseEntity<String> responseEntity= restTemplate.getForEntity(uri.toString(), String.class);
            fileExists = responseEntity.getStatusCode().equals(HttpStatus.OK);
        }catch (Exception e){
        		log.error("Document {} is not found in the Filestore for tenantId {} ! An exception occurred!", 
        			  fileStoreId, 
        			  tenantId, 
        			  e);
        }
        return fileExists;
    }

    public Document saveDocumentToFileStore(byte[] payInSlipBytes, String tenantId) {

        try {
            String uri = buildFileStoreUri(tenantId);

            ByteArrayResource byteArrayResource = new ByteArrayResource(payInSlipBytes) {
                @Override
                public String getFilename() {
                    return "file.pdf";
                }
            };

            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Create the request body
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", byteArrayResource);

            // Build the entity
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<Object> responseEntity = restTemplate.postForEntity(uri, requestEntity, Object.class);

            return extractDocumentFromResponse(responseEntity);
        } catch (Exception e) {
            log.error("Error while saving document to file store: {}", e.getMessage(), e);
            throw new CustomException(FILE_STORE_UTILITY_EXCEPTION, "Error occurred when getting saving document in File Store");
        }
    }

    private String buildFileStoreUri(String tenantId) {
        return new StringBuilder()
                .append(configs.getFileStoreHost())
                .append(configs.getFileStoreSaveEndPoint())
                .append("?tenantId=").append(tenantId)
                .append("&module=").append(configs.getFileStoreCaseModule())
                .toString();
    }

    private Document extractDocumentFromResponse(ResponseEntity<Object> responseEntity) {
        JsonNode rootNode = mapper.convertValue(responseEntity.getBody(), JsonNode.class);
        if (rootNode.has(FILES_KEY) && rootNode.get(FILES_KEY).isArray() && rootNode.get(FILES_KEY).get(0).isObject()) {
            Document document = new Document();
            document.setFileStore(rootNode.get(FILES_KEY).get(0).get(FILE_STORE_ID_KEY).asText());
            document.setDocumentType(DOCUMENT_TYPE_PDF);
            Field field = Field.builder().key("FILE_CATEGORY").value("CASE_GENERATED_DOCUMENT").build();
            AdditionalFields additionalFields = AdditionalFields.builder().fields(Collections.singletonList(field)).build();
            document.setAdditionalDetails(additionalFields);
            log.info("File Store Details: {}", document);
            return document;
        } else {
            log.error("Failed to get valid file store id from File Store Service Response");
            throw new CustomException("INVALID_FILE_STORE_RESPONSE", "Failed to get valid file store id from file store service");
        }
    }
}
