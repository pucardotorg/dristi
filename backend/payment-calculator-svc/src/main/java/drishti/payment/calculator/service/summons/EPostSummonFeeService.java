package drishti.payment.calculator.service.summons;

import drishti.payment.calculator.config.Configuration;
import drishti.payment.calculator.repository.PostalHubRepository;
import drishti.payment.calculator.service.SummonPayment;
import drishti.payment.calculator.util.SpeedPostUtil;
import drishti.payment.calculator.util.SummonUtil;
import drishti.payment.calculator.web.models.*;
import drishti.payment.calculator.web.models.enums.Classification;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static drishti.payment.calculator.config.ServiceConstants.POSTAL_HUB_NOT_FOUND;
import static drishti.payment.calculator.config.ServiceConstants.POSTAL_HUB_NOT_FOUND_MSG;

@Service
public class EPostSummonFeeService implements SummonPayment {

    private final SummonUtil summonUtil;
    private final PostalHubRepository repository;
    private final SpeedPostUtil speedPostUtil;
    private final Configuration config;

    @Autowired
    public EPostSummonFeeService(SummonUtil summonUtil, PostalHubRepository repository, SpeedPostUtil speedPostUtil, Configuration config) {
        this.summonUtil = summonUtil;
        this.repository = repository;
        this.speedPostUtil = speedPostUtil;
        this.config = config;
    }

    @Override
    public Calculation calculatePayment(RequestInfo requestInfo, SummonCalculationCriteria criteria) {


        EPostConfigParams ePostConfigParams = summonUtil.getIPostFeesDefaultData(requestInfo, criteria.getTenantId());

        HubSearchCriteria searchCriteria = HubSearchCriteria.builder().pincode(Collections.singletonList(criteria.getReceiverPincode())).build();
        List<PostalHub> postalHub = repository.getPostalHub(searchCriteria);
        if (postalHub.isEmpty()) {
            throw new CustomException(POSTAL_HUB_NOT_FOUND, POSTAL_HUB_NOT_FOUND_MSG);
        }

        Classification classification = postalHub.get(0).getClassification();
        Double ePostFeeWithoutGST = calculateEPostFee(config.getNumberOfPgOfSummon(), classification, ePostConfigParams);

        Double courtFees = summonUtil.calculateCourtFees(ePostConfigParams);
        Double envelopeFee = ePostConfigParams.getEnvelopeChargeIncludingGst();
        Double gstPercentage = ePostConfigParams.getGstPercentage();
        Double gstFee = ePostFeeWithoutGST * gstPercentage;

        List<BreakDown> breakDowns = summonUtil.getFeeBreakdown(courtFees, gstFee, ePostFeeWithoutGST + envelopeFee);

        double totalAmount = ePostFeeWithoutGST + (gstPercentage * ePostFeeWithoutGST) + courtFees + envelopeFee;

        return Calculation.builder()
                .applicationId(criteria.getSummonId())
                .totalAmount(Math.round(totalAmount * 100.0) / 100.0)
                .tenantId(criteria.getTenantId())
                .breakDown(breakDowns)
                .build();
    }

    public Double calculateEPostFee(Integer numberOfPages, Classification classification, EPostConfigParams configParams) {

        Double weightPerPage = configParams.getPageWeight();
        Double printingFeePerPage = configParams.getPrintingFeePerPage();
        Double businessFee = configParams.getBusinessFee();

        SpeedPost speedPost = configParams.getSpeedPost();
        // Total Weight in grams
        Double totalWeight = numberOfPages * weightPerPage;
        // Total Printing Fee
        Double totalPrintingFee = numberOfPages * printingFeePerPage;
        // Speed Post Fee
        Double speedPostFee = speedPostUtil.getSpeedPostFee(totalWeight, classification, speedPost);
        // Total Fee before GST
        return totalPrintingFee + speedPostFee + businessFee;
    }




}
