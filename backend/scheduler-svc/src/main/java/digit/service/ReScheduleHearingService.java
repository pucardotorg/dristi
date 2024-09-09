package digit.service;


import com.fasterxml.jackson.databind.JsonNode;
import digit.config.Configuration;
import digit.config.ServiceConstants;
import digit.enrichment.ReScheduleRequestEnrichment;
import digit.kafka.producer.Producer;
import digit.repository.ReScheduleRequestRepository;
import digit.util.CaseUtil;
import digit.util.DateUtil;
import digit.util.HearingUtil;
import digit.util.MasterDataUtil;
import digit.validator.ReScheduleRequestValidator;
import digit.web.models.*;
import digit.web.models.cases.CaseCriteria;
import digit.web.models.cases.SearchCaseRequest;
import digit.web.models.hearing.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static digit.config.ServiceConstants.OPT_OUT_SELECTION_LIMIT;
import static digit.config.ServiceConstants.STATUS_RESCHEDULE;


/**
 * Contains methods related to raised reschedule request, update request,search , bulk rescheduling
 */
@Service
@Slf4j
public class ReScheduleHearingService {

    private final Configuration config;
    private final ReScheduleRequestRepository repository;
    private final ReScheduleRequestEnrichment enrichment;
    private final Producer producer;
    private final HearingService hearingService;
    private final CalendarService calendarService;
    private final ServiceConstants serviceConstants;
    private final MasterDataUtil helper;
    private final CaseUtil caseUtil;
    private final HearingUtil hearingUtil;
    private final ServiceConstants constants;
    private final DateUtil dateUtil;
    private final ReScheduleRequestValidator validator;


    @Autowired
    public ReScheduleHearingService(Configuration config, ReScheduleRequestRepository repository, ReScheduleRequestValidator validator, ReScheduleRequestEnrichment enrichment, Producer producer, HearingService hearingService, CalendarService calendarService, ServiceConstants serviceConstants, MasterDataUtil helper, CaseUtil caseUtil, HearingUtil hearingUtil, ServiceConstants constants, DateUtil dateUtil) {


        this.config = config;
        this.repository = repository;
        this.enrichment = enrichment;
        this.producer = producer;
        this.hearingService = hearingService;
        this.calendarService = calendarService;
        this.serviceConstants = serviceConstants;
        this.helper = helper;
        this.caseUtil = caseUtil;
        this.constants = constants;
        this.hearingUtil = hearingUtil;
        this.dateUtil = dateUtil;
        this.validator = validator;
    }

    /**
     * @param reScheduleHearingsRequest
     * @return
     */

