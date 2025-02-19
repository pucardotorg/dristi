package digit.service;


import digit.config.Configuration;
import digit.config.ServiceConstants;
import digit.enrichment.HearingEnrichment;
import digit.kafka.producer.Producer;
import digit.repository.HearingRepository;
import digit.util.HearingUtil;
import digit.util.MasterDataUtil;
import digit.web.models.*;
import digit.web.models.hearing.Hearing;
import digit.web.models.hearing.HearingListSearchRequest;
import digit.web.models.hearing.HearingSearchCriteria;
import digit.web.models.hearing.HearingUpdateBulkRequest;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static digit.config.ServiceConstants.SCHEDULE;


@Service
@Slf4j
public class HearingService {


    private final HearingEnrichment hearingEnrichment;
    private final Producer producer;
    private final Configuration config;
    private final HearingRepository hearingRepository;
    private final ServiceConstants serviceConstants;
    private final MasterDataUtil helper;
    private final HearingUtil hearingUtil;

    @Autowired
    public HearingService(HearingEnrichment hearingEnrichment, Producer producer, Configuration config, HearingRepository hearingRepository, ServiceConstants serviceConstants, MasterDataUtil helper, HearingUtil hearingUtil) {
        this.hearingEnrichment = hearingEnrichment;
        this.producer = producer;
        this.config = config;
        this.hearingRepository = hearingRepository;
        this.serviceConstants = serviceConstants;
        this.helper = helper;
        this.hearingUtil = hearingUtil;
    }


    /**
     * Schedules hearings based on the provided request.
     * <p>
     * The function validates the request, retrieves necessary data from MDMS,
     * and calculates the judge's availability. It schedules the hearings by
     * assigning available slots and updates both the hearing table and
     * external systems using the hearing service and utility.
     *
     * @param schedulingRequests contains the scheduling request and request info
     * @return a list of scheduled hearings, or null if an error occurs
     * @throws CustomException if the request is invalid
     */
    public List<ScheduleHearing> schedule(ScheduleHearingRequest schedulingRequests) {
        log.info("operation = schedule, result = IN_PROGRESS, ScheduleHearingRequest={}, Hearing={}", schedulingRequests, schedulingRequests.getHearing());

        List<MdmsSlot> defaultSlots = helper.getDataFromMDMS(MdmsSlot.class, serviceConstants.DEFAULT_SLOTTING_MASTER_NAME, serviceConstants.DEFAULT_COURT_MODULE_NAME);

        List<MdmsHearing> defaultHearings = helper.getDataFromMDMS(MdmsHearing.class, serviceConstants.DEFAULT_HEARING_MASTER_NAME, serviceConstants.DEFAULT_COURT_MODULE_NAME);

        Map<String, MdmsHearing> hearingTypeMap = defaultHearings.stream().collect(Collectors.toMap(
                MdmsHearing::getHearingType,
                obj -> obj
        ));

        hearingEnrichment.enrichScheduleHearing(schedulingRequests, defaultSlots, hearingTypeMap);
        log.info("operation = schedule, result = SUCCESS, ScheduleHearingRequest={}, Hearing={}", schedulingRequests, schedulingRequests.getHearing());
        return schedulingRequests.getHearing();
    }


    /**
     * Updates the hearing based on the ScheduleHearingRequest object.
     * This function first enriches the hearing object and then sends the updated hearing to kafka topic.
     *
     * @param scheduleHearingRequest object containing the request info and hearings
     * @return list of updated schedule hearing object
     */
    public List<ScheduleHearing> update(ScheduleHearingRequest scheduleHearingRequest) {
        log.info("operation = update, result = IN_PROGRESS, ScheduleHearingRequest={}, Hearing={}", scheduleHearingRequest, scheduleHearingRequest.getHearing());


        hearingEnrichment.enrichUpdateScheduleHearing(scheduleHearingRequest.getRequestInfo(), scheduleHearingRequest.getHearing());

        producer.push(config.getScheduleHearingUpdateTopic(), scheduleHearingRequest);

        log.info("operation = update, result = SUCCESS, ScheduleHearing={}", scheduleHearingRequest.getHearing());

        return scheduleHearingRequest.getHearing();

    }


    /**
     * Searches for scheduled hearings based on the given criteria.
     *
     * @param request the search request containing the criteria for hearing search
     * @param limit   the maximum number of records to return
     * @param offset  the starting point for the records to return
     * @return a list of scheduled hearings that match the search criteria
     */
    public List<ScheduleHearing> search(HearingSearchRequest request, Integer limit, Integer offset) {

        return hearingRepository.getHearings(request.getCriteria(), limit, offset);

    }


