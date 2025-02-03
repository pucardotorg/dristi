package digit.service;

import digit.config.Configuration;
import digit.enrichment.ADiaryEnrichment;
import digit.kafka.Producer;
import digit.repository.DiaryRepository;
import digit.util.CaseUtil;
import digit.util.FileStoreUtil;
import digit.util.PdfServiceUtil;
import digit.validators.ADiaryValidator;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.models.Workflow;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.text.SimpleDateFormat;

import static digit.config.ServiceConstants.*;

@Service
@Slf4j
public class DiaryService {

    private final Producer producer;

    private final Configuration configuration;

    private final DiaryRepository diaryRepository;

    private final ADiaryValidator validator;

    private final ADiaryEnrichment enrichment;

    private final DiaryEntryService diaryEntryService;

    private final FileStoreUtil fileStoreUtil;

    private final PdfServiceUtil pdfServiceUtil;

    private final WorkflowService workflowService;

    private final CaseUtil caseUtil;

    public DiaryService(Producer producer, Configuration configuration, DiaryRepository diaryRepository, ADiaryValidator validator, ADiaryEnrichment enrichment, DiaryEntryService diaryEntryService, FileStoreUtil fileStoreUtil, PdfServiceUtil pdfServiceUtil, WorkflowService workflowService, CaseUtil caseUtil) {
        this.producer = producer;
        this.configuration = configuration;
        this.diaryRepository = diaryRepository;
        this.validator = validator;
        this.enrichment = enrichment;
        this.diaryEntryService = diaryEntryService;
        this.fileStoreUtil = fileStoreUtil;
        this.pdfServiceUtil = pdfServiceUtil;
        this.workflowService = workflowService;
        this.caseUtil = caseUtil;
    }

    public List<CaseDiaryListItem> searchCaseDiaries(CaseDiarySearchRequest searchRequest) {

        log.info("operation = searchCaseDiaries ,  result = IN_PROGRESS , CaseDiarySearchRequest : {} ", searchRequest);
        try {
            if (searchRequest == null || searchRequest.getCriteria() == null) {
                return null;
            }
            log.info("operation = searchDiaryEntries ,  result = SUCCESS , caseDiaryEntryRequest : {} ", searchRequest);
            return diaryRepository.getCaseDiaries(searchRequest);
        } catch (CustomException e) {
            log.error("Custom exception while searching");
            throw e;
        } catch (Exception e) {
            throw new CustomException(DIARY_SEARCH_EXCEPTION, "Error while searching");
        }

    }

    public CaseDiary updateDiary(CaseDiaryRequest caseDiaryRequest) {
        log.info("operation = updateDiaryEntry ,  result = IN_PROGRESS , caseDiaryEntryRequest : {} ", caseDiaryRequest);

        try {
            validator.validateUpdateDiary(caseDiaryRequest);

            enrichment.enrichUpdateCaseDiary(caseDiaryRequest);


            Workflow workflow = caseDiaryRequest.getDiary().getWorkflow();

            if (workflow == null) {
                Workflow workflowWithSignAction = Workflow.builder().action(SIGN_ACTION).build();
                caseDiaryRequest.getDiary().setWorkflow(workflowWithSignAction);
            }

            workflowService.updateWorkflowStatus(caseDiaryRequest);

            producer.push(configuration.getDiaryUpdateTopic(), caseDiaryRequest);

        } catch (CustomException e) {
            log.error("Custom exception occurred while updating diary");
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating diary");
            throw new CustomException(DIARY_UPDATE_EXCEPTION, e.getMessage());
        }
        log.info("operation = updateDiaryEntry ,  result = SUCCESS , caseDiaryEntryRequest : {} ", caseDiaryRequest);

        return caseDiaryRequest.getDiary();
    }