    public List<ReScheduleHearing> create(ReScheduleHearingRequest reScheduleHearingsRequest) {
        log.info("operation = create, result = IN_PROGRESS,  RescheduledRequest = {}", reScheduleHearingsRequest.getReScheduleHearing());

        List<ReScheduleHearing> reScheduleHearing = reScheduleHearingsRequest.getReScheduleHearing();
        RequestInfo requestInfo = reScheduleHearingsRequest.getRequestInfo();

        enrichment.enrichRescheduleRequest(reScheduleHearingsRequest);

        try {

            String tenantId = reScheduleHearing.get(0).getTenantId();

            for (ReScheduleHearing hearingDetail : reScheduleHearing) {
                CaseCriteria criteria = CaseCriteria.builder().tenantId(tenantId).filingNumber(hearingDetail.getCaseId()).build();
                SearchCaseRequest searchCaseRequest = SearchCaseRequest.builder().RequestInfo(requestInfo).criteria(Collections.singletonList(criteria)).build();
                JsonNode cases = caseUtil.getCases(searchCaseRequest);
                JsonNode litigants = caseUtil.getLitigants(cases);
                Set<String> litigantIds = caseUtil.getIndividualIds(litigants);
                JsonNode representatives = caseUtil.getRepresentatives(cases);
                Set<String> representativeIds = caseUtil.getIdsFromJsonNodeArray(representatives);
                int noOfAttendees = representativeIds.size();


                List<SchedulerConfig> dataFromMDMS = helper.getDataFromMDMS(SchedulerConfig.class, constants.SCHEDULER_CONFIG_MASTER_NAME, constants.SCHEDULER_CONFIG_MODULE_NAME);

                List<SchedulerConfig> filteredApplications = dataFromMDMS.stream()
                        .filter(application -> application.getIdentifier().equals(OPT_OUT_SELECTION_LIMIT))
                        .toList();

                int unit = filteredApplications.get(0).getUnit();
                Integer numberOfSuggestedDays = Math.toIntExact((long) unit * noOfAttendees + 1);

                litigantIds = caseUtil.getLitigantsFromRepresentatives(litigantIds, representatives);

                hearingDetail.setRepresentatives(representativeIds);
                hearingDetail.setLitigants(litigantIds);

                LocalDate availableAfter = dateUtil.getLocalDateFromEpoch(hearingDetail.getAvailableAfter()).plusDays(1);

                Long optOutAfterDate = dateUtil.getEPochFromLocalDate(availableAfter);

                List<AvailabilityDTO> availability = calendarService.getJudgeAvailability(JudgeAvailabilitySearchRequest.builder().requestInfo(requestInfo).criteria(JudgeAvailabilitySearchCriteria.builder().judgeId(hearingDetail.getJudgeId()).fromDate(optOutAfterDate).courtId("0001")  //TODO: need to configure somewhere
                        .numberOfSuggestedDays(numberOfSuggestedDays).tenantId(tenantId).build()).build());

                // update here all the suggestedDay in reschedule hearing day
                List<Long> suggestedDays = availability.stream().map((suggestedDate) -> Long.valueOf(suggestedDate.getDate())).toList();
                hearingDetail.setSuggestedDates(suggestedDays);


                List<ScheduleHearing> hearings = hearingService.search(HearingSearchRequest.builder().requestInfo(requestInfo).criteria(ScheduleHearingSearchCriteria.builder().hearingIds(Collections.singletonList(hearingDetail.getHearingBookingId())).build()).build(), null, null);
                ScheduleHearing hearing = hearings.get(0);
                hearing.setStatus(STATUS_RESCHEDULE);
                hearing.setRescheduleRequestId(hearingDetail.getRescheduledRequestId());

                //reschedule hearing to unblock the calendar
                hearingService.update(ScheduleHearingRequest.builder().requestInfo(requestInfo).hearing(hearings).build());

                List<ScheduleHearing> udpateHearingList = new ArrayList<>();

                for (AvailabilityDTO availabilityDTO : availability) {
                    //TODO: update logic to assign start time and end time

                    ScheduleHearing scheduleHearing = new ScheduleHearing(hearing);


                    long blockedDate = Long.parseLong(availabilityDTO.getDate());
                    scheduleHearing.setHearingDate(blockedDate);
                    scheduleHearing.setStartTime(blockedDate);
                    scheduleHearing.setEndTime(blockedDate);
                    scheduleHearing.setRescheduleRequestId(hearingDetail.getRescheduledRequestId());
                    scheduleHearing.setStatus("BLOCKED");

                    udpateHearingList.add(scheduleHearing);

                }
                List<ScheduleHearing> schedule = hearingService.schedule(ScheduleHearingRequest.builder().requestInfo(requestInfo).hearing(udpateHearingList).build());
                producer.push(config.getScheduleHearingTopic(), schedule);
            }

        } catch (Exception e) {

            log.error("DK_SH_APP_ERR: error while blocking the calendar", e);
        }


        producer.push(config.getRescheduleRequestCreateTopic(), reScheduleHearing);

        log.info("operation = create, result=SUCCESS, ReScheduleHearing={}", reScheduleHearing);

        return reScheduleHearing;

    }


    /**
     * @param request
     * @return
     */
    public List<ReScheduleHearing> search(ReScheduleHearingReqSearchRequest request, Integer limit, Integer offset) {
        return repository.getReScheduleRequest(request.getCriteria(), limit, offset);
    }

