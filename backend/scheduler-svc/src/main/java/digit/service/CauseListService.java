package digit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.config.ServiceConstants;
import digit.kafka.producer.Producer;
import digit.repository.CauseListRepository;
import digit.repository.HearingRepository;
import digit.util.*;
import digit.web.models.*;
import digit.web.models.cases.CaseCriteria;
import digit.web.models.cases.SearchCaseRequest;
import digit.web.models.hearing.Hearing;
import digit.web.models.hearing.HearingListSearchRequest;
import digit.web.models.hearing.HearingSearchCriteria;
import digit.web.models.hearing.HearingUpdateBulkRequest;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static digit.models.coremodels.user.enums.UserType.CITIZEN;

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

    private HearingUtil hearingUtil;

    private CaseUtil caseUtil;

    private DateUtil dateUtil;

    private ObjectMapper objectMapper;

    private ApplicationUtil applicationUtil;

    private FileStoreUtil fileStoreUtil;

    private UserService userService;

    @Autowired
    public CauseListService(HearingRepository hearingRepository, CauseListRepository causeListRepository,
                            Producer producer, Configuration config, PdfServiceUtil pdfServiceUtil,
                            MdmsUtil mdmsUtil, ServiceConstants serviceConstants, HearingUtil hearingUtil,
                            CaseUtil caseUtil, DateUtil dateUtil, ObjectMapper objectMapper, ApplicationUtil applicationUtil,
                            FileStoreUtil fileStoreUtil, UserService userService) {
        this.hearingRepository = hearingRepository;
        this.causeListRepository = causeListRepository;
        this.producer = producer;
        this.config =  config;
        this.pdfServiceUtil = pdfServiceUtil;
        this.mdmsUtil = mdmsUtil;
        this.serviceConstants = serviceConstants;
        this.hearingUtil = hearingUtil;
        this.caseUtil = caseUtil;
        this.dateUtil = dateUtil;
        this.objectMapper = objectMapper;
        this.applicationUtil = applicationUtil;
        this.fileStoreUtil = fileStoreUtil;
        this.userService = userService;
    }

    public void updateCauseListForTomorrow() {
        log.info("operation = updateCauseListForTomorrow, result = IN_PROGRESS");
        List<CauseList> causeLists = new ArrayList<>();
        //TODO get judges from db once tables are ready
        List<String> courtIds = new ArrayList<>();
        courtIds.add(config.getCourtId());

        // Multi Thread processing: process 10 judges at a time
        ExecutorService executorService = Executors.newCachedThreadPool();

        // Submit tasks for each judge
        submitTasks(executorService, courtIds, causeLists);

        // Wait for all tasks to complete
        waitForTasksCompletion(executorService);

        if (!CollectionUtils.isEmpty(causeLists)) {
            RequestInfo requestInfo = createInternalRequestInfo();
            CauseListRequest causeListRequest = CauseListRequest.builder().requestInfo(requestInfo)
                    .causeList(causeLists).build();

            producer.push(config.getCauseListInsertTopic(), causeListRequest);
            updateBulkHearing(causeLists);
        } else {
            log.info("No cause lists to be created");
        }
        log.info("operation = updateCauseListForTomorrow, result = SUCCESS");
    }

    private void submitTasks(ExecutorService executorService, List<String> courtIds, List<CauseList> causeLists) {
        for (String courtId : courtIds) {
            // Submit a task to the executor service for each judge
            executorService.submit(() -> generateCauseList(courtId, causeLists, null, null));
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

    public void generateCauseList(String courtId, List<CauseList> causeLists, String hearingDate, String uuid) {
        log.info("operation = generateCauseListForJudge, result = IN_PROGRESS, judgeId = {}", courtId);
        try {
            HearingSearchCriteria hearingSearchCriteria = HearingSearchCriteria.builder()
                    .fromDate(getFromDate(hearingDate))
                    .toDate(getToDate(hearingDate))
                    .courtId(config.getCourtEnabled() ? courtId : null)
                    .build();
            List<Hearing> hearingList = getHearingsForCourt(hearingSearchCriteria);
            if(hearingList.isEmpty()){
                log.info("No hearings found for court: {}", courtId);
                return;
            }

            List<CauseList> causeList  = getCauseListFromHearings(hearingList);
            enrichCauseList(causeList);

            Map<String, Integer> hearingTypeMap = getHearingTypeMap(causeList);
            List<CaseType> caseTypePriority = getCaseTypeMap();

            causeList.sort(Comparator
                    .comparing((CauseList a) -> {
                        Integer hearingTypeId = hearingTypeMap.get(a.getHearingType());
                        return hearingTypeId != null ? hearingTypeId : Integer.MAX_VALUE;
                    }, Comparator.nullsLast(Integer::compareTo))
                    .thenComparing(a -> {
                        CaseType caseType = caseTypePriority.stream()
                                .filter(b -> b.getCaseType().equalsIgnoreCase(a.getCaseType()))
                                .findFirst()
                                .orElse(null);
                        return caseType != null ? caseType.getPriority() : Integer.MAX_VALUE;
                    }, Comparator.nullsLast(Integer::compareTo))
                    .thenComparing(CauseList::getCaseRegistrationDate, Comparator.nullsLast(Comparable::compareTo)));

            generateCauseListFromHearings(causeList);
            ByteArrayResource byteArrayResource = generateCauseListPdf(causeList);
            Document document = fileStoreUtil.saveDocumentToFileStore(byteArrayResource, config.getEgovStateTenantId());

            CauseListPdf causeListPdf = CauseListPdf.builder()
                    .courtId(config.getCourtId())
                    .tenantId(config.getEgovStateTenantId())
                    .judgeId(causeList.get(0).getJudgeId())
                    .fileStoreId(document.getFileStore())
                    .date(dateUtil.getLocalDateFromEpoch(causeList.get(0).getStartTime()).toString())
                    .createdTime(dateUtil.getEpochFromLocalDateTime(LocalDateTime.now()))
                    .createdBy(uuid == null ? serviceConstants.SYSTEM_ADMIN : uuid)
                    .build();

            RequestInfo requestInfo = createInternalRequestInfo();
            CauseListPdfRequest causeListPdfRequest = CauseListPdfRequest.builder().requestInfo(requestInfo).causeListPdf(causeListPdf).build();

            producer.push(config.getCauseListPdfTopic(), causeListPdfRequest);
            causeLists.addAll(causeList);
            log.info("operation = generateCauseListForJudge, result = SUCCESS, judgeId = {}", courtId);
        } catch (Exception e) {
            log.error("operation = generateCauseListForJudge, result = FAILURE, judgeId = {}, error = {}", courtId, e.getMessage(), e);
        }
    }

    private Long getToDate(String hearingDate) {
        return hearingDate == null
                ? dateUtil.getEpochFromLocalDateTime(LocalDateTime.now().toLocalDate().plusDays(2).atStartOfDay())
                : dateUtil.getEpochFromLocalDateTime(LocalDate.parse(hearingDate).plusDays(1).atStartOfDay());
    }

    private Long getFromDate(String hearingDate) {
        return hearingDate == null
                ? dateUtil.getEpochFromLocalDateTime(LocalDateTime.now().toLocalDate().plusDays(1).atStartOfDay())
                : dateUtil.getEpochFromLocalDateTime(LocalDate.parse(hearingDate).atStartOfDay());
    }


    public List<CaseType> getCaseTypeMap() {
        log.info("operation = getCaseTypeMap, result = IN_PROGRESS");
        List<CaseType> caseTypeList = new ArrayList<>();
        try {
            caseTypeList = causeListRepository.getCaseTypes();
            caseTypeList.sort(Comparator.comparing(CaseType::getPriority));
        } catch (Exception e) {
            log.error("operation = getCaseTypeMap, result = FAILURE, error = {}", e.getMessage(), e);
        }
        return caseTypeList;
    }
    private Map<String, Integer> getHearingTypeMap(List<CauseList> causeLists) {
        log.info("operation = getHearingTypePriority, result = IN_PROGRESS");
        Map<String, Integer> hearingTypePrioriyMap = new HashMap<>();
        try {
            List<MdmsHearing> mdmsHearings = getHearingDataFromMdms();
            for (MdmsHearing mdmsHearing : mdmsHearings) {
                hearingTypePrioriyMap.put(mdmsHearing.getHearingType(), mdmsHearing.getPriority());
            }

            for(CauseList cause: causeLists){
                Optional<MdmsHearing> optionalHearing = mdmsHearings.stream().filter(a -> a.getHearingType()
                        .equalsIgnoreCase(cause.getHearingType())).findFirst();
                if (optionalHearing.isPresent() && (optionalHearing.get().getHearingTime() != null)) {
                    cause.setHearingTimeInMinutes(optionalHearing.get().getHearingTime());
                    log.info("Minutes to be allotted {} for CauseList {}", cause.getHearingTimeInMinutes(),
                            cause.getId());
                }
            }
            log.info("operation = getHearingTypePriority, result = SUCCESS");
        } catch (Exception e) {
            log.error("operation = getHearingTypePriority, result = FAILURE, error = {}", e.getMessage(), e);
        }
        return hearingTypePrioriyMap;
    }

    private List<MdmsHearing> getHearingDataFromMdms() {
        log.info("operation = getHearingDataFromMdms, result = IN_PROGRESS");
        List<MdmsHearing> mdmsHearings = new ArrayList<>();
        try {
            RequestInfo requestInfo = new RequestInfo();
            Map<String, Map<String, JSONArray>> defaultHearingsData =
                    mdmsUtil.fetchMdmsData(requestInfo, config.getEgovStateTenantId(),
                            serviceConstants.DEFAULT_COURT_MODULE_NAME,
                            Collections.singletonList(serviceConstants.DEFAULT_HEARING_MASTER_NAME));
            JSONArray jsonArray = defaultHearingsData.get(serviceConstants.DEFAULT_COURT_MODULE_NAME).get(serviceConstants.DEFAULT_HEARING_MASTER_NAME);
            ObjectMapper objectMapper = new ObjectMapper();
            for (Object obj : jsonArray) {
                MdmsHearing hearing = objectMapper.convertValue(obj, MdmsHearing.class);
                mdmsHearings.add(hearing);
            }
            log.info("operation = getHearingDataFromMdms, result = SUCCESS");
        } catch (Exception e) {
            log.error("operation = getHearingDataFromMdms, result = FAILURE, error = {}", e.getMessage(), e);
        }
        return mdmsHearings;
    }

    private void generateCauseListFromHearings(List<CauseList> causeList) {
        log.info("operation = generateCauseListFromHearings, result = SUCCESS, judgeId = {}", causeList.get(0).getJudgeId());
        try {
            List<MdmsSlot> mdmsSlotList = getSlottingDataFromMdms();
            Collections.reverse(mdmsSlotList);int currentSlotIndex = 0; // Track the current slot index
            int accumulatedTime = 0; // Track accumulated hearing time within the slot

            for(CauseList causeList1 : causeList){
                while(currentSlotIndex < mdmsSlotList.size()){
                    MdmsSlot mdmsSlot = mdmsSlotList.get(currentSlotIndex);
                    int hearingTime = causeList1.getHearingTimeInMinutes();

                    if(accumulatedTime + hearingTime <= mdmsSlot.getSlotDuration()){
                        getCauseListFromHearingAndSlot(causeList1, mdmsSlot, accumulatedTime);
                        accumulatedTime += hearingTime;
                        break;
                    } else {
                        currentSlotIndex++;
                        accumulatedTime = 0;
                    }
                    if (currentSlotIndex == mdmsSlotList.size()) {
                        MdmsSlot lastMdmsSlot = mdmsSlotList.get(mdmsSlotList.size() - 1);
                        getCauseListFromHearingAndSlot(causeList1, lastMdmsSlot, accumulatedTime);
                    }
                }
            }
            log.info("operation = generateCauseListFromHearings, result = SUCCESS, judgeId = {}", causeList.get(0).getJudgeId());
        } catch (Exception e) {
            log.error("operation = generateCauseListFromHearings, result = FAILURE, judgeId = {}, error = {}", causeList.get(0).getJudgeId(), e.getMessage(), e);
        }
    }

    private void getCauseListFromHearingAndSlot(CauseList causeList, MdmsSlot mdmsSlot, int accumulatedTime) {
        Long slotStartTime = dateUtil.getEpochFromLocalDateTime(LocalDateTime.of(dateUtil.getLocalDateFromEpoch(causeList.getStartTime()), LocalTime.parse(mdmsSlot.getSlotStartTime())));
        long startTime = slotStartTime + ((long) accumulatedTime * 60 * 1000);
        Long endTime = startTime + (causeList.getHearingTimeInMinutes() * 60 * 1000);
        causeList.setSlot(mdmsSlot.getSlotName());
        causeList.setStartTime(startTime);
        causeList.setEndTime(endTime);
    }


    private List<MdmsSlot> getSlottingDataFromMdms() {
        log.info("operation = getSlottingDataFromMdms, result = IN_PROGRESS");
        List<MdmsSlot> mdmsSlots = new ArrayList<>();
        try {
            RequestInfo requestInfo = new RequestInfo();
            Map<String, Map<String, JSONArray>> defaultHearingsData =
                    mdmsUtil.fetchMdmsData(requestInfo, config.getEgovStateTenantId(),
                            serviceConstants.DEFAULT_COURT_MODULE_NAME,
                            Collections.singletonList(serviceConstants.DEFAULT_SLOTTING_MASTER_NAME));
            JSONArray jsonArray = defaultHearingsData.get("court").get("slots");
            ObjectMapper objectMapper = new ObjectMapper();
            for (Object obj : jsonArray) {
                MdmsSlot mdmsSlot = objectMapper.convertValue(obj, MdmsSlot.class);
                mdmsSlots.add(mdmsSlot);
            }
            log.info("operation = getSlottingDataFromMdms, result = SUCCESS");
        } catch (Exception e) {
            log.error("operation = getSlottingDataFromMdms, result = FAILURE, error = {}", e.getMessage(), e);
        }
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

    public List<String> getFileStoreForCauseList(CauseListSearchCriteria searchCriteria) {
        if (searchCriteria != null && searchCriteria.getSearchDate() != null
                && searchCriteria.getSearchDate().isAfter(LocalDate.now().plusDays(1))) {
            throw new CustomException("DK_CL_APP_ERR", "CauseList Search date cannot be after than tomorrow");
        }
        return causeListRepository.getCauseListFileStore(searchCriteria);
    }
    public ByteArrayResource downloadCauseListForTomorrow(CauseListSearchRequest searchRequest) {
        log.info("operation = downloadCauseListForTomorrow, with searchRequest : {}", searchRequest.toString());
        List<String> fileStoreIds = getFileStoreForCauseList(searchRequest.getCauseListSearchCriteria());
        if(CollectionUtils.isEmpty(fileStoreIds)){
            throw new CustomException("DK_CL_APP_ERR", "No CauseList found for the given search criteria");
        }
        byte[] pdfBytes = fileStoreUtil.getFile(config.getEgovStateTenantId(), fileStoreIds.get(0));
        return new ByteArrayResource(pdfBytes);
    }

    public ByteArrayResource generateCauseListPdf(List<CauseList> causeLists){
        log.info("operation = generateCauseListPdf, result = IN_PROGRESS");
        ByteArrayResource byteArrayResource = null;
        try {
           List<SlotList> slotLists;
           slotLists = buildSlotList(causeLists);
           for(SlotList slotList : slotLists){
               addIndexing(slotList.getCauseLists());
           }
            Map<String, List<SlotList>> groupedSlots = slotLists.stream()
                    .collect(Collectors.groupingBy(SlotList::getSlotName));

           List<HearingListPriority> hearingListPriorities = groupedSlots.entrySet().stream()
                   .map(entry -> {
                          List<SlotList> slotList = entry.getValue();
                          return HearingListPriority.builder()
                                 .slotName(entry.getKey())
                                 .slotList(slotList)
                                 .slotStartTime(slotList.get(0).getSlotStartTime())
                                 .slotEndTime(slotList.get(0).getSlotEndTime())
                                 .build();
                   })
                   .toList();

           SlotRequest slotRequest = SlotRequest.builder()
                   .requestInfo(createInternalRequestInfo())
                   .hearingListPriority(hearingListPriorities).build();

           byteArrayResource =  pdfServiceUtil.generatePdfFromPdfService(slotRequest, config.getEgovStateTenantId(), config.getCauseListPdfTemplateKey());
           log.info("operation = generateCauseListPdf, result = SUCCESS");
        } catch (Exception e) {
            log.error("Error occurred while generating pdf: {}", e.getMessage());
        }
        return byteArrayResource;
    }

    public void addIndexing(List<CauseList> causeLists){
        for(CauseList causeList: causeLists){
            causeList.setIndex(causeLists.indexOf(causeList) + 1);
        }
    }
    public List<SlotList> buildSlotList(List<CauseList> causeLists) {
        List<SlotList> slots = new ArrayList<>();
        List<MdmsHearing> mdmsHearings = getHearingDataFromMdms();
        for (CauseList causeList : causeLists) {
            String slotName = causeList.getSlot();
            if(slotName == null)continue;
            String hearingType = causeList.getHearingType();
            Optional<String> hearingNameOptional = mdmsHearings.stream()
                    .filter(a -> a.getHearingType().equals(causeList.getHearingType()))
                    .map(MdmsHearing::getHearingName)
                    .findFirst();
            SlotList existingSlot = findSlot(slots, slotName, hearingNameOptional.get());

            hearingType = hearingNameOptional.orElse(hearingType);
            if (existingSlot != null) {
                List<CauseList> mutableCauseLists = new ArrayList<>(existingSlot.getCauseLists());
                mutableCauseLists.add(causeList);
                existingSlot.setCauseLists(mutableCauseLists);
            } else {
                String slotStartTime = Objects.equals(slotName, "Morning Slot") ? "10:00:00" : "14:00:00";
                String slotEndTime = Objects.equals(slotName, "Morning Slot") ? "13:00:00" : "17:00:00";
                SlotList newSlot = SlotList.builder()
                        .slotName(slotName)
                        .slotStartTime(slotStartTime)
                        .slotEndTime(slotEndTime)
                        .courtId(config.getCourtName())
                        .hearingType(hearingType)
                        .judgeName(config.getJudgeName())
                        .judgeDesignation(config.getJudgeDesignation())
                        .causeLists(List.of(causeList)).build();
                slots.add(newSlot);
            }
        }

        return slots;
    }

    private SlotList findSlot(List<SlotList> slots, String slotName, String hearingType) {
        for (SlotList slot : slots) {
            if (slot.getSlotName().equals(slotName) && slot.getHearingType().equals(hearingType)) {
                return slot;
            }
        }
        return null;
    }

    public List<Hearing> getHearingsForCourt(HearingSearchCriteria hearingSearchCriteria) {
        log.info("operation = getHearingsForCourt, result = IN_PROGRESS, courtId = {}", hearingSearchCriteria.getCourtId());
        List<Hearing> hearings = new ArrayList<>();
        try {
            RequestInfo requestInfo = new RequestInfo();
            HearingListSearchRequest hearingListSearchRequest = HearingListSearchRequest.builder()
                    .requestInfo(requestInfo)
                    .criteria(hearingSearchCriteria)
                    .build();
            hearings = hearingUtil.fetchHearing(hearingListSearchRequest);
        } catch (Exception e) {
            log.error("Error occurred while fetching hearings for court: {}", e.getMessage());
        }
        return hearings;
    }

    public List<CauseList> getCauseListFromHearings(List<Hearing> hearingList) {
        List<CauseList> causeLists = new ArrayList<>();
        for (Hearing hearing : hearingList) {
            CauseList causeList = CauseList.builder()
                    .id(hearing.getId())
                    .tenantId(hearing.getTenantId())
                    .hearingId(hearing.getHearingId())
                    .filingNumber(hearing.getFilingNumber().get(0))
                    .cnrNumbers(hearing.getCnrNumbers())
                    .applicationNumbers(hearing.getApplicationNumbers())
                    .hearingType(hearing.getHearingType())
                    .status(hearing.getStatus())
                    .startTime(hearing.getStartTime())
                    .endTime(hearing.getEndTime())
                    .presidedBy(hearing.getPresidedBy())
                    .attendees(hearing.getAttendees())
                    .transcript(hearing.getTranscript())
                    .vcLink(hearing.getVcLink())
                    .isActive(hearing.getIsActive())
                    .documents(hearing.getDocuments())
                    .additionalDetails(hearing.getAdditionalDetails())
                    .auditDetails(hearing.getAuditDetails())
                    .workflow(hearing.getWorkflow())
                    .notes(hearing.getNotes())
                    .courtId(config.getCourtId())
                    .judgeName(config.getJudgeName())
                    .judgeDesignation(config.getJudgeDesignation())
                    .hearingDate(dateUtil.getLocalDateFromEpoch(hearing.getStartTime()).toString())
                    .build();

            causeLists.add(causeList);
        }
        return causeLists;
    }

    public void enrichCauseList(List<CauseList> causeLists) {
        for(CauseList causeList: causeLists) {
            enrichCase(causeList);
            enrichApplication(causeList);
        }
    }

    public void enrichCase(CauseList causeList) {
        log.info("operation = enrichCase, result = IN_PROGRESS, filingNumber = {}", causeList.getFilingNumber());
        try {
            CaseCriteria criteria = CaseCriteria.builder().filingNumber(causeList.getFilingNumber()).build();
            SearchCaseRequest searchCaseRequest = SearchCaseRequest.builder()
                    .RequestInfo(createInternalRequestInfo())
                    .tenantId(config.getEgovStateTenantId())
                    .criteria(Collections.singletonList(criteria))
                    .build();

            JsonNode caseList = caseUtil.getCases(searchCaseRequest);
            if(caseList != null) {
                JsonNode representatives = caseList.get(0).get("representatives");
                JsonNode litigants = caseList.get(0).get("litigants");


                causeList.setCaseId(caseList.get(0).get("id").isNull() ? null : caseList.get(0).get("id").asText());
                causeList.setCaseType(caseList.get(0).get("caseType").isNull() ? null : caseList.get(0).get("caseType").asText());
                causeList.setCaseTitle(caseList.get(0).get("caseTitle").isNull() ? null : caseList.get(0).get("caseTitle").asText());
                causeList.setCaseNumber(caseList.get(0).get("courtCaseNumber").isNull() ? null : caseList.get(0).get("courtCaseNumber").asText());
                causeList.setCmpNumber(caseList.get(0).get("cmpNumber").isNull() ? null : caseList.get(0).get("cmpNumber").asText());

                long registrationDate = caseList.get(0).get("registrationDate").asLong();
                causeList.setCaseRegistrationDate(registrationDate);

                List<AdvocateMapping> advocateMappings;
                List<Party> litigantsList;
                try {
                    advocateMappings = objectMapper.readValue(representatives.toString(),
                            objectMapper.getTypeFactory().constructCollectionType(List.class, AdvocateMapping.class));
                    litigantsList = objectMapper.readValue(litigants.toString(),
                            objectMapper.getTypeFactory().constructCollectionType(List.class, Party.class));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                causeList.setRepresentatives(advocateMappings != null ? advocateMappings : new ArrayList<>());
                causeList.setLitigants(litigantsList != null ? litigantsList : new ArrayList<>());

                List<String> complainantAdvocates = new ArrayList<>();
                List<String> respondentAdvocates = new ArrayList<>();
                assert litigantsList != null;
                for(Party party: litigantsList) {
                    assert advocateMappings != null;
                    AdvocateMapping advocateDetails= isAdvocatePresent(party.getIndividualId(), advocateMappings);
                    if(party.getPartyType().equals(serviceConstants.COMPLAINANT)) {
                        if (advocateDetails != null) {
                            LinkedHashMap advocate = ((LinkedHashMap) advocateDetails.getAdditionalDetails());
                            complainantAdvocates.add(advocate.get(serviceConstants.ADVOCATE_NAME).toString());
                        }
                    }
                    else if(party.getPartyType().equals(serviceConstants.RESPONDENT)) {
                        if (advocateDetails != null) {
                            LinkedHashMap advocate = ((LinkedHashMap) advocateDetails.getAdditionalDetails());
                            respondentAdvocates.add(advocate.get(serviceConstants.ADVOCATE_NAME).toString());
                        }
                    }

                }

                if(complainantAdvocates.isEmpty()){
                    complainantAdvocates.add("");
                }
                if(respondentAdvocates.isEmpty()){
                    respondentAdvocates.add("");
                }
                causeList.setComplainantAdvocates(complainantAdvocates);
                causeList.setRespondentAdvocates(respondentAdvocates);

                List<String> advocateNames = new ArrayList<>();
                advocateNames.addAll(complainantAdvocates);
                advocateNames.addAll(respondentAdvocates);
                causeList.setAdvocateNames(advocateNames);

                log.info("operation = enrichCase, result = SUCCESS, filingNumber = {}", causeList.getFilingNumber());
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching case for filing number: {}", causeList.getFilingNumber());
        }
    }

    private AdvocateMapping isAdvocatePresent(String individualId, List<AdvocateMapping> representatives) {
        for(AdvocateMapping advocateMapping: representatives) {
            if(advocateMapping.getRepresenting().stream().anyMatch(a -> a.getIndividualId().equals(individualId))) {
                return advocateMapping;
            }
        }
        return null;
    }
    public void enrichApplication(CauseList causeList) {
        log.info("operation = enrichApplication, result = IN_PROGRESS, filingNumber = {}", causeList.getFilingNumber());

        ApplicationCriteria criteria = ApplicationCriteria.builder()
                .filingNumber(causeList.getFilingNumber())
                .tenantId(config.getEgovStateTenantId())
                .status(serviceConstants.APPLICATION_STATE)
                .build();
        ApplicationRequest applicationRequest = ApplicationRequest.builder()
                .requestInfo(createInternalRequestInfo())
                .criteria(criteria)
                .build();

        try {
            JsonNode applicationList = applicationUtil.getApplications(applicationRequest);

            if(applicationList != null) {
                List<String> applicationNumbers = new ArrayList<>();
                for (JsonNode application : applicationList) {
                    applicationNumbers.add(application.get("applicationNumber").asText());
                }
                causeList.setApplicationNumbers(applicationNumbers);
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching applications for filing number: {}", causeList.getFilingNumber());
        }
    }

    public void updateBulkHearing(List<CauseList> causeLists) {
        List<Hearing> hearingList = new ArrayList<>();
        for(CauseList causeList: causeLists){
            Hearing hearing = Hearing.builder()
                    .id(causeList.getId())
                    .tenantId(causeList.getTenantId())
                    .hearingId(causeList.getHearingId())
                    .filingNumber(Collections.singletonList(causeList.getFilingNumber()))
                    .applicationNumbers(causeList.getApplicationNumbers())
                    .hearingType(causeList.getHearingType())
                    .status(causeList.getStatus())
                    .startTime(causeList.getStartTime())
                    .endTime(causeList.getEndTime())
                    .build();
            hearingList.add(hearing);
        }

        HearingUpdateBulkRequest updateBulkRequest = HearingUpdateBulkRequest.builder()
                .requestInfo(createInternalRequestInfo())
                .hearings(hearingList)
                .build();
        hearingUtil.callHearing(updateBulkRequest);
    }

    private RequestInfo createInternalRequestInfo() {
        User userInfo = new User();
        userInfo.setUuid(userService.internalMicroserviceRoleUuid);
        return RequestInfo.builder().userInfo(userInfo).build();
    }
}
