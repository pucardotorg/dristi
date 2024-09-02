package org.drishti.esign.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.drishti.esign.config.Configuration;
import org.drishti.esign.repository.ServiceRequestRepository;
import org.drishti.esign.web.models.StorageResponse;
import org.egov.tracer.model.CustomException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileStoreUtil {


    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final Configuration configs;
    private final ServiceRequestRepository serviceRepository;

    @Autowired
    public FileStoreUtil(RestTemplate restTemplate, ObjectMapper mapper, Configuration configs, ServiceRequestRepository serviceRepository) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.configs = configs;
        this.serviceRepository = serviceRepository;
    }


    public Resource fetchFileStoreObjectById(String fileStoreId, String tenantId) {

        StringBuilder uri = new StringBuilder();
        uri.append(configs.getFilestoreHost()).append(configs.getFilestoreSearchEndPoint());
        uri = appendQueryParams(uri, "fileStoreId", fileStoreId, "tenantId", tenantId);
        Resource object;
        try {
            object = restTemplate.getForObject(uri.toString(), Resource.class);
            return object;

        } catch (Exception e) {
            throw new CustomException("FILESTORE_SERVICE_EXCEPTION", "exception occurred while calling filestore service");

        }


    }

    public StringBuilder appendQueryParams(StringBuilder uri, String paramName1, String paramValue1, String paramName2, String paramValue2) {
        if (uri.indexOf("?") == -1) {
            uri.append("?");
        } else {
            uri.append("&");
        }
        uri.append(paramName1).append("=").append(paramValue1).append("&");
        uri.append(paramName2).append("=").append(paramValue2);
        return uri;
    }


    public String storeFileInFileStore(MultipartFile file, String tenantId) {

        if (!FileValidationUtil.isValidFile(file)) {
            throw new IllegalArgumentException("Invalid file type");
        }
        String module = "signed";  // fixme: take me from constant file
        StringBuilder uri = new StringBuilder();
        uri.append(configs.getFilestoreHost()).append(configs.getFilestoreCreateEndPoint());

        List<MultipartFile> files = new ArrayList<>();
        files.add(file);

        MultiValueMap<String, Object> request = new LinkedMultiValueMap<>();
        request.add("file", file.getResource());
        request.add("tenantId", tenantId);
        request.add("module", module);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(request, headers);


        ResponseEntity<String> response = restTemplate.exchange(uri.toString(), HttpMethod.POST, entity, String.class);
        String body = response.getBody();
        JSONObject jsonObject = new JSONObject(body);
        JSONObject fileObject = jsonObject.getJSONArray("files").getJSONObject(0);
        return fileObject.getString("fileStoreId");


    }


}
