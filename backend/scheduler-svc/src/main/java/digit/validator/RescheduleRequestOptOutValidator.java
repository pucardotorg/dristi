package digit.validator;


import digit.config.Configuration;
import digit.config.ServiceConstants;
import digit.repository.RescheduleRequestOptOutRepository;
import digit.service.ReScheduleHearingService;
import digit.util.MasterDataUtil;
import digit.web.models.*;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static digit.config.ServiceConstants.OPT_OUT_SELECTION_LIMIT;

@Component
public class RescheduleRequestOptOutValidator {


    private final RescheduleRequestOptOutRepository repository;
    private final ReScheduleHearingService reScheduleHearingService;
    private final Configuration config;


    private final MasterDataUtil mdmsUtil;
    private final ServiceConstants constants;


    @Autowired
    public RescheduleRequestOptOutValidator(RescheduleRequestOptOutRepository repository, ReScheduleHearingService reScheduleHearingService, Configuration config, MasterDataUtil mdmsUtil, ServiceConstants constants) {
        this.repository = repository;
        this.reScheduleHearingService = reScheduleHearingService;
        this.config = config;

        this.mdmsUtil = mdmsUtil;
        this.constants = constants;
    }


    public void validateRequest(OptOutRequest request) {

        OptOut optOut = request.getOptOut();

        ReScheduleHearingReqSearchRequest searchRequest = ReScheduleHearingReqSearchRequest.builder()
                .requestInfo(request.getRequestInfo())
                .criteria(ReScheduleHearingReqSearchCriteria.builder().rescheduledRequestId(List.of(optOut.getRescheduleRequestId())).build())
                .build();
        List<ReScheduleHearing> reScheduleHearings = reScheduleHearingService.search(searchRequest, null, null);

        if (reScheduleHearings.isEmpty()) {
            throw new CustomException("DK_OO_INVALID_APPLICATION_ID", "No request with application id:" + optOut.getRescheduleRequestId() + " system");
        }
        ReScheduleHearing rescheduleReq = reScheduleHearings.get(0);


        if (ServiceConstants.INACTIVE.equals(rescheduleReq.getStatus())) {
            throw new CustomException("DK_OO_REQUEST_COMPLETED", "Opt out is no longer supported for request");
        }


        Set<String> litigants = rescheduleReq.getLitigants();
        Set<String> representatives = rescheduleReq.getRepresentatives();

        // if (!(representatives.contains(optOut.getIndividualId()) || litigants.contains(optOut.getIndividualId()))) {
        //     throw new CustomException("DK_OO_INVALID_REQUESTER_ID", "User is not authorised for opt out");
        // }

        List<SchedulerConfig> dataFromMDMS = mdmsUtil.getDataFromMDMS(SchedulerConfig.class, constants.SCHEDULER_CONFIG_MASTER_NAME, constants.SCHEDULER_CONFIG_MODULE_NAME);

        List<SchedulerConfig> filteredApplications = dataFromMDMS.stream()
                .filter(application -> application.getIdentifier().equals(OPT_OUT_SELECTION_LIMIT))
                .toList();

        int unit = filteredApplications.get(0).getUnit();

        //number of opt out validation
        if (optOut.getOptoutDates().size() > unit) {
            throw new CustomException("DK_OO_SELECTION_LIMIT_ERR", "you are eligible to opt out " + unit + " only");
        }

        //already opt out check
        OptOutSearchCriteria optOutSearchCriteria = OptOutSearchCriteria.builder().individualId(optOut.getIndividualId()).rescheduleRequestId(optOut.getRescheduleRequestId()).build();
        List<OptOut> optOuts = repository.getOptOut(optOutSearchCriteria, null, null);
        if (!optOuts.isEmpty()) {
            throw new CustomException("DK_OO_APP_ERR", "Already Opted out the dates.");
        }


        Set<Long> optOutDates = new HashSet<>(optOut.getOptoutDates());//

        rescheduleReq.getSuggestedDates().forEach(optOutDates::remove);

        if (!optOutDates.isEmpty()) {
            throw new CustomException("DK_OO_APP_ERR", "opt out dates must be from suggested days");

        }

    }


}

