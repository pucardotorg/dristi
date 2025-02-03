package digit.validators;

import digit.repository.DiaryEntryRepository;
import digit.repository.DiaryRepository;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static digit.config.ServiceConstants.VALIDATION_EXCEPTION;

@Component
@Slf4j
public class ADiaryValidator {

    private final DiaryEntryRepository diaryEntryRepository;
    private final DiaryRepository diaryRepository;

    public ADiaryValidator(DiaryEntryRepository diaryEntryRepository, DiaryRepository diaryRepository) {
        this.diaryEntryRepository = diaryEntryRepository;
        this.diaryRepository = diaryRepository;
    }

    public void validateSaveDiaryEntry(CaseDiaryEntryRequest caseDiaryEntryRequest) {

        CaseDiaryEntry caseDiaryEntry = caseDiaryEntryRequest.getDiaryEntry();

        RequestInfo requestInfo = caseDiaryEntryRequest.getRequestInfo();

        if (ObjectUtils.isEmpty(caseDiaryEntry)) {
            throw new CustomException(VALIDATION_EXCEPTION, "case diary entry is mandatory to create an entry");
        }
        if (requestInfo == null || requestInfo.getUserInfo() == null) {
            throw new CustomException(VALIDATION_EXCEPTION, "request Info or user info can not be null");
        }
    }

    public void validateUpdateDiaryEntry(CaseDiaryEntryRequest caseDiaryEntryRequest) {

        CaseDiaryEntry caseDiaryEntry = caseDiaryEntryRequest.getDiaryEntry();

        RequestInfo requestInfo = caseDiaryEntryRequest.getRequestInfo();

        if (ObjectUtils.isEmpty(caseDiaryEntry)) {
            throw new CustomException(VALIDATION_EXCEPTION, "case diary entry is mandatory to update");
        }
        if (caseDiaryEntry.getId() == null) {
            throw new CustomException(VALIDATION_EXCEPTION, "Id is mandatory to update entry");
        }
        if (requestInfo == null || requestInfo.getUserInfo() == null) {
            throw new CustomException(VALIDATION_EXCEPTION, "request info or user info is mandatory");
        }

        validateExistingDiaryEntry(caseDiaryEntry);

    }

    private void validateExistingDiaryEntry(CaseDiaryEntry caseDiaryEntry) {

        CaseDiaryExistCriteria caseDiaryExistCriteria = CaseDiaryExistCriteria.builder()
                .tenantId(caseDiaryEntry.getTenantId())
                .id(caseDiaryEntry.getId())
                .build();

        List<CaseDiaryEntry> caseDiaryEntryResponse = diaryEntryRepository.getExistingDiaryEntry(caseDiaryExistCriteria);

        if (caseDiaryEntryResponse == null || caseDiaryEntryResponse.isEmpty()) {
            throw new CustomException(VALIDATION_EXCEPTION, "diary entry does not exists");
        }
        if (caseDiaryEntryResponse.size() > 1) {
            throw new CustomException(VALIDATION_EXCEPTION, "multiple diary entries found with id");
        }

    }

    public void validateUpdateDiary(CaseDiaryRequest caseDiaryRequest) {

        CaseDiary diary = caseDiaryRequest.getDiary();

        RequestInfo requestInfo = caseDiaryRequest.getRequestInfo();

        if (ObjectUtils.isEmpty(diary)) {
            throw new CustomException(VALIDATION_EXCEPTION, "case diary is mandatory to create/update an entry");
        }
        if (diary.getId() == null) {
            throw new CustomException(VALIDATION_EXCEPTION, "Id is mandatory to update entry");
        }
        if (requestInfo == null || requestInfo.getUserInfo() == null) {
            throw new CustomException(VALIDATION_EXCEPTION, "request Info or user info can not be null");
        }

        validateExistingDiary(diary);
    }

    private void validateExistingDiary(CaseDiary diary) {

        CaseDiarySearchCriteria searchCriteria = CaseDiarySearchCriteria.builder().tenantId(diary.getTenantId())
                .date(diary.getDiaryDate()).judgeId(diary.getJudgeId())
                .build();

        CaseDiarySearchRequest caseDiarySearchRequest = CaseDiarySearchRequest.builder().criteria(searchCriteria).build();

        List<CaseDiaryListItem> diaryResponse = diaryRepository.getCaseDiaries(caseDiarySearchRequest);

        if (diaryResponse == null) {
            throw new CustomException(VALIDATION_EXCEPTION, "diary does not exist");
        }

        List<CaseDiaryListItem> diaries = diaryResponse.stream()
                .filter(diary1 -> diary1.getDiaryId().equals(diary.getId())).toList();

        if (diaries.size() > 1) {
            throw new CustomException(VALIDATION_EXCEPTION, "multiple entries found with same id");
        } else if (diaries.isEmpty()) {
            throw new CustomException(VALIDATION_EXCEPTION, "diary does not exist");
        }
    }

}
