package org.pucar.dristi.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.models.individual.Individual;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.HearingRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.HearingRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.DateUtil;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.util.SchedulerUtil;
import org.pucar.dristi.validator.HearingRegistrationValidator;
import org.pucar.dristi.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.pucar.dristi.config.ServiceConstants.*;

@Service
@Slf4j
public class HearingService {

    private final HearingRegistrationValidator validator;
    private final HearingRegistrationEnrichment enrichmentUtil;
    private final WorkflowService workflowService;
    private final HearingRepository hearingRepository;
    private final Producer producer;
    private final Configuration config;
    private final CaseUtil caseUtil;
    private final ObjectMapper objectMapper;
    private final IndividualService individualService;
    private final SmsNotificationService notificationService;
    private final MdmsUtil mdmsUtil;
    private final DateUtil dateUtil;
    private final SchedulerUtil schedulerUtil;

    @Autowired
    public HearingService(
            HearingRegistrationValidator validator,
            HearingRegistrationEnrichment enrichmentUtil,
            WorkflowService workflowService,
            HearingRepository hearingRepository,
            Producer producer,
            Configuration config, CaseUtil caseUtil, ObjectMapper objectMapper, IndividualService individualService, SmsNotificationService notificationService, MdmsUtil mdmsUtil, DateUtil dateUtil, SchedulerUtil schedulerUtil) {
        this.validator = validator;
        this.enrichmentUtil = enrichmentUtil;
        this.workflowService = workflowService;
        this.hearingRepository = hearingRepository;
        this.producer = producer;
        this.config = config;
        this.caseUtil = caseUtil;
        this.objectMapper = objectMapper;
        this.individualService = individualService;
        this.notificationService = notificationService;
        this.mdmsUtil = mdmsUtil;
        this.dateUtil = dateUtil;
        this.schedulerUtil = schedulerUtil;
    }

    public Hearing createHearing(HearingRequest body) {
        try {

            // Validate applications
            validator.validateHearingRegistration(body);

            // Enrich applications
            enrichmentUtil.enrichHearingRegistration(body);

            // Initiate workflow for the new application-
            workflowService.updateWorkflowStatus(body);

            // Push the application to the topic for persister to listen and persist

            producer.push(config.getHearingCreateTopic(), body);

            return body.getHearing();
        } catch (CustomException e) {
            log.error("Custom Exception occurred while creating hearing");
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while creating hearing");
            throw new CustomException(HEARING_CREATE_EXCEPTION, e.getMessage());
        }
    }

    public List<Hearing> searchHearing(HearingSearchRequest request) {

        try {
            return hearingRepository.getHearings(request);
        } catch (CustomException e) {
            log.error("Custom Exception occurred while searching");
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching search results");
            throw new CustomException(HEARING_SEARCH_EXCEPTION, e.getMessage());
        }
    }

    public Hearing updateHearing(HearingRequest hearingRequest) {

        try {

            // Validate whether the application that is being requested for update indeed exists
            Hearing hearing = validator.validateHearingExistence(hearingRequest.getRequestInfo(), hearingRequest.getHearing());

            // Updating Hearing request
            hearing.setWorkflow(hearingRequest.getHearing().getWorkflow());
            hearing.setStartTime(hearingRequest.getHearing().getStartTime());
            hearing.setEndTime(hearingRequest.getHearing().getEndTime());
            hearing.setTranscript(hearingRequest.getHearing().getTranscript());
            hearing.setNotes(hearingRequest.getHearing().getNotes());
            hearing.setAttendees(hearingRequest.getHearing().getAttendees());
            hearing.setDocuments(hearingRequest.getHearing().getDocuments());
            hearing.setAdditionalDetails(hearingRequest.getHearing().getAdditionalDetails());
            hearing.setVcLink(hearingRequest.getHearing().getVcLink());
            hearingRequest.setHearing(hearing);
            workflowService.updateWorkflowStatus(hearingRequest);

            // Enrich application upon update
            enrichmentUtil.enrichHearingApplicationUponUpdate(hearingRequest);

            String updatedState = hearingRequest.getHearing().getStatus();
            producer.push(config.getHearingUpdateTopic(), hearingRequest);

            callNotificationService(hearingRequest, updatedState);

            return hearingRequest.getHearing();

        } catch (CustomException e) {
            log.error("Custom Exception occurred while updating hearing");
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while updating hearing");
            throw new CustomException(HEARING_UPDATE_EXCEPTION, "Error occurred while updating hearing: " + e.getMessage());
        }

    }

