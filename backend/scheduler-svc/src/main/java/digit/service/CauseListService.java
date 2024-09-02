package digit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.config.ServiceConstants;
import digit.kafka.producer.Producer;
import digit.repository.CauseListRepository;
import digit.repository.HearingRepository;
import digit.util.MdmsUtil;
import digit.util.PdfServiceUtil;
import digit.web.models.*;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CauseListService {

    private HearingRepository hearingRepository;

    private CauseListRepository causeListRepository;

    private Producer producer;

    private Configuration config;

    private PdfServiceUtil pdfServiceUtil;

    private MdmsUtil mdmsUtil;

    private ServiceConstants serviceConstants;

    @Autowired
    public CauseListService(HearingRepository hearingRepository, CauseListRepository causeListRepository,
                            Producer producer, Configuration config, PdfServiceUtil pdfServiceUtil,
                            MdmsUtil mdmsUtil, ServiceConstants serviceConstants) {
        this.hearingRepository = hearingRepository;
        this.causeListRepository = causeListRepository;
        this.producer = producer;
        this.config =  config;
        this.pdfServiceUtil = pdfServiceUtil;
        this.mdmsUtil = mdmsUtil;
        this.serviceConstants = serviceConstants;
    }

    public void updateCauseListForTomorrow() {
        log.info("operation = updateCauseListForTomorrow, result = IN_PROGRESS");
        List<CauseList> causeLists = new ArrayList<>();
        //TODO get judges from db once tables are ready
        List<String> judgeIds = new ArrayList<>();
        judgeIds.add("judge001"); judgeIds.add("judge002"); judgeIds.add("judge003");

        // Multi Thread processing: process 10 judges at a time
        ExecutorService executorService = Executors.newCachedThreadPool();

        // Submit tasks for each judge
        submitTasks(executorService, judgeIds, causeLists);

        // Wait for all tasks to complete
        waitForTasksCompletion(executorService);

        if (!CollectionUtils.isEmpty(causeLists)) {
            CauseListResponse causeListResponse = CauseListResponse.builder()
                    .responseInfo(ResponseInfo.builder().build()).causeList(causeLists).build();
            producer.push(config.getCauseListInsertTopic(), causeListResponse);
        } else {
            log.info("No cause lists to be created");
        }
        log.info("operation = updateCauseListForTomorrow, result = SUCCESS");
    }

    private void submitTasks(ExecutorService executorService, List<String> judgeIds, List<CauseList> causeLists) {
        for (String judgeId : judgeIds) {
            // Submit a task to the executor service for each judge
            executorService.submit(() -> generateCauseListForJudge(judgeId, causeLists));
        }
    }

    private void waitForTasksCompletion(ExecutorService executorService) {
        executorService.shutdown();
        try {
            // Wait until all tasks are completed or timeout occurs
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            log.error("Error occurred while waiting for task completion: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void generateCauseListForJudge(String judgeId, List<CauseList> causeLists) {
        log.info("operation = generateCauseListForJudge, result = IN_PROGRESS, judgeId = {}", judgeId);
        ScheduleHearingSearchCriteria searchCriteria =  ScheduleHearingSearchCriteria.builder()
                .judgeId(judgeId)
                .build();
        List<ScheduleHearing> scheduleHearings = hearingRepository.getHearings(searchCriteria, null, null);
        if (CollectionUtils.isEmpty(scheduleHearings)) {
            log.info("No hearings scheduled tomorrow for judgeId = {}", judgeId);
        } else {
            log.info("No. of hearings scheduled tomorrow for judgeId = {} is {}", judgeId, scheduleHearings.size());
            fillHearingTimesWithDataFromMdms(scheduleHearings);
            generateCauseListFromHearings(scheduleHearings, causeLists);
            if (!CollectionUtils.isEmpty(causeLists)) {
                log.info("Generated {} CauseLists for judgeId {}", causeLists.size(), judgeId);
            }
            log.info("operation = generateCauseListForJudge, result = SUCCESS, judgeId = {}", judgeId);
        }
    }

    private void fillHearingTimesWithDataFromMdms(List<ScheduleHearing> scheduleHearings) {
        log.info("operation = fillHearingTimesWithDataFromMdms, result = IN_PROGRESS, judgeId = {}", scheduleHearings.get(0).getJudgeId());
        List<MdmsHearing> mdmsHearings = getHearingDataFromMdms();
//        for (ScheduleHearing scheduleHearing : scheduleHearings) {
//            Optional<MdmsHearing> optionalHearing = mdmsHearings.stream().filter(a -> a.getHearingName()
//                    .equalsIgnoreCase(scheduleHearing.getEventType().getValue())).findFirst();
//            if (optionalHearing.isPresent() && (optionalHearing.get().getHearingTime() != null)) {
//                scheduleHearing.setHearingTimeInMinutes(optionalHearing.get().getHearingTime());
//                log.info("Minutes to be allotted {} for Schedule Hearing {}", scheduleHearing.getHearingTimeInMinutes(),
//                        scheduleHearing.getHearingBookingId());
//            }
//        }
        log.info("operation = fillHearingTimesWithDataFromMdms, result = SUCCESS, judgeId = {}", scheduleHearings.get(0).getJudgeId());
    }

    private List<MdmsHearing> getHearingDataFromMdms() {
        log.info("operation = getHearingDataFromMdms, result = IN_PROGRESS");
        RequestInfo requestInfo = new RequestInfo();
        Map<String, Map<String, JSONArray>> defaultHearingsData =
                mdmsUtil.fetchMdmsData(requestInfo, config.getEgovStateTenantId(),
                        serviceConstants.DEFAULT_COURT_MODULE_NAME,
                        Collections.singletonList(serviceConstants.DEFAULT_HEARING_MASTER_NAME));
        JSONArray jsonArray = defaultHearingsData.get("court").get("hearings");
        List<MdmsHearing> mdmsHearings = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (Object obj : jsonArray) {
            MdmsHearing hearing = objectMapper.convertValue(obj, MdmsHearing.class);
            mdmsHearings.add(hearing);
        }
        log.info("operation = getHearingDataFromMdms, result = SUCCESS");
        return mdmsHearings;
    }

    private void generateCauseListFromHearings(List<ScheduleHearing> scheduleHearings, List<CauseList> causeLists) {
        log.info("operation = generateCauseListFromHearings, result = SUCCESS, judgeId = {}", scheduleHearings.get(0).getJudgeId());
        List<MdmsSlot> mdmsSlotList = getSlottingDataFromMdms();
//        scheduleHearings.sort(Comparator.comparing(ScheduleHearing::getEventType));
        int currentSlotIndex = 0; // Track the current slot index
        int accumulatedTime = 0; // Track accumulated hearing time within the slot

        for (ScheduleHearing hearing : scheduleHearings) {
            while (currentSlotIndex < mdmsSlotList.size()) {
                MdmsSlot mdmsSlot = mdmsSlotList.get(currentSlotIndex);
                int hearingTime = hearing.getHearingTimeInMinutes();

                if (accumulatedTime + hearingTime <= mdmsSlot.getSlotDuration()) {
                    CauseList causeList = getCauseListFromHearingAndSlot(hearing, mdmsSlot);
                    causeLists.add(causeList);
                    accumulatedTime += hearingTime;
                    break; // Move to the next hearing
                } else {
                    // Move to the next mdmsSlot
                    currentSlotIndex++;
                    accumulatedTime = 0; // Reset accumulated time for the new mdmsSlot
                }
            }

            if (currentSlotIndex == mdmsSlotList.size()) {
                // Add remaining cases to the last slot
                MdmsSlot lastMdmsSlot = mdmsSlotList.get(mdmsSlotList.size() - 1);
                CauseList causeList = getCauseListFromHearingAndSlot(hearing, lastMdmsSlot);
                causeLists.add(causeList);
            }
        }
        log.info("operation = generateCauseListFromHearings, result = SUCCESS, judgeId = {}", scheduleHearings.get(0).getJudgeId());
    }

    private static CauseList getCauseListFromHearingAndSlot(ScheduleHearing hearing, MdmsSlot mdmsSlot) {
        log.info("Added hearing {} to slot {}", hearing.getHearingBookingId(), mdmsSlot.getSlotName());
        return CauseList.builder()
                .judgeId(hearing.getJudgeId())
                .courtId(hearing.getCourtId())
                .tenantId(hearing.getTenantId())
                .caseId(hearing.getCaseId())
//                .typeOfHearing(hearing.getEventType().getValue())
                .tentativeSlot(mdmsSlot.getSlotName())
//                .caseDate(hearing.getDate().toString())
                .caseTitle(hearing.getTitle())
                .build();
    }


    private List<MdmsSlot> getSlottingDataFromMdms() {
        log.info("operation = getSlottingDataFromMdms, result = IN_PROGRESS");
        RequestInfo requestInfo = new RequestInfo();
        Map<String, Map<String, JSONArray>> defaultHearingsData =
                mdmsUtil.fetchMdmsData(requestInfo, config.getEgovStateTenantId(),
                        serviceConstants.DEFAULT_COURT_MODULE_NAME,
                        Collections.singletonList(serviceConstants.DEFAULT_SLOTTING_MASTER_NAME));
        JSONArray jsonArray = defaultHearingsData.get("court").get("slots");
        List<MdmsSlot> mdmsSlots = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (Object obj : jsonArray) {
            MdmsSlot mdmsSlot = objectMapper.convertValue(obj, MdmsSlot.class);
            mdmsSlots.add(mdmsSlot);
        }
        log.info("operation = getSlottingDataFromMdms, result = SUCCESS");
        return mdmsSlots;
    }

    public List<CauseList> viewCauseListForTomorrow(CauseListSearchRequest searchRequest) {
        log.info("operation = viewCauseListForTomorrow, with searchRequest : {}", searchRequest.toString());
        return getCauseListForTomorrow(searchRequest.getCauseListSearchCriteria());
    }

    private List<CauseList> getCauseListForTomorrow(CauseListSearchCriteria searchCriteria) {
        if (searchCriteria != null && searchCriteria.getSearchDate() != null
                && searchCriteria.getSearchDate().isAfter(LocalDate.now().plusDays(1))) {
            throw new CustomException("DK_CL_APP_ERR", "CauseList Search date cannot be after than tomorrow");
        }
        return causeListRepository.getCauseLists(searchCriteria);
    }

    public ByteArrayResource downloadCauseListForTomorrow(CauseListSearchRequest searchRequest) {
        log.info("operation = downloadCauseListForTomorrow, with searchRequest : {}", searchRequest.toString());
        List<CauseList> causeLists = getCauseListForTomorrow(searchRequest.getCauseListSearchCriteria());
        CauseListRequest causeListRequest = CauseListRequest.builder()
                .requestInfo(searchRequest.getRequestInfo()).causeList(causeLists).build();
        return pdfServiceUtil.generatePdfFromPdfService(causeListRequest , searchRequest.getRequestInfo().getUserInfo().getTenantId(),
                config.getCauseListPdfTemplateKey());
    }
}
