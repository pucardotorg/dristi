package org.pucar.dristi.util;

import org.pucar.dristi.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FileStoreUtil {

    @Autowired
    private Configuration configs;

    private RestTemplate restTemplate;

    @Autowired
    public FileStoreUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Boolean fileStore(String tenantId,  String FileStoreId) {
        try{
            StringBuilder uri = new StringBuilder(configs.getFileStoreHost()).append(configs.getFileStorePath());
            uri.append("tenantId=").append(tenantId).append("&").append("fileStoreId=").append(FileStoreId);
            ResponseEntity<String> responseEntity= restTemplate.getForEntity(uri.toString(), String.class);
            return responseEntity.getStatusCode().equals(HttpStatus.OK);
        }catch (Exception e){
            return false;
        }

    }
}
