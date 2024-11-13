package digit.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class FileStorageUtil {

    private final RestTemplate restTemplate;

    private final Configuration config;

    private final ObjectMapper mapper;

    public FileStorageUtil(RestTemplate restTemplate, Configuration config, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.config = config;
        this.mapper = mapper;
    }

    public String saveDocumentToFileStore(ByteArrayResource byteArrayResource) {
        StringBuilder uri = new StringBuilder();
        uri.append(config.getFileStoreHost())
                .append(config.getFileStoreEndPoint())
                .append("?tenantId=").append(config.getEgovStateTenantId()).append("&module=").append(config.getSummonsFileStoreModule());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", byteArrayResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parts, headers);

        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(uri.toString(),
                requestEntity, Object.class);

        JsonNode rootNode = mapper.convertValue(responseEntity.getBody(), JsonNode.class);
        if (rootNode.has("files") && rootNode.get("files").isArray()
                && rootNode.get("files").get(0).isObject()) {
            return rootNode.get("files").get(0).get("fileStoreId").asText();
        } else {
            throw new CustomException("SUMMONS_FILE_STORE_ERROR", "Failed to get valid file store id from file store service");
        }
    }
}
