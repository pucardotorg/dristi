package digit.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.web.models.PdfRequest;
import digit.web.models.TaskRequest;
import digit.web.models.VcCredentialRequest;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class CaseManagementUtil {

    private final RestTemplate restTemplate;

    private final Configuration config;

    private final ObjectMapper objectMapper;

    private final OrderUtil orderUtil;

    @Autowired
    public CaseManagementUtil(RestTemplate restTemplate, Configuration config, ObjectMapper objectMapper, OrderUtil orderUtil) {
        this.restTemplate = restTemplate;
        this.config = config;
        this.objectMapper = objectMapper;
        this.orderUtil = orderUtil;
    }

    public void generateVcForTask(TaskRequest taskRequest, String moduleName) {
        try {
            StringBuilder uri = new StringBuilder();
            uri.append(config.getPdfServiceHost())
                    .append(config.getBffServiceVCEndpoint());

            String referenceId = taskRequest.getTask().getId().toString();
            String orderId = taskRequest.getTask().getOrderId().toString();
            String tenantId = taskRequest.getTask().getTenantId();
            String filestoreId = orderUtil.fetchSignedFileStore(orderId, tenantId, taskRequest.getRequestInfo());
            VcCredentialRequest credentialRequest = VcCredentialRequest.builder()
                    .requestInfo(taskRequest.getRequestInfo())
                    .tenantId(tenantId)
                    .moduleName(moduleName)
                    .referenceId(referenceId)
                    .fileStoreId(filestoreId)
                    .build();



            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<VcCredentialRequest> requestEntity = new HttpEntity<>(credentialRequest, headers);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(uri.toString(),
                    requestEntity, String.class);

        } catch (Exception e) {
            log.error("Error getting response from Task Service", e);
            throw new CustomException("TASK_UPDATE_ERROR", "Error getting response from task Service");
        }
    }

    public String getFileStoreIdFromBffService(TaskRequest taskRequest, String templateKey, String moduleName) {
        try {
            StringBuilder uri = new StringBuilder();
            uri.append(config.getPdfServiceHost())
                    .append(config.getBffServicePdfEndpoint());

            PdfRequest pdfRequest = PdfRequest.builder()
                    .requestInfo(taskRequest.getRequestInfo())
                    .referenceId(taskRequest.getTask().getId().toString())
                    .referenceCode(templateKey)
                    .moduleName(moduleName)
                    .tenantId(taskRequest.getTask().getTenantId())
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<PdfRequest> requestEntity = new HttpEntity<>(pdfRequest, headers);

            ResponseEntity<Object> responseEntity = restTemplate.postForEntity(uri.toString(),
                    requestEntity, Object.class);

            JsonNode rootNode = objectMapper.valueToTree(responseEntity.getBody());
            JsonNode fileStoreIdsNode = rootNode.get("filestoreIds");

            if (fileStoreIdsNode != null && fileStoreIdsNode.isArray() && !fileStoreIdsNode.isEmpty()) {
                return fileStoreIdsNode.get(0).asText();
            } else {
                throw new CustomException("SU_PDF_APP_ERROR", "No filestoreIds found in the response");
            }

        } catch (Exception e) {
            log.error("Error getting response from Pdf Service", e);
            throw new CustomException("SU_PDF_APP_ERROR", "Error getting response from Pdf Service");
        }
    }
}
