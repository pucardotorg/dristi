package digit.config;


import org.springframework.stereotype.Component;


@Component
public class ServiceConstants {

    public static final String EXTERNAL_SERVICE_EXCEPTION = "External Service threw an Exception: ";
    public static final String SEARCHER_SERVICE_EXCEPTION = "Exception while fetching from searcher: ";
    public static final String IDGEN_ERROR = "IDGEN ERROR";
    public static final String NO_IDS_FOUND_ERROR = "No ids returned from idgen Service";
    public static final String ERROR_WHILE_FETCHING_FROM_MDMS = "Exception occurred while fetching category lists from mdms: ";
    public static final String RES_MSG_ID = "uief87324";
    public static final String SUCCESSFUL = "successful";
    public static final String FAILED = "failed";
    public static final String PARSING_ERROR = "PARSING ERROR";
    public static final String FAILED_TO_PARSE_BUSINESS_SERVICE_SEARCH = "Failed to parse response of workflow business service search";
    public static final String BUSINESS_SERVICE_NOT_FOUND = "BUSINESSSERVICE_NOT_FOUND";
    public static final String THE_BUSINESS_SERVICE = "The businessService ";
    public static final String NOT_FOUND = " is not found";
    public static final String TENANTID = "?tenantId=";
    public static final String BUSINESS_SERVICES = "&businessServices=";

    public static final String OPT_OUT_DUE = "OPT_OUT_DUE";

    public static final String OPT_OUT_SELECTION_LIMIT = "OPT_OUT_SELECTION_LIMIT";


    public static final String INACTIVE = "INACTIVE";
    public static final String ACTIVE = "ACTIVE";
    public static final String BLOCKED = "BLOCKED";
    public static final String SCHEDULE = "SCHEDULED";
    public final String DEFAULT_JUDGE_CALENDAR_MODULE_NAME = "schedule-hearing";
    public final String DEFAULT_JUDGE_CALENDAR_MASTER_NAME = "COURT000334";
    public final String DEFAULT_COURT_MODULE_NAME = "court";
    public final String DEFAULT_SLOTTING_MASTER_NAME = "slots";
    public final String DEFAULT_HEARING_MASTER_NAME = "hearings";
    public final String SCHEDULER_CONFIG_MASTER_NAME = "config";
    public final String SCHEDULER_CONFIG_MODULE_NAME = "SCHEDULER-CONFIG";

    public static final String PENDING_TASK_ENTITY_TYPE ="order-managelifecycle";
    public static final String PENDING_TASK_NAME = "Create Order for rescheduling the hearing";
    public static final String PENDING_TASK_STATUS = "RESCHEDULE_HEARING";

}
