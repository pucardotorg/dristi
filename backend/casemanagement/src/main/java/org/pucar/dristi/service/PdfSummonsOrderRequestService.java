package org.pucar.dristi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.pucar.dristi.config.Configuration;
import static org.pucar.dristi.config.ServiceConstants.FILE_STORE_MAPPER_KEY;
import static org.pucar.dristi.config.ServiceConstants.JSON_PARSING_ERR;
import org.pucar.dristi.repository.PdfResponseRepository;
import org.pucar.dristi.repository.ServiceRequestRepository;
import org.pucar.dristi.web.models.PdfRequest;
import org.pucar.dristi.web.models.PdfSummonsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class PdfSummonsOrderRequestService {
    private final ObjectMapper objectMapper;
    private final ServiceRequestRepository serviceRequestRepository;
    private final PdfResponseRepository referenceIdMapperRepository;
    private final StringRedisTemplate stringRedisTemplate;

    private final Configuration configuration;

    @Autowired
    public PdfSummonsOrderRequestService(
            ObjectMapper objectMapper,
            ServiceRequestRepository serviceRequestRepository,
            PdfResponseRepository referenceIdMapperRepository,
            StringRedisTemplate stringRedisTemplate,
            Configuration configuration) {
        this.objectMapper = objectMapper;
        this.serviceRequestRepository = serviceRequestRepository;
        this.referenceIdMapperRepository = referenceIdMapperRepository;
        this.stringRedisTemplate = stringRedisTemplate;
        this.configuration=configuration;
    }

    @Value("${egov.pdf.create}")
    private String generatePdfUrl;

    @Value("${egov.pdf.host}")
    private String generatePdfHost;

    @PostConstruct
    public void loadCache() {
        log.info("Loading data into cache from database");
        List<Map<String, Object>> rows = referenceIdMapperRepository.findAll();
        for (Map<String, Object> row : rows) {
            try {
                String referenceId = (String) row.get("referenceId");
                Object jsonResponseObject = row.get("jsonResponse");
                if (jsonResponseObject instanceof PGobject pgObject) {
                    String jsonResponse = pgObject.getValue();
                    stringRedisTemplate.opsForHash().put(FILE_STORE_MAPPER_KEY, referenceId, jsonResponse);
                } else {
                    log.error("jsonResponse is not an instance of PGobject for referenceId: {}", referenceId);
                    throw new CustomException("CACHE_LOADING_ERROR", "jsonResponse is not an instance of PGobject");
                }
            } catch (Exception e) {
                log.error("Error loading data into cache for referenceId: {}", row.get("referenceId"), e);
                throw new CustomException("CACHE_LOADING_ERROR", "error loading data into cache");
            }
        }
    }

    public Object createPdf(Object requestObject, PdfRequest pdfRequestobject) {
        String referenceId=pdfRequestobject.getReferenceId();
        String refCode=pdfRequestobject.getReferenceCode();
        String tenantId= pdfRequestobject.getTenantId();
        // Step 1: Check the cache
        String cachedJsonResponse = (String) stringRedisTemplate.opsForHash().get(FILE_STORE_MAPPER_KEY, referenceId);
        if (cachedJsonResponse != null) {
            log.info("Cache hit for referenceId: {}", referenceId);
            try{
                return objectMapper.readValue(cachedJsonResponse, Object.class);
            }
            catch (Exception e){
                throw new CustomException(JSON_PARSING_ERR,"error while serializing cache data");
            }
        }

        // Step 2: Check the database if cache miss
        Optional<String> dbJsonResponse = referenceIdMapperRepository.findJsonResponseByReferenceId(referenceId);
        if (dbJsonResponse.isPresent()) {
            log.info("Database hit for referenceId: {}", referenceId);
            // Update cache
            stringRedisTemplate.opsForHash().put(FILE_STORE_MAPPER_KEY, referenceId, dbJsonResponse.get());
            try{
                return objectMapper.readValue(dbJsonResponse.get(), Object.class);
            }
            catch (Exception e){
                throw new CustomException(JSON_PARSING_ERR,"error while serializing cache data");
            }
        }

        // Step 3: Create PDF if not found
        log.info("Creating PDF for referenceId: {}", referenceId);
        StringBuilder requestUrl = new StringBuilder();
        requestUrl.append(configuration.getGeneratePdfHost()).append(configuration.getGeneratePdfUrl()).append("?key=").append(refCode)
                .append("&tenantId=").append(tenantId);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestObject, headers);
        Object pdfResponse = serviceRequestRepository.fetchResult(requestUrl, requestEntity);
        // Convert pdfResponse to JSON
        String jsonResponse=null;
        try{
            jsonResponse = objectMapper.writeValueAsString(pdfResponse);
        }
        catch (Exception e){
            throw new CustomException(JSON_PARSING_ERR,"error parsing the json response");
        }
        // Step 4: Store JSON in database and cache on success
        log.info("Storing JSON response in database and updating cache for referenceId: {}", referenceId);
        if(jsonResponse!=null){
            referenceIdMapperRepository.saveJsonResponse(referenceId, jsonResponse);
            stringRedisTemplate.opsForHash().put(FILE_STORE_MAPPER_KEY, referenceId, jsonResponse);
        }

        return pdfResponse;
    }
}