    public HearingExists isHearingExist(HearingExistsRequest body) {
        try {
            HearingExists order = body.getOrder();
            HearingCriteria criteria = HearingCriteria.builder().cnrNumber(order.getCnrNumber())
                    .filingNumber(order.getFilingNumber()).applicationNumber(order.getApplicationNumber()).hearingId(order.getHearingId()).tenantId(body.getRequestInfo().getUserInfo().getTenantId()).build();
            Pagination pagination = Pagination.builder().limit(1.0).offSet((double) 0).build();
            HearingSearchRequest hearingSearchRequest = HearingSearchRequest.builder().criteria(criteria).pagination(pagination).build();
            List<Hearing> hearingList = hearingRepository.getHearings(hearingSearchRequest);
            order.setExists(!hearingList.isEmpty());
            return order;
        } catch (CustomException e) {
            log.error("Custom Exception occurred while verifying hearing");
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while verifying hearing");
            throw new CustomException(HEARING_SEARCH_EXCEPTION, "Error occurred while searching hearing: " + e.getMessage());
        }
    }

    public Hearing updateTranscriptAdditionalAttendees(HearingRequest hearingRequest) {
        try {
            Hearing hearing = validator.validateHearingExistence(hearingRequest.getRequestInfo(), hearingRequest.getHearing());
            enrichmentUtil.enrichHearingApplicationUponUpdate(hearingRequest);
            hearingRepository.updateTranscriptAdditionalAttendees(hearingRequest.getHearing());
            hearing.setTranscript(hearingRequest.getHearing().getTranscript());
            hearing.setAuditDetails(hearingRequest.getHearing().getAuditDetails());
            hearing.setAdditionalDetails(hearingRequest.getHearing().getAdditionalDetails());
            hearing.setAttendees(hearingRequest.getHearing().getAttendees());
            hearing.setVcLink(hearingRequest.getHearing().getVcLink());
            hearing.setNotes(hearingRequest.getHearing().getNotes());
            return hearing;
        } catch (CustomException e) {
            log.error("Custom Exception occurred while verifying hearing");
            throw e;
        } catch (Exception e) {
            throw new CustomException(HEARING_UPDATE_EXCEPTION, "Error occurred while updating hearing: " + e.getMessage());
        }
    }

    public void updateStartAndTime(UpdateTimeRequest body) {
        try {
            for (Hearing hearing : body.getHearings()) {
                if (hearing.getHearingId() == null || hearing.getHearingId().isEmpty()) {
                    throw new CustomException(HEARING_UPDATE_TIME_EXCEPTION, "Hearing Id is required for updating start and end time");
                }
                Hearing existingHearing = validator.validateHearingExistence(body.getRequestInfo(), hearing);
                existingHearing.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
                existingHearing.getAuditDetails().setLastModifiedBy(body.getRequestInfo().getUserInfo().getUuid());
                hearing.setAuditDetails(existingHearing.getAuditDetails());

                HearingRequest hearingRequest = HearingRequest.builder().requestInfo(body.getRequestInfo())
                        .hearing(hearing).build();
                producer.push(config.getStartEndTimeUpdateTopic(), hearingRequest);
            }

        } catch (CustomException e) {
            log.error("Custom Exception occurred while updating hearing start and end time");
            throw e;
        } catch (Exception e) {
            throw new CustomException(HEARING_UPDATE_TIME_EXCEPTION, "Error occurred while updating hearing start and end time: " + e.getMessage());
        }
    }

    public Hearing uploadWitnessDeposition(HearingRequest hearingRequest) {

        try {

            // Validate whether the application that is being requested for update indeed exists
            Hearing hearing = validator.validateHearingExistence(hearingRequest.getRequestInfo(), hearingRequest.getHearing());

            // Updating Hearing request

            hearing.setDocuments(hearingRequest.getHearing().getDocuments());
            hearingRequest.setHearing(hearing);

            // Enrich application upon update
            enrichmentUtil.enrichHearingApplicationUponUpdate(hearingRequest);

            producer.push(config.getHearingUpdateTopic(), hearingRequest);

            return hearingRequest.getHearing();

        } catch (CustomException e) {
            log.error("Custom Exception occurred while uploading witness deposition pdf");
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while uploading witness deposition pdf");
            throw new CustomException(WITNESS_DEPOSITION_UPDATE_EXCEPTION, "Error occurred while uploading witness deposition pdf: " + e.getMessage());
        }

    }

