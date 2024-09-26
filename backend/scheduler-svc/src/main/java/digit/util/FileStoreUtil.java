package digit.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static digit.config.ServiceConstants.FILE_STORE_UTILITY_EXCEPTION;


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

    public byte[] getFile(String tenantId,  String fileStoreId) {
        byte[] pdfBytes = null;
        try{
            StringBuilder uri = new StringBuilder(configs.getFileStoreHost()).append(configs.getFileStorePath());
            uri.append("tenantId=").append(tenantId).append("&").append("fileStoreId=").append(fileStoreId);
            ResponseEntity<Resource> responseEntity= restTemplate.getForEntity(uri.toString(), Resource.class);
            return responseEntity.getBody().getContentAsByteArray();
        }catch (Exception e){
            log.error("Document {} is not found in the Filestore for tenantId {} ! An exception occurred!",
                    fileStoreId,
                    tenantId,
                    e);
        }
        return pdfBytes;
    }

    public Document saveDocumentToFileStore(ByteArrayResource byteArrayResource, String tenantId) {

        try {
            String uri = buildFileStoreUri(tenantId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", byteArrayResource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<Object> responseEntity = restTemplate.postForEntity(uri, requestEntity, Object.class);

            return extractDocumentFromResponse(responseEntity);
        } catch (Exception e) {
            log.error("Error while saving document to file store: {}", e.getMessage(), e);
            throw new CustomException(FILE_STORE_UTILITY_EXCEPTION, "Error occurred when getting saving document in File Store");
        }
    }

    private String buildFileStoreUri(String tenantId) {
        return configs.getFileStoreHost() +
                configs.getFileStoreSaveEndPoint() +
                "?tenantId=" + tenantId +
                "&module=" + configs.getFileStoreCauseListModule();
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
            log.error("Failed to get valid file store id from File Store Service Response");
            throw new CustomException("INVALID_FILE_STORE_RESPONSE", "Failed to get valid file store id from file store service");
        }
    }
}

