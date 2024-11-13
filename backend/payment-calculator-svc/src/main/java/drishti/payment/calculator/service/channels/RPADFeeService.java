package drishti.payment.calculator.service.channels;

import drishti.payment.calculator.service.Payment;
import drishti.payment.calculator.util.TaskUtil;
import drishti.payment.calculator.web.models.*;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static drishti.payment.calculator.config.ServiceConstants.*;

@Service
@Slf4j
public class RPADFeeService implements Payment {

    private final TaskUtil taskUtil;

    @Autowired
    public RPADFeeService(TaskUtil taskUtil) {
        this.taskUtil = taskUtil;
    }

    @Deprecated
    @Override
    public Calculation calculatePayment(RequestInfo requestInfo, SummonCalculationCriteria criteria) {
        return null;
    }

    @Override
    public Calculation calculatePayment(RequestInfo requestInfo, TaskPaymentCriteria criteria) {

        try {
            String taskType = criteria.getTaskType();
            String tenantId = criteria.getTenantId();
            log.info("Calculating payment for task type: {} in tenant: {}", taskType, tenantId);

            List<TaskPayment> taskPaymentMasterData = taskUtil.getTaskPaymentMasterData(requestInfo, tenantId);
            List<TaskPayment> filteredTaskPayment = taskPaymentMasterData.stream()
                    .filter(element -> taskType.equals(element.getType()))
                    .toList();

            if (filteredTaskPayment.isEmpty()) {
                throw new CustomException(ILLEGAL_STATE_EXCEPTION, "No matching task payment found for task type: " + taskType);
            }

            Double courtFees = taskUtil.calculateCourtFees(filteredTaskPayment.get(0));
            log.debug("Calculated court fees: {}", courtFees);

            return Calculation.builder()
                    .applicationId(criteria.getId())
                    .tenantId(criteria.getTenantId())
                    .totalAmount(courtFees)
                    .breakDown(Collections.singletonList(new BreakDown(COURT_FEE, courtFees, new HashMap<>()))).build();
        } catch (Exception e) {
            log.error("Error calculating payment for task type: {} in tenant: {}", criteria.getTaskType(), criteria.getTenantId(), e);
            throw new CustomException(CALCULATE_PAYMENT_EXCEPTION, e.getMessage());
        }
    }
}