    private void callNotificationService(HearingRequest hearingRequest, String updatedState) {

        try {
            CaseSearchRequest caseSearchRequest = createCaseSearchRequest(hearingRequest.getRequestInfo(), hearingRequest.getHearing());
            JsonNode caseDetails = caseUtil.searchCaseDetails(caseSearchRequest);

            Object additionalDetailsObject = hearingRequest.getHearing().getAdditionalDetails();
            String jsonData = objectMapper.writeValueAsString(additionalDetailsObject);
            JsonNode additionalData = objectMapper.readTree(jsonData);
            boolean caseAdjourned = additionalData.has("purposeOfAdjournment");

            String messageCode = updatedState != null ? getMessageCode(updatedState, caseAdjourned) : null;
            assert messageCode != null;
            log.info("Message code: {}", messageCode);

            String hearingDate = hearingRequest.getHearing().getStartTime() != null ? hearingRequest.getHearing().getStartTime().toString() : "";
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(hearingDate)), ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DOB_FORMAT_D_M_Y);
            String date = dateTime.format(formatter);

            Set<String> individualIds = extractIndividualIds(caseDetails);

            Set<String> phoneNumbers = callIndividualService(hearingRequest.getRequestInfo(), individualIds);

            SmsTemplateData smsTemplateData = SmsTemplateData.builder()
                    .courtCaseNumber(caseDetails.has("courtCaseNumber") ? caseDetails.get("courtCaseNumber").asText() : "")
                    .cmpNumber(caseDetails.has("cmpNumber") ? caseDetails.get("cmpNumber").asText() : "")
                    .hearingDate(date)
                    .tenantId(hearingRequest.getHearing().getTenantId()).build();

