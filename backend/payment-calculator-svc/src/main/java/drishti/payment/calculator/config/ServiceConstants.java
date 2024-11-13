package drishti.payment.calculator.config;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ServiceConstants {

    public static final String EXTERNAL_SERVICE_EXCEPTION = "External Service threw an Exception: ";
    public static final String SEARCHER_SERVICE_EXCEPTION = "Exception while fetching from searcher: ";

    public static final String ERROR_WHILE_FETCHING_FROM_MDMS = "Exception occurred while fetching category lists from mdms: ";

    public static final String RES_MSG_ID = "uief87324";
    public static final String SUCCESSFUL = "successful";
    public static final String FAILED = "failed";

    public static final String SUMMON_MODULE = "summons";
    public static final String I_POST_MASTER = "e-post";

    public static final String CASE_MODULE = "case";
    public static final String E_FILLING_MASTER = "e-filling";

    public static final String VAKALATHNAMA_FEE = "Vakalathnama Fee";

    public static final String ADVOCATE_WELFARE_FUND = "Advocate Welfare Fund";

    public static final String ADVOCATE_CLERK_WELFARE_FUND = "Advocate Clerk Welfare Fund";
    public static final String TOTAL_APPLICATION_FEE = "Total Application Fee";
    public static final String PETITION_FEE = "Petition Fee";
    public static final String DELAY_CONDONATION_FEE = "Delay Condonation Application Fee";

    public static final String COURT_FEE = "Court Fee";
    public static final String POSTAL_HUB_NOT_FOUND = "POSTAL_HUB_NOT_FOUND";
    public static final String POSTAL_HUB_NOT_FOUND_MSG = "Pincode not found for speed post fee calculation";

    public static final String GST = "Gst";
    public static final String E_POST = "E Post";

    public static final String DK_PC_ID_ERR = "DK_PC_ID_ERR";

    public static final String DK_PC_ID_ERR_MSG = "id is mandatory for updating postal hub";

    public static final String HUB_ALREADY_EXIST = "HUB_ALREADY_EXIST";
    public static final String HUB_ALREADY_EXIST_MSG = "Hub already exist in DB";

    public static final String CALCULATE_PAYMENT_EXCEPTION = "CALCULATE_PAYMENT_EXCEPTION";
    public static final String ILLEGAL_STATE_EXCEPTION = "ILLEGAL_STATE_EXCEPTION";


}
