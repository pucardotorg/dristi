package digit.util;

import digit.config.Configuration;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import static digit.config.ServiceConstants.*;

@Component
@Slf4j
public class PdfServiceUtil {

    private final RestTemplate restTemplate;

    private final Configuration config;

    @Autowired
    public PdfServiceUtil(RestTemplate restTemplate, Configuration config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public ByteArrayResource generatePdfFromPdfService(TaskRequest taskRequest, String tenantId,
                                                       String pdfTemplateKey, boolean qrCode) {
        try {
            StringBuilder uri = new StringBuilder();
            uri.append(config.getPdfServiceHost())
                    .append(config.getPdfServiceEndpoint())
                    .append("?tenantId=").append(tenantId).append("&key=").append(pdfTemplateKey);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            SummonsPdf summonsPdf = createSummonsPdfFromTask(taskRequest.getTask());
            if (qrCode && taskRequest.getTask().getDocuments() != null && !taskRequest.getTask().getDocuments().isEmpty()) {
                Document document = taskRequest.getTask().getDocuments().get(0);
                if (document != null) {
                    String embeddedUrl = config.getFileStoreHost() + config.getFileStoreSearchEndPoint() + "?tenantId=" + tenantId + "&fileStoreId=" + document.getFileStore();
                    summonsPdf.setEmbeddedUrl(embeddedUrl);

                }
            }
            log.info("Summons Pdf: {}", summonsPdf);
            SummonsPdfRequest summonsPdfRequest = SummonsPdfRequest.builder()
                    .summonsPdf(summonsPdf).requestInfo(taskRequest.getRequestInfo()).build();
            HttpEntity<SummonsPdfRequest> requestEntity = new HttpEntity<>(summonsPdfRequest, headers);

            ResponseEntity<ByteArrayResource> responseEntity = restTemplate.postForEntity(uri.toString(),
                    requestEntity, ByteArrayResource.class);

            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("Error getting response from Pdf Service", e);
            throw new CustomException("SU_PDF_APP_ERROR", "Error getting response from Pdf Service");
        }
    }

    private SummonsPdf createSummonsPdfFromTask(Task task) {
        Long issueDate = null;
        if (NOTICE.equals(task.getTaskType())) {
            issueDate = task.getTaskDetails().getNoticeDetails().getIssueDate();
        } else if (SUMMON.equals(task.getTaskType())) {
            issueDate = task.getTaskDetails().getSummonDetails().getIssueDate();
        }
        String issueDateString = Optional.ofNullable(issueDate)
                .map(this::formatDateFromMillis)
                .orElse("");
        String hearingDateString = Optional.ofNullable(task.getTaskDetails().getCaseDetails().getHearingDate())
                .map(this::formatDateFromMillis)
                .orElse("");
        String filingNumber = task.getFilingNumber();
        String courtName = task.getTaskDetails().getCaseDetails().getCourtName();
        return SummonsPdf.builder()
                .tenantId(task.getTenantId())
                .cnrNumber(task.getCnrNumber())
                .filingNumber(filingNumber)
                .issueDate(issueDateString)
                .caseName(task.getTaskDetails().getCaseDetails().getCaseTitle())
                .caseNumber(extractCaseNumber(filingNumber))
                .caseYear(extractCaseYear(filingNumber))
                .judgeName(task.getTaskDetails().getCaseDetails().getJudgeName())
                .courtName(courtName == null ? config.getCourtName(): courtName)
                .hearingDate(hearingDateString)
                .respondentName(task.getTaskDetails().getRespondentDetails().getName())
                .respondentAddress(task.getTaskDetails().getRespondentDetails().getAddress().toString())
                .complainantName(task.getTaskDetails().getComplainantDetails().getName())
                .complainantAddress(task.getTaskDetails().getComplainantDetails().getAddress().toString())
                .build();
    }

    private String extractCaseYear(String input) {
        if (input == null) {
            return "";
        }
        String regex = "-(\\d{4})$";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String numberStr = matcher.group(1);
            return numberStr.replaceFirst("^0+(?!$)", "");
        } else {
            return  "";
        }
    }

    public static String extractCaseNumber(String input) {
        if (input == null) {
            return "";
        }
        String regex = "-(\\d{6})-";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    private String formatDateFromMillis(long millis) {
        try {
            ZonedDateTime dateTime = Instant.ofEpochMilli(millis)
                    .atZone(ZoneId.of("Asia/Kolkata"));

            String day = String.valueOf(dateTime.getDayOfMonth());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);
            String formattedMonthYear = dateTime.format(formatter);

            return String.format("%s day of %s", day, formattedMonthYear);
        } catch (Exception e) {
            log.error("Failed to get Date in String format from : {}", millis);
            return "";
        }
    }
}
