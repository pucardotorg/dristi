package digit.service;


import digit.config.Configuration;
import digit.enrichment.RescheduleRequestOptOutEnrichment;
import digit.kafka.producer.Producer;
import digit.repository.RescheduleRequestOptOutRepository;
import digit.validator.RescheduleRequestOptOutValidator;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RescheduleRequestOptOutService {

    private final RescheduleRequestOptOutRepository optOutRepository;

    private final RescheduleRequestOptOutValidator optOutValidator;

    private final RescheduleRequestOptOutEnrichment optOutEnrichment;

    private final Producer producer;

    private final Configuration config;

    private final ReScheduleHearingService reScheduleHearingService;

    @Autowired
    public RescheduleRequestOptOutService(RescheduleRequestOptOutRepository optOutRepository, RescheduleRequestOptOutValidator optOutValidator, RescheduleRequestOptOutEnrichment optOutEnrichment, Producer producer, Configuration config, ReScheduleHearingService reScheduleHearingService) {
        this.optOutRepository = optOutRepository;
        this.optOutValidator = optOutValidator;
        this.optOutEnrichment = optOutEnrichment;
        this.producer = producer;
        this.config = config;
        this.reScheduleHearingService = reScheduleHearingService;
    }

    /**
     * @param request
     * @return
     */
    public OptOut create(OptOutRequest request) {
        log.info("operation = create, result = IN_PROGRESS, OptOut = {}", request.getOptOut());

        optOutValidator.validateRequest(request);

        optOutEnrichment.enrichCreateRequest(request);

        producer.push(config.getOptOutTopic(), request.getOptOut());

        log.info("operation = create, result = SUCCESS, OptOut = {}", request.getOptOut());

        return request.getOptOut();
    }

    /**
     * @param request
     * @return
     */
//    public List<OptOut> update(OptOutRequest request) {
//        log.info("operation = update, result = IN_PROGRESS, OptOut = {}", request.getOptOuts());
//
//        optOutValidator.validateUpdateRequest(request);
//
//        optOutEnrichment.enrichUpdateRequest(request);
//
//        producer.push(config.getOptOutUpdateTopic(), request.getOptOuts());
//
//        log.info("operation = update, result = SUCCESS, OptOut = {}", request.getOptOuts());
//
//        return request.getOptOuts();
//    }

    /**
     * @param request
     * @return
     */

    public List<OptOut> search(OptOutSearchRequest request, Integer limit, Integer offset) {
        return optOutRepository.getOptOut(request.getCriteria(), limit, offset);
    }
}