            for (String number : phoneNumbers) {
                notificationService.sendNotification(hearingRequest.getRequestInfo(), smsTemplateData, messageCode, number);
            }
        } catch (Exception e) {
            // Log the exception and continue the execution without throwing
            log.error("Error occurred while sending notification: {}", e.toString());
        }
    }

    public CaseSearchRequest createCaseSearchRequest(RequestInfo requestInfo, Hearing hearing) {
        CaseSearchRequest caseSearchRequest = new CaseSearchRequest();
        caseSearchRequest.setRequestInfo(requestInfo);
        CaseCriteria caseCriteria = CaseCriteria.builder().filingNumber(hearing.getFilingNumber().get(0)).defaultFields(false).build();
        caseSearchRequest.addCriteriaItem(caseCriteria);
        return caseSearchRequest;
    }

    private String getMessageCode(String updatedStatus, Boolean hearingAdjourned) {

        log.info("Operation: getMessage, UpdatedStatus: {}", updatedStatus);
        if (hearingAdjourned && updatedStatus.equalsIgnoreCase(COMPLETED)) {
            return HEARING_ADJOURNED;
        }
        return null;
    }

    public Set<String> extractIndividualIds(JsonNode caseDetails) {
        JsonNode litigantNode = caseDetails.get("litigants");
        JsonNode representativeNode = caseDetails.get("representatives");
        Set<String> uuids = new HashSet<>();

        if (litigantNode.isArray()) {
            for (JsonNode node : litigantNode) {
                String uuid = node.path("additionalDetails").get("uuid").asText();
                if (!uuid.isEmpty()) {
                    uuids.add(uuid);
                }
            }
        }
        if (representativeNode.isArray()) {
            for (JsonNode advocateNode : representativeNode) {
                JsonNode representingNode = advocateNode.get("representing");
                if (representingNode.isArray()) {
                    String uuid = advocateNode.path("additionalDetails").get("uuid").asText();
                    if (!uuid.isEmpty()) {
                        uuids.add(uuid);
                    }
                }
            }
        }
        return uuids;
    }

    private Set<String> callIndividualService(RequestInfo requestInfo, Set<String> ids) {

        Set<String> mobileNumber = new HashSet<>();
        List<Individual> individuals = individualService.getIndividuals(requestInfo, new ArrayList<>(ids));
        for (Individual individual : individuals) {
            if (individual.getMobileNumber() != null) {
                mobileNumber.add(individual.getMobileNumber());
            }
        }
        return mobileNumber;
    }

    public List<ScheduleHearing> bulkReschedule(@Valid BulkRescheduleRequest request) {
        BulkReschedule bulkReschedule = request.getBulkReschedule();
        RequestInfo requestInfo = request.getRequestInfo();
        Set<Integer> slotIds = bulkReschedule.getSlotIds();

        validator.validateBulkRescheduleRequest(requestInfo, bulkReschedule);

        List<Hearing> hearingsToReschedule = getHearingsForBulkReschedule(slotIds, bulkReschedule, requestInfo);

        if (hearingsToReschedule.isEmpty()) {
            log.info("No hearings found for bulk reschedule");
            return new ArrayList<>();
        }

        List<String> hearingIds = hearingsToReschedule.stream().map(Hearing::getHearingId).toList();
        bulkReschedule.setHearingIds(hearingIds);
        request.setBulkReschedule(bulkReschedule);

        List<ScheduleHearing> scheduleHearings = schedulerUtil.callBulkReschedule(request);

        Map<String, ScheduleHearing> scheduleHearingMap = scheduleHearings.stream().collect(Collectors.toMap(ScheduleHearing::getHearingBookingId, obj -> obj));
        for (Hearing hearing : hearingsToReschedule) {
            if (scheduleHearingMap.containsKey(hearing.getHearingId())) {
                ScheduleHearing scheduleHearing = scheduleHearingMap.get(hearing.getHearingId());
                hearing.setStartTime(scheduleHearing.getStartTime());
                hearing.setEndTime(scheduleHearing.getEndTime());
                hearing.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
                hearing.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUuid());
            }
        }
        UpdateTimeRequest reschedule = UpdateTimeRequest.builder().requestInfo(requestInfo).hearings(hearingsToReschedule).build();

        producer.push(config.getBulkRescheduleTopic(),reschedule);
        return scheduleHearings;
    }


    private List<Hearing> getHearingsForBulkReschedule(Set<Integer> slotIds, BulkReschedule bulkReschedule, RequestInfo requestInfo) {

        HearingCriteria criteria = HearingCriteria.builder()
                .tenantId(bulkReschedule.getTenantId()).fromDate(bulkReschedule.getStartTime())
                .toDate(bulkReschedule.getEndTime())
                .courtId(bulkReschedule.getCourtId())
                .build();

        HearingSearchRequest searchRequest = HearingSearchRequest.builder()
                .requestInfo(requestInfo)
                .criteria(criteria)
                .pagination(null)
                .build();

        List<Hearing> hearingsToReschedule = new ArrayList<>();

        if (!slotIds.isEmpty()) {
            // check mdms data for slot filtering if any slot id is there
            Map<String, Map<String, JSONArray>> slotDetails = mdmsUtil.fetchMdmsData(requestInfo, bulkReschedule.getTenantId(), "court", Collections.singletonList("slots"));

            JSONArray slots = slotDetails.get("court").get("slots");
            for (Object slot : slots) {
                HearingSlot hearingSlot = objectMapper.convertValue(slot, HearingSlot.class);
                if (slotIds.contains(hearingSlot.getId())) {

                    Long startDate = bulkReschedule.getStartTime();
                    Long endDate = bulkReschedule.getEndTime();
                    String startTime = hearingSlot.getSlotStartTime();
                    String endTime = hearingSlot.getSlotEndTime();

                    LocalDateTime from = dateUtil.getLocalDateTimeFromEpoch(startDate);
                    LocalDateTime fromLocalDateTime = dateUtil.getLocalDateTime(from, startTime);
                    Long fromDate = dateUtil.getEpochFromLocalDateTime(fromLocalDateTime);

                    LocalDateTime to = dateUtil.getLocalDateTimeFromEpoch(endDate);
                    LocalDateTime toLocalDateTime = dateUtil.getLocalDateTime(to, endTime);
                    Long toDate = dateUtil.getEpochFromLocalDateTime(toLocalDateTime);

                    criteria.setFromDate(fromDate);
                    criteria.setToDate(toDate);

                    List<Hearing> hearings = searchHearing(searchRequest);
                    hearingsToReschedule.addAll(hearings);

                    slotIds.remove(hearingSlot.getId());

                }
            }
        } else {
            hearingsToReschedule = searchHearing(searchRequest);

        }

        return hearingsToReschedule;


    }


}
