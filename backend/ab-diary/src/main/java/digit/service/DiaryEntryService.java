package digit.service;

import digit.config.Configuration;
import digit.enrichment.ADiaryEntryEnrichment;
import digit.kafka.Producer;
import digit.repository.DiaryEntryRepository;
import digit.validators.ADiaryValidator;
import digit.web.models.CaseDiaryEntry;
import digit.web.models.CaseDiaryEntryRequest;
import digit.web.models.CaseDiarySearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Service;

import java.util.List;

import static digit.config.ServiceConstants.*;

@Service
@Slf4j
public class DiaryEntryService {

    private final ADiaryValidator validator;

    private final ADiaryEntryEnrichment enrichment;

    private final Producer producer;

    private final Configuration configuration;

    private final DiaryEntryRepository diaryEntryRepository;

    public DiaryEntryService(ADiaryValidator validator, ADiaryEntryEnrichment enrichment, Producer producer, Configuration configuration, DiaryEntryRepository diaryEntryRepository) {
        this.validator = validator;
        this.enrichment = enrichment;
        this.producer = producer;
        this.configuration = configuration;
        this.diaryEntryRepository = diaryEntryRepository;
    }

    public CaseDiaryEntry addDiaryEntry(CaseDiaryEntryRequest caseDiaryEntryRequest) {

        log.info("operation = addDiaryEntry ,  result = IN_PROGRESS , caseDiaryEntryRequest : {} ", caseDiaryEntryRequest);

        try {

            validator.validateSaveDiaryEntry(caseDiaryEntryRequest);

            enrichment.enrichCreateDiaryEntry(caseDiaryEntryRequest);

            producer.push(configuration.getDiaryEntryCreateTopic(), caseDiaryEntryRequest);

        } catch (CustomException e) {
            log.error("Custom exception occurred while creating diary entry");
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while creating diary entry");
            throw new CustomException(DIARY_ENTRY_CREATE_EXCEPTION, e.getMessage());
        }
        log.info("operation = addDiaryEntry ,  result = SUCCESS , caseDiaryEntryRequest : {} ", caseDiaryEntryRequest);
        return caseDiaryEntryRequest.getDiaryEntry();
    }

    public CaseDiaryEntry updateDiaryEntry(CaseDiaryEntryRequest caseDiaryEntryRequest) {

        log.info("operation = updateDiaryEntry ,  result = IN_PROGRESS , caseDiaryEntryRequest : {} ", caseDiaryEntryRequest);

        try {

            validator.validateUpdateDiaryEntry(caseDiaryEntryRequest);

            enrichment.enrichUpdateEntry(caseDiaryEntryRequest);

            producer.push(configuration.getDiaryEntryUpdateTopic(), caseDiaryEntryRequest);

        } catch (CustomException e) {
            log.error("Custom exception occurred while updating diary entry");
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating diary entry");
            throw new CustomException(DIARY_ENTRY_UPDATE_EXCEPTION, e.getMessage());
        }

        log.info("operation = updateDiaryEntry ,  result = SUCCESS , caseDiaryEntryRequest : {} ", caseDiaryEntryRequest);

        return caseDiaryEntryRequest.getDiaryEntry();
    }

    public List<CaseDiaryEntry> searchDiaryEntries(CaseDiarySearchRequest searchRequest) {

        log.info("operation = searchDiaryEntries ,  result = IN_PROGRESS , CaseDiarySearchRequest : {} ", searchRequest);

        try {

            if (searchRequest == null || searchRequest.getCriteria() == null) {
                return null;
            }

            log.info("operation = searchDiaryEntries ,  result = SUCCESS , caseDiaryEntryRequest : {} ", searchRequest);

            return diaryEntryRepository.getCaseDiaryEntries(searchRequest);
        } catch (CustomException e) {
            log.error("Custom exception while searching");
            throw e;
        } catch (Exception e) {
            throw new CustomException(DIARY_ENTRY_SEARCH_EXCEPTION, "Error while searching");
        }

    }

}
