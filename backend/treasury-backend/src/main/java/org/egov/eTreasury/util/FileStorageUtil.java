package org.egov.eTreasury.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.eTreasury.config.PaymentConfiguration;
import org.egov.tracer.model.CustomException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.egov.eTreasury.config.ServiceConstants.*;

@Component
@Slf4j
public class FileStorageUtil {

    private final RestTemplate restTemplate;

    private final PaymentConfiguration config;

    private final ObjectMapper mapper;

    public FileStorageUtil(RestTemplate restTemplate, PaymentConfiguration config, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.config = config;
        this.mapper = mapper;
    }

    public Document saveDocumentToFileStore(byte[] payInSlipBytes) {

        try {
            String uri = buildFileStoreUri();

            ByteArrayResource byteArrayResource = new ByteArrayResource(payInSlipBytes) {
                @Override
                public String getFilename() {
                    return "file.pdf"; // Provide a filename
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
            throw new CustomException(FILESTORE_UTILITY_EXCEPTION, "Error occurred when getting saving document in File Store");
        }
    }

    private String buildFileStoreUri() {
        return new StringBuilder()
                .append(config.getFileStoreHost())
                .append(config.getFileStoreEndPoint())
                .append("?tenantId=").append(config.getEgovStateTenantId())
                .append("&module=").append(config.getTreasuryFileStoreModule())
                .toString();
    }

    private Document extractDocumentFromResponse(ResponseEntity<Object> responseEntity) {
        JsonNode rootNode = mapper.convertValue(responseEntity.getBody(), JsonNode.class);
        if (rootNode.has(FILES_KEY) && rootNode.get(FILES_KEY).isArray() && rootNode.get(FILES_KEY).get(0).isObject()) {
            Document document = new Document();
            document.setFileStore(rootNode.get(FILES_KEY).get(0).get(FILE_STORE_ID_KEY).asText());
            document.setDocumentType(DOCUMENT_TYPE_PDF);
            log.info("File Store Details: {}", document);
            return document;
        } else {
            throw new CustomException(INVALID_FILE_STORE_ID, "Failed to get valid file store id from file store service");
        }
    }
}