    /**
     * @param request
     * @return
     */
    public List<ReScheduleHearing> bulkReschedule(BulkReScheduleHearingRequest request) {
        try {
            log.info("operation = bulkReschedule, result = IN_PROGRESS,  BulkRescheduling = {}", request.getBulkRescheduling());

            validator.validateBulkRescheduleRequest(request);

            List<MdmsSlot> defaultSlots = helper.getDataFromMDMS(MdmsSlot.class, serviceConstants.DEFAULT_SLOTTING_MASTER_NAME, serviceConstants.DEFAULT_COURT_MODULE_NAME);
            double totalHrs = defaultSlots.stream().reduce(0.0, (total, slot) -> total + slot.getSlotDuration() / 60.0, Double::sum);
            List<MdmsHearing> defaultHearings = helper.getDataFromMDMS(MdmsHearing.class, serviceConstants.DEFAULT_HEARING_MASTER_NAME, serviceConstants.DEFAULT_COURT_MODULE_NAME);
            Map<String, MdmsHearing> hearingTypeMap = defaultHearings.stream().collect(Collectors.toMap(MdmsHearing::getHearingType, obj -> obj));
            BulkReschedulingOfHearings bulkRescheduling = request.getBulkRescheduling();

            String tenantId = bulkRescheduling.getTenantId();
            String judgeId = bulkRescheduling.getJudgeId();
            Long endTime = bulkRescheduling.getEndTime();
            Long startTime = bulkRescheduling.getStartTime();
            Long fromDate = bulkRescheduling.getScheduleAfter();

            List<String> hearingIds = bulkRescheduling.getHearingIds();

            ScheduleHearingSearchCriteria criteria;

            if (startTime != null && endTime != null) {

                criteria = ScheduleHearingSearchCriteria.builder().judgeId(judgeId).startDateTime(startTime).endDateTime(endTime).tenantId(tenantId).status(Arrays.asList("SCHEDULE", "BLOCKED")).build();

            } else if (!hearingIds.isEmpty()) {

                criteria = ScheduleHearingSearchCriteria.builder().judgeId(judgeId).hearingIds(hearingIds).tenantId(tenantId).build();

            } else {
                throw new CustomException("DK_BULK_INVALID_REQUEST", "Request is not valid");
            }


            List<ScheduleHearing> hearings = hearingService.search(HearingSearchRequest.builder().requestInfo(request.getRequestInfo()).criteria(criteria).build(), null, null);

            if (CollectionUtils.isEmpty(hearings)) {
                return new ArrayList<>();
            }
            //get available date
            List<AvailabilityDTO> availability = calendarService
                    .getJudgeAvailability(JudgeAvailabilitySearchRequest
                            .builder().requestInfo(request.getRequestInfo())
                            .criteria(JudgeAvailabilitySearchCriteria
                                    .builder()
                                    .judgeId(judgeId).fromDate(fromDate).courtId("0001")   // need to configure some where
                                    .numberOfSuggestedDays(hearings.size() + 10).tenantId(tenantId)
                                    .build()).build());

            // assign slots and push for schedule hearing
            int index = 0;
            Double requiredSlot = null;
            for (ScheduleHearing hearing : hearings) {
                String eventType = hearing.getHearingType();

                MdmsHearing hearingType = hearingTypeMap.get(eventType);
                requiredSlot = hearingType.getHearingTime() / 60.00;

                Double occupiedBandwidth = availability.get(index).getOccupiedBandwidth();
                if (totalHrs - occupiedBandwidth > requiredSlot) {  // need to configure
                    hearing.setHearingDate(Long.parseLong(availability.get(index).getDate()));
                    availability.get(index).setOccupiedBandwidth(occupiedBandwidth + requiredSlot);  // need to configure
                } else {
                    hearing.setHearingDate(Long.parseLong(availability.get(++index).getDate()));
                    availability.get(index).setOccupiedBandwidth(availability.get(index).getOccupiedBandwidth() + requiredSlot);  // need to configure
                }
            }
            // try to make it async
            // updated hearing in hearing table
            List<ScheduleHearing> rescheduleHearings = hearingService.updateBulk(ScheduleHearingRequest.builder().hearing(hearings).requestInfo(request.getRequestInfo()).build(), defaultSlots, hearingTypeMap);


            // update hearing of dpg as well
            // fetch dpg hearing with hearingids assign startTime and end time then hit their update api
            LocalDate startDate = dateUtil.getLocalDateFromEpoch(bulkRescheduling.getStartTime());
            LocalDate endDate = dateUtil.getLocalDateFromEpoch(bulkRescheduling.getEndTime());
            HearingSearchCriteria searchCriteria = HearingSearchCriteria.builder().tenantId(bulkRescheduling.getTenantId()).fromDate(startDate).toDate(endDate).build();
            Pagination pagination = Pagination.builder().limit(100.0).build();
            HearingListSearchRequest searchRequest = HearingListSearchRequest.builder().requestInfo(request.getRequestInfo()).criteria(searchCriteria).pagination(pagination).build();

            List<Hearing> hearingList = hearingUtil.fetchHearing(searchRequest);
            Map<String, ScheduleHearing> scheduleHearingMap = rescheduleHearings.stream().collect(Collectors.toMap(ScheduleHearing::getHearingBookingId, obj -> obj));
            for (Hearing hearing : hearingList) {
                if (scheduleHearingMap.containsKey(hearing.getHearingId())) {
                    ScheduleHearing scheduleHearing = scheduleHearingMap.get(hearing.getHearingId());
                    hearing.setStartTime(scheduleHearing.getStartTime());
                    hearing.setEndTime(scheduleHearing.getEndTime());
                }
            }
            HearingUpdateBulkRequest updateBulkRequest = HearingUpdateBulkRequest.builder().requestInfo(request.getRequestInfo()).hearings(hearingList).build();
            hearingUtil.callHearing(updateBulkRequest);
            // one edge case is here , for opt out if that date or in that duration suggested days are there then what need to done
            log.info("operation = bulkReschedule, result = SUCCESS");
            return null;
        } catch (Exception e) {
            log.error("Error updating bulk reschdule request: {}", e.getMessage());
            return null;
        }

    }


}