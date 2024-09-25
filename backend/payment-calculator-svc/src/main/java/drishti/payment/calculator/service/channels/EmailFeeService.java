package drishti.payment.calculator.service.channels;


import drishti.payment.calculator.service.SummonPayment;
import drishti.payment.calculator.util.TaskUtil;
import drishti.payment.calculator.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static drishti.payment.calculator.config.ServiceConstants.COURT_FEE;

@Service
@Slf4j
public class EmailFeeService implements SummonPayment {

    private final TaskUtil taskUtil;

    @Autowired
    public EmailFeeService(TaskUtil taskUtil) {
        this.taskUtil = taskUtil;
    }

    @Deprecated
    @Override
    public Calculation calculatePayment(RequestInfo requestInfo, SummonCalculationCriteria criteria) {

        SpeedPostConfigParams speedPostConfigParams = taskUtil.getIPostFeesDefaultData(requestInfo, criteria.getTenantId());
        Double courtFee = taskUtil.calculateCourtFees(speedPostConfigParams);
        return Calculation.builder()
                .applicationId(criteria.getSummonId())
                .tenantId(criteria.getTenantId())
                .totalAmount(courtFee)
                .breakDown(Collections.singletonList(new BreakDown(COURT_FEE, courtFee, new HashMap<>()))).build();
    }

    @Override
    public Calculation calculatePayment(RequestInfo requestInfo, TaskPaymentCriteria criteria) {

        String taskType = criteria.getTaskType();
        String tenantId = criteria.getTenantId();
        List<TaskPayment> taskPaymentMasterData = taskUtil.getTaskPaymentMasterData(requestInfo, tenantId);
        List<TaskPayment> filteredTaskPayment = taskPaymentMasterData.stream()
                .filter(element -> taskType.equals(element.getType()))
                .toList();

        //TODO:validation on allowed methods

        Double courtFees = taskUtil.calculateCourtFees(filteredTaskPayment.get(0));

        return Calculation.builder()
                .applicationId(criteria.getId())
                .tenantId(criteria.getTenantId())
                .totalAmount(courtFees)
                .breakDown(Collections.singletonList(new BreakDown(COURT_FEE, courtFees, new HashMap<>()))).build();
    }
}
