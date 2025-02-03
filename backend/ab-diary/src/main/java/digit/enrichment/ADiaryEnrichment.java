package digit.enrichment;

import digit.repository.DiaryRepository;
import digit.util.ADiaryUtil;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import java.util.*;

import static digit.config.ServiceConstants.ENRICHMENT_EXCEPTION;

@Component
@Slf4j
public class ADiaryEnrichment {

    private final ADiaryUtil aDiaryUtil;

    private final DiaryRepository diaryRepository;

    public ADiaryEnrichment(ADiaryUtil aDiaryUtil, DiaryRepository diaryRepository) {
        this.aDiaryUtil = aDiaryUtil;
        this.diaryRepository = diaryRepository;
    }

    public void enrichUpdateCaseDiary(CaseDiaryRequest caseDiaryRequest) {
        log.info("operation = enrichUpdateCaseDiary ,  result = IN_PROGRESS , CaseDiaryRequest : {} ", caseDiaryRequest);

        try {
            CaseDiary diary = caseDiaryRequest.getDiary();
            RequestInfo requestInfo = caseDiaryRequest.getRequestInfo();
            User user = requestInfo.getUserInfo();

            AuditDetails auditDetails = AuditDetails.builder()
                    .lastModifiedBy(user.getUuid())
                    .lastModifiedTime(aDiaryUtil.getCurrentTimeInMilliSec())
                    .build();

            diary.setAuditDetails(auditDetails);

            if (caseDiaryRequest.getDiary().getDocuments() != null) {

                enrichDiaryDocument(caseDiaryRequest);
            }

        } catch (Exception e) {
            log.error("Error occurred during enriching diary");
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error during enriching diary");
        }

        log.info("operation = enrichUpdateCaseDiary ,  result = SUCCESS , CaseDiaryRequest : {} ", caseDiaryRequest);

    }

    public void enrichDiaryDocument(CaseDiaryRequest caseDiaryRequest) {


        CaseDiaryDocument caseDiaryDocument = caseDiaryRequest.getDiary().getDocuments().get(0);

        RequestInfo requestInfo = caseDiaryRequest.getRequestInfo();
        User user = requestInfo.getUserInfo();

        if (caseDiaryDocument != null) {
            caseDiaryDocument.setId(aDiaryUtil.generateUUID());

            AuditDetails auditDetails = AuditDetails.builder().createdBy(user.getUuid()).lastModifiedBy(user.getUuid())
                    .createdTime(aDiaryUtil.getCurrentTimeInMilliSec()).lastModifiedTime(aDiaryUtil.getCurrentTimeInMilliSec())
                    .build();

            caseDiaryDocument.setAuditDetails(auditDetails);
        }

        caseDiaryRequest.getDiary().setDocuments(Collections.singletonList(caseDiaryDocument));

    }

    public void enrichGenerateRequestForDiary(CaseDiaryGenerateRequest generateRequest) {

        try {
            CaseDiary caseDiary = generateRequest.getDiary();

            RequestInfo requestInfo = generateRequest.getRequestInfo();

            // TODO works for A-diary need to enrich for B-diary
            CaseDiarySearchRequest caseDiaryRequest = CaseDiarySearchRequest.builder()
                    .criteria(CaseDiarySearchCriteria.builder()
                            .judgeId(caseDiary.getJudgeId())
                            .date(caseDiary.getDiaryDate())
                            .diaryType(caseDiary.getDiaryType())
                            .tenantId(caseDiary.getTenantId())
                            .build())
                    .build();

            List<CaseDiary> caseDiaryList = diaryRepository.getCaseDiariesWithDocuments(caseDiaryRequest);

            AuditDetails auditDetails;

            if (!caseDiaryList.isEmpty()) {
                CaseDiary caseDiaryFromDb = caseDiaryList.get(0);
                caseDiary.setId(caseDiaryFromDb.getId());
                caseDiary.setAuditDetails(caseDiaryFromDb.getAuditDetails());
                CaseDiaryDocument caseDiaryDocument = CaseDiaryDocument.builder()
                        .id(caseDiaryFromDb.getDocuments().get(0).getId())
                        .auditDetails(caseDiaryFromDb.getDocuments().get(0).getAuditDetails())
                        .build();
                auditDetails = getAuditDetailsForUpdate(requestInfo, caseDiaryDocument);
                caseDiary.setDocuments(Collections.singletonList(caseDiaryDocument));
            } else {
                caseDiary.setId(aDiaryUtil.generateUUID());
                CaseDiaryDocument caseDiaryDocument = CaseDiaryDocument.builder()
                        .id(aDiaryUtil.generateUUID())
                        .build();
                auditDetails = getAuditDetailsForCreate(requestInfo);
                caseDiary.setDocuments(Collections.singletonList(caseDiaryDocument));
            }

            caseDiary.setAuditDetails(auditDetails);
            caseDiary.getDocuments().get(0).setAuditDetails(auditDetails);
            generateRequest.setDiary(caseDiary);
        }
        catch (Exception e) {
            log.error("Error occurred during enriching diary ");
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error during enriching diary");        }
    }

    private AuditDetails getAuditDetailsForCreate(RequestInfo requestinfo) {

        User user = requestinfo.getUserInfo();

        return AuditDetails.builder().createdBy(user.getUuid()).lastModifiedBy(user.getUuid())
                .createdTime(aDiaryUtil.getCurrentTimeInMilliSec()).lastModifiedTime(aDiaryUtil.getCurrentTimeInMilliSec())
                .build();

    }

    private AuditDetails getAuditDetailsForUpdate(RequestInfo requestInfo,CaseDiaryDocument caseDiaryDocument) {

        User user = requestInfo.getUserInfo();

        return AuditDetails.builder().lastModifiedBy(user.getUuid())
                .lastModifiedTime(aDiaryUtil.getCurrentTimeInMilliSec())
                .createdTime(caseDiaryDocument.getAuditDetails().getCreatedTime())
                .createdBy(caseDiaryDocument.getAuditDetails().getCreatedBy())
                .build();

    }


}