    public String generateDiary(CaseDiaryGenerateRequest generateRequest) {

        log.info("operation = generateDiary ,  result = IN_PROGRESS , CaseDiaryGenerateRequest : {} ", generateRequest);
        try {

            enrichment.enrichGenerateRequestForDiary(generateRequest);

            SimpleDateFormat dateFormat = new SimpleDateFormat(DOB_FORMAT_D_M_Y);

            //TODO: use strategy design pattern to get case diary entries based on diaryType

//            if (generateRequest.getDiary().getDiaryType().equalsIgnoreCase())

//            List<CourtCase> caseListResponse = caseUtil.getCaseDetails(generateRequest);

//            String cmpNumber = null;
//            String courtCaseNumber = null;
//            if (caseListResponse != null) {
//                cmpNumber = caseListResponse.get(0).getCmpNumber();
//                courtCaseNumber = caseListResponse.get(0).getCourtCaseNumber();
//            }

//            CaseDiarySearchRequest caseDiarySearchRequest = buildCaseDiarySearchRequest(generateRequest, cmpNumber);
//            List<CaseDiaryEntry> caseDiaryEntries = new ArrayList<>(diaryEntryService.searchDiaryEntries(caseDiarySearchRequest));
            CaseDiarySearchRequest caseDiarySearchRequest = buildCaseDiarySearchRequest(generateRequest, null);
            List<CaseDiaryEntry> caseDiaryEntries = diaryEntryService.searchDiaryEntries(caseDiarySearchRequest);

            caseDiaryEntries.forEach(entry -> {
                if (entry.getHearingDate() != null) {
                    Date date = new Date(entry.getHearingDate());
                    entry.setDate(dateFormat.format(date));
                }
            });

            CaseDiary caseDiary = generateRequest.getDiary();

            caseDiary.setCaseDiaryEntries(caseDiaryEntries);
            caseDiary.setDate(dateFormat.format(new Date(caseDiary.getDiaryDate())));
            generateRequest.setDiary(caseDiary);

            ByteArrayResource byteArrayResource = generateCaseDiary(caseDiary, generateRequest.getRequestInfo());
            Document document = fileStoreUtil.saveDocumentToFileStore(byteArrayResource, generateRequest.getDiary().getTenantId());
            CaseDiaryDocument caseDiaryDocument = getCaseDiaryDocument(generateRequest, document);
            generateRequest.getDiary().setDocuments(Collections.singletonList(caseDiaryDocument));

            Workflow workFlowForCreateAction = Workflow.builder()
                    .action(GENERATE_ACTION)
                    .build();
            generateRequest.getDiary().setWorkflow(workFlowForCreateAction);

            CaseDiaryRequest caseDiaryRequest = CaseDiaryRequest.builder().requestInfo(generateRequest.getRequestInfo()).diary(generateRequest.getDiary()).build();

            workflowService.updateWorkflowStatus(caseDiaryRequest);
            producer.push(configuration.getCaseDiaryTopic(), caseDiaryRequest);

            log.info("operation = generateDiary ,  result = SUCCESS , CaseDiaryGenerateRequest : {} ", generateRequest);
            return document.getFileStore();

        } catch (CustomException e) {
            log.error("Custom exception while generating diary");
            throw e;
        } catch (Exception e) {
            throw new CustomException(DIARY_GENERATE_EXCEPTION, "Error while generating diary");
        }

    }

    public CaseDiarySearchRequest buildCaseDiarySearchRequest(CaseDiaryGenerateRequest generateRequest, String caseId) {
        return CaseDiarySearchRequest.builder().requestInfo(generateRequest.getRequestInfo())
                .criteria(CaseDiarySearchCriteria.builder()
                        .caseId(caseId)
                        .tenantId(generateRequest.getDiary().getTenantId())
                        .diaryType(generateRequest.getDiary().getDiaryType())
                        .date(generateRequest.getDiary().getDiaryDate())
                        .judgeId(generateRequest.getDiary().getJudgeId())
                        .build())
                .build();
    }

    public CaseDiaryDocument getCaseDiaryDocument(CaseDiaryGenerateRequest generateRequest, Document document) {

        return CaseDiaryDocument.builder()
                .id(generateRequest.getDiary().getDocuments().get(0).getId())
                .tenantId(generateRequest.getDiary().getTenantId())
                .documentType(document.getDocumentType())
                .caseDiaryId(String.valueOf(generateRequest.getDiary().getId()))
                .isActive(true)
                .fileStoreId(document.getFileStore())
                .documentUid(document.getDocumentUid())
                .additionalDetails(document.getAdditionalDetails())
                .auditDetails(generateRequest.getDiary().getAuditDetails())
                .build();
    }

    public ByteArrayResource generateCaseDiary(CaseDiary caseDiary, RequestInfo requestInfo) {
        log.info("operation = generateCaseDiary, result = IN_PROGRESS");
        ByteArrayResource byteArrayResource = null;
        try {
            CaseDiaryRequest caseDiaryRequest = CaseDiaryRequest.builder()
                    .requestInfo(requestInfo)
                    .courtName(configuration.getCourtName())
                    .diary(caseDiary).build();

            String pdfTemplateKey = "";
            if (StringUtils.equals(caseDiary.getDiaryType().toLowerCase(), "adiary")) {
                pdfTemplateKey = configuration.getADiaryPdfTemplateKey();
            } else if (StringUtils.equals(caseDiary.getDiaryType().toLowerCase(), "bdiary")) {
                pdfTemplateKey = configuration.getBDiaryPdfTemplateKey();
            }

            byteArrayResource = pdfServiceUtil.generatePdfFromPdfService(caseDiaryRequest, caseDiary.getTenantId(), pdfTemplateKey);
            log.info("operation = generateCauseListPdf, result = SUCCESS");
        } catch (Exception e) {
            log.error("Error occurred while generating pdf: {}", e.getMessage());
        }
        return byteArrayResource;
    }
}
