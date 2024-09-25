package drishti.payment.calculator.service.channels;

import drishti.payment.calculator.config.Configuration;
import drishti.payment.calculator.repository.PostalHubRepository;
import drishti.payment.calculator.service.SummonPayment;
import drishti.payment.calculator.util.SpeedPostUtil;
import drishti.payment.calculator.util.TaskUtil;
import drishti.payment.calculator.web.models.*;
import drishti.payment.calculator.web.models.enums.Classification;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static drishti.payment.calculator.config.ServiceConstants.POSTAL_HUB_NOT_FOUND;
import static drishti.payment.calculator.config.ServiceConstants.POSTAL_HUB_NOT_FOUND_MSG;

@Service
public class EPostFeeService implements SummonPayment {

    private final TaskUtil taskUtil;
    private final PostalHubRepository repository;
    private final SpeedPostUtil speedPostUtil;
    private final Configuration config;

    @Autowired
    public EPostFeeService(TaskUtil taskUtil, PostalHubRepository repository, SpeedPostUtil speedPostUtil, Configuration config) {
        this.taskUtil = taskUtil;
        this.repository = repository;
        this.speedPostUtil = speedPostUtil;
        this.config = config;
    }

    @Deprecated
    @Override
    public Calculation calculatePayment(RequestInfo requestInfo, SummonCalculationCriteria criteria) {

        SpeedPostConfigParams ePostConfigParams = taskUtil.getIPostFeesDefaultData(requestInfo, criteria.getTenantId());

        HubSearchCriteria searchCriteria = HubSearchCriteria.builder().pincode(Collections.singletonList(criteria.getReceiverPincode())).build();
        List<PostalHub> postalHub = repository.getPostalHub(searchCriteria);
        if (postalHub.isEmpty()) {
            throw new CustomException(POSTAL_HUB_NOT_FOUND, POSTAL_HUB_NOT_FOUND_MSG);
        }

        Classification classification = postalHub.get(0).getClassification();
        Double ePostFeeWithoutGST = speedPostUtil.calculateEPostFee(config.getNumberOfPgOfSummon(), classification, ePostConfigParams);

        Double courtFees = taskUtil.calculateCourtFees(ePostConfigParams);
        Double envelopeFee = ePostConfigParams.getEnvelopeChargeIncludingGst();
        Double gstPercentage = ePostConfigParams.getGstPercentage();
        Double gstFee = ePostFeeWithoutGST * gstPercentage;

        List<BreakDown> breakDowns = taskUtil.getFeeBreakdown(courtFees, gstFee, ePostFeeWithoutGST + envelopeFee);

        double totalAmount = ePostFeeWithoutGST + (gstPercentage * ePostFeeWithoutGST) + courtFees + envelopeFee;

        return Calculation.builder()
                .applicationId(criteria.getSummonId())
                .totalAmount(Math.round(totalAmount * 100.0) / 100.0)
                .tenantId(criteria.getTenantId())
                .breakDown(breakDowns)
                .build();
    }

    @Override
    public Calculation calculatePayment(RequestInfo requestInfo, TaskPaymentCriteria criteria) {


        SpeedPostConfigParams speedPostConfigParams = taskUtil.getIPostFeesDefaultData(requestInfo, criteria.getTenantId());

        HubSearchCriteria searchCriteria = HubSearchCriteria.builder().pincode(Collections.singletonList(criteria.getReceiverPincode())).build();
        List<PostalHub> postalHub = repository.getPostalHub(searchCriteria);
        if (postalHub.isEmpty()) {
            throw new CustomException(POSTAL_HUB_NOT_FOUND, POSTAL_HUB_NOT_FOUND_MSG);
        }
        Classification classification = postalHub.get(0).getClassification();

        String taskType = criteria.getTaskType();
        String tenantId = criteria.getTenantId();
        List<TaskPayment> taskPaymentMasterData = taskUtil.getTaskPaymentMasterData(requestInfo, tenantId);
        List<TaskPayment> filteredTaskPayment = taskPaymentMasterData.stream()
                .filter(element -> taskType.equals(element.getType()))
                .toList();

        Double courtFees = taskUtil.calculateCourtFees(filteredTaskPayment.get(0));
        Double postFee = speedPostUtil.calculateEPostFee(config.getNumberOfPgOfSummon(), classification, speedPostConfigParams);

        List<BreakDown> breakDowns = taskUtil.getFeeBreakdown(courtFees, postFee);

        double totalAmount = courtFees + postFee;

        return Calculation.builder()
                .applicationId(criteria.getId())
                .totalAmount(Math.round(totalAmount * 100.0) / 100.0)
                .tenantId(criteria.getTenantId())
                .breakDown(breakDowns)
                .build();


    }


}