    /**
     * Returns a list of AvailabilityDTO objects representing the available dates
     * for judges based on the given search criteria.
     *
     * @param scheduleHearingSearchCriteria the search criteria to filter the results
     * @return a list of AvailabilityDTO objects
     */
    public List<AvailabilityDTO> getAvailableDateForHearing(ScheduleHearingSearchCriteria scheduleHearingSearchCriteria) {

        return hearingRepository.getAvailableDatesOfJudges(scheduleHearingSearchCriteria);
    }


    /**
     * Updates multiple hearings in bulk based on the provided request.
     * <p>
     * The function validates the request, retrieves necessary data from MDMS,
     * and calculates the judge's availability. It updates the hearing schedules
     * by assigning available slots and updates both the hearing table and
     * external systems using the hearing service and utility.
     *
     * @param request        contains the bulk rescheduling details and request info
     * @param defaultSlot    list of default slots for each judge
     * @param hearingTypeMap map of hearing type to MdmsHearing
     * @return a list of rescheduled hearings, or null if an error occurs
     * @throws CustomException if the request is invalid
     */
    public List<ScheduleHearing> updateBulk(ScheduleHearingRequest request, List<MdmsSlot> defaultSlot, Map<String, MdmsHearing> hearingTypeMap) {

        log.info("operation = updateBulk, result = IN_PROGRESS");
        hearingEnrichment.enrichBulkReschedule(request, defaultSlot, hearingTypeMap);
        producer.push(config.getScheduleHearingUpdateTopic(), request);
        log.info("operation = updateBulk, result = SUCCESS");

        return request.getHearing();
    }


    /**
     * Updates a hearing by rescheduling it to a new date and time.
     * <p>
     * The function first retrieves the hearing to be updated from the database,
     * then reschedules it to the new date and time provided in the request, and
     * finally updates the hearing in the database.
     * <p>
     * The function also updates the associated hearings in the database that
     * were previously blocked due to the rescheduling of the hearing.
     *
     * @param request contains the hearing to be updated and the request info
     * @return the updated hearing
     */
    public ScheduleHearing updateHearing(UpdateHearingRequest request) {

        ScheduleHearing hearing = request.getHearing();
        assert (hearing != null);
        String rescheduleRequestId = hearing.getRescheduleRequestId();
        assert rescheduleRequestId != null;

        List<ScheduleHearing> hearings = search(HearingSearchRequest.builder().requestInfo(new RequestInfo())
                .criteria(ScheduleHearingSearchCriteria.builder()
                        .hearingIds(Collections.singletonList(hearing.getHearingBookingId())).build()).build(), null, null);

        assert !hearings.isEmpty();
        ScheduleHearing scheduleHearing = hearings.get(0);
        scheduleHearing.setHearingDate(hearing.getHearingDate());
        scheduleHearing.setStartTime(hearing.getStartTime());
        scheduleHearing.setEndTime(hearing.getEndTime());
        scheduleHearing.setStatus(SCHEDULE);
        List<ScheduleHearing> schedule = schedule(ScheduleHearingRequest.builder().requestInfo(request.getRequestInfo())
                .hearing(Collections.singletonList(scheduleHearing)).build());
        producer.push(config.getScheduleHearingUpdateTopic(), ScheduleHearingRequest.builder().requestInfo(request.getRequestInfo()).hearing(schedule).build());

        List<ScheduleHearing> blockedHearings = search(HearingSearchRequest.builder().requestInfo(new RequestInfo())
                .criteria(ScheduleHearingSearchCriteria.builder()
                        .rescheduleId(rescheduleRequestId).build()).build(), null, null);


        blockedHearings.forEach((element) -> element.setStatus("INACTIVE"));
        producer.push(config.getScheduleHearingUpdateTopic(), ScheduleHearingRequest.builder().requestInfo(request.getRequestInfo()).hearing(blockedHearings).build());


        HearingListSearchRequest searchRequest = HearingListSearchRequest.builder()
                .requestInfo(request.getRequestInfo())
                .criteria(HearingSearchCriteria.builder()
                        .hearingId(hearing.getHearingBookingId()).build())
                .build();

        List<Hearing> moduleHearing = hearingUtil.fetchHearing(searchRequest);

        moduleHearing.get(0).setStartTime(schedule.get(0).getStartTime());
        moduleHearing.get(0).setEndTime(schedule.get(0).getEndTime());

        hearingUtil.callHearing(HearingUpdateBulkRequest.builder().requestInfo(request.getRequestInfo())
                .hearings(moduleHearing).build());


        return scheduleHearing;
    }
}
