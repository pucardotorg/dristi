package org.pucar.dristi.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.config.ServiceConstants;
import org.pucar.dristi.repository.CaseRepository;
import org.pucar.dristi.service.NotificationService;
import org.pucar.dristi.util.RequestInfoGenerator;
import org.pucar.dristi.web.models.AdvocateMapping;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.CourtCase;
import org.pucar.dristi.web.models.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@Slf4j
@EnableScheduling
public class CronJobScheduler {

    private final CaseRepository caseRepository;
    private final RequestInfoGenerator requestInfoGenerator;
    private final NotificationService notificationService;
    private final Configuration config;
    private final ExecutorService executorService;

    @Autowired
    public CronJobScheduler(CaseRepository caseRepository, RequestInfoGenerator requestInfoGenerator,
                            NotificationService notificationService, Configuration config) {
        this.caseRepository = caseRepository;
        this.requestInfoGenerator = requestInfoGenerator;
        this.notificationService = notificationService;
        this.config = config;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Async
    @Scheduled(cron = "${config.case.esign.pending}", zone = "Asia/Kolkata")
    public void sendNotificationToESignPending() {
        if (config.getIsSMSEnabled()) {
            log.info("Starting Cron Job For Sending Notification To Litigant ESign Pending");
            processNotifications("DRAFT_IN_PROGRESS", ServiceConstants.ESIGN_PENDING);

            log.info("Starting Cron Job For Sending Notification To Advocate ESign Pending");
            processNotifications("DRAFT_IN_PROGRESS", ServiceConstants.ADVOCATE_ESIGN_PENDING);
        }
    }

    @Async
    @Scheduled(cron = "${config.application.payment.pending}", zone = "Asia/Kolkata")
    public void sendNotificationToPaymentPending() {
        if (config.getIsSMSEnabled()) {
            log.info("Starting Cron Job For Sending Notification To Payment Pending");
            processNotifications("PAYMENT_PENDING", ServiceConstants.PAYMENT_PENDING);
        }
    }

    /**
     * Common logic to process notifications based on the case status.
     *
     * @param status            The status of the cases to process.
     * @param notificationType  Notification type for the main case.
     */
    private void processNotifications(String status, String notificationType) {
        RequestInfo requestInfo = requestInfoGenerator.generateSystemRequestInfo();
        List<Future<Boolean>> futures = new ArrayList<>();

        try {
            int offset = 0;
            int limit = 100;
            List<CourtCase> courtCases;

            do {
                courtCases = fetchCases(status, offset, limit, requestInfo);
                log.info("Fetched {} cases for processing with offset {}", courtCases.size(), offset);

                for (CourtCase courtCase : courtCases) {
                    Future<Boolean> future = executorService.submit(() -> processCase(courtCase, requestInfo, notificationType));
                    futures.add(future);
                }

                // Increase the offset for the next batch
                offset += limit;

            } while (courtCases.size() == limit);

            // Wait for all tasks to complete
            handleFutureResults(futures);

            log.info("Completed Cron Job For Sending Notifications");

        } catch (Exception e) {
            log.error("Error occurred during notification processing", e);
        }
    }

    /**
     * Fetches cases based on the given criteria.
     */
    private List<CourtCase> fetchCases(String status, int offset, int limit, RequestInfo requestInfo) {
        Pagination pagination = Pagination.builder().limit( limit).offSet( offset).build();
        CaseCriteria criteria = CaseCriteria.builder()
                .status(Collections.singletonList(status))
                .filingToDate(LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())
                .filingFromDate(LocalDate.now().minusDays(Integer.parseInt(config.getUserNotificationPeriod()))
                        .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())
                .pagination(pagination)
                .build();

        List<CaseCriteria> criteriaList = caseRepository.getCases(Collections.singletonList(criteria), requestInfo);
        return criteriaList.get(0).getResponseList();
    }

    /**
     * Processes a single court case and sends notifications.
     */
    private Boolean processCase(CourtCase courtCase, RequestInfo requestInfo, String notificationType) {
        try {
            if (ServiceConstants.ADVOCATE_ESIGN_PENDING.equalsIgnoreCase(notificationType)
                    && !CollectionUtils.isEmpty(courtCase.getRepresentatives())) {
                for (AdvocateMapping mapping : courtCase.getRepresentatives()) {
                    notificationService.sendNotification(requestInfo, courtCase, notificationType, mapping.getAuditDetails().getCreatedBy());
                }
            } else {
                notificationService.sendNotification(requestInfo, courtCase, notificationType, courtCase.getAuditdetails().getCreatedBy());
            }
            return true;
        } catch (Exception e) {
            log.error("Error processing case: {}", courtCase.getId(), e);
            return false;
        }
    }

    /**
     * Handles future results and logs warnings for failed tasks.
     */
    private void handleFutureResults(List<Future<Boolean>> futures) {
        for (Future<Boolean> future : futures) {
            try {
                if (!future.get()) {
                    log.warn("Failed to send notifications in some cases");
                }
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error waiting for task completion", e);
            }
        }
    }
}