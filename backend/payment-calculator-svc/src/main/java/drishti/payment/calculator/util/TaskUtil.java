package drishti.payment.calculator.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import drishti.payment.calculator.web.models.BreakDown;
import drishti.payment.calculator.web.models.IssuedEntity;
import drishti.payment.calculator.web.models.SpeedPostConfigParams;
import drishti.payment.calculator.web.models.TaskPayment;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static drishti.payment.calculator.config.ServiceConstants.*;

@Component
public class TaskUtil {

    private final MdmsUtil mdmsUtil;

    private final ObjectMapper objectMapper;

    @Autowired
    public TaskUtil(MdmsUtil mdmsUtil, ObjectMapper objectMapper) {
        this.mdmsUtil = mdmsUtil;
        this.objectMapper = objectMapper;
    }

    public SpeedPostConfigParams getIPostFeesDefaultData(RequestInfo requestInfo, String tenantId) {

        Map<String, Map<String, JSONArray>> response = mdmsUtil.fetchMdmsData(requestInfo, tenantId, SUMMON_MODULE, Collections.singletonList(I_POST_MASTER));
        JSONArray array = response.get(SUMMON_MODULE).get(I_POST_MASTER);
        Object object = array.get(0);

        return objectMapper.convertValue(object, SpeedPostConfigParams.class);
        //todo :add other methods
    }

    public List<TaskPayment> getTaskPaymentMasterData(RequestInfo requestInfo, String tenantId) {

        Map<String, Map<String, JSONArray>> response = mdmsUtil.fetchMdmsData(requestInfo, tenantId, "task-payment", Collections.singletonList("taskPaymentDetails"));
        JSONArray array = response.get("task-payment").get("taskPaymentDetails");

        List<TaskPayment> taskPayments = new ArrayList<>();
        if (array != null) {
            for (Object o : array) {
                TaskPayment taskPayment = objectMapper.convertValue(o, TaskPayment.class);
                taskPayments.add(taskPayment);
            }
        }

        return taskPayments;
    }

    @Deprecated
    public Double calculateCourtFees(SpeedPostConfigParams ePostFeesDefaultData) {
        return ePostFeesDefaultData.getCourtFee() + ePostFeesDefaultData.getApplicationFee();
    }

    public Double calculateCourtFees(TaskPayment taskPayment) {
        double courtFee = taskPayment.getCourtfee();
        Boolean issuanceFeeEnable = taskPayment.getIssuanceFeeEnable();
        double issuanceFee = 0;
        if (issuanceFeeEnable) {
            issuanceFee = getIssuanceFee(taskPayment.getIssuedEntities());
        }
        return courtFee + issuanceFee;
    }

    private double getIssuanceFee(List<IssuedEntity> issuedEntities) {
        if (issuedEntities.isEmpty()) {
            return 0; // todo: throw exception or something else based on requirement
        }
        //todo: logic need to change acc to updated hld , this is for current requirement
        return issuedEntities.get(0).getIssuanceFee();

    }

    @Deprecated
    public List<BreakDown> getFeeBreakdown(double courtFee, double gst, double postFee) {
        List<BreakDown> feeBreakdowns = new ArrayList<>();

        feeBreakdowns.add(new BreakDown(COURT_FEE, courtFee, new HashMap<>()));
        feeBreakdowns.add(new BreakDown(GST, gst, new HashMap<>()));
        feeBreakdowns.add(new BreakDown(E_POST, postFee, new HashMap<>()));

        return feeBreakdowns;
    }

    public List<BreakDown> getFeeBreakdown(double courtFee,  double postFee) {
        List<BreakDown> feeBreakdowns = new ArrayList<>();
        feeBreakdowns.add(new BreakDown(COURT_FEE, courtFee, new HashMap<>()));
        feeBreakdowns.add(new BreakDown(E_POST, postFee, new HashMap<>()));
        return feeBreakdowns;
    }
}
