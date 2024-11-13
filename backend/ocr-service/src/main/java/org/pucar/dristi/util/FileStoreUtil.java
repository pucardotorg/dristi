package org.pucar.dristi.util;

import lombok.extern.slf4j.Slf4j;
import org.pucar.dristi.config.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
@Slf4j
public class FileStoreUtil {

    private Properties properties;

    private RestTemplate restTemplate;

    @Autowired
    public FileStoreUtil(RestTemplate restTemplate, Properties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public Resource getFileFromStore(String fileStoreId) {
        Resource resource = null;
        try {
            StringBuilder uri = new StringBuilder(properties.getFileStoreHost()).append(properties.getFileStoreEndpoint());
            uri.append("?tenantId=").append(properties.getStateLevelTenantId()).append("&").append("fileStoreId=").append(fileStoreId);
            ResponseEntity<Resource> responseEntity = restTemplate.getForEntity(uri.toString(), Resource.class);
            resource = responseEntity.getBody();
        } catch (Exception e) {
            log.error("Document {} is not found in the Filestore for tenantId {} ! An exception occurred!",
                    fileStoreId,
                    properties.getStateLevelTenantId(),
                    e);
        }
        return resource;
    }
}
