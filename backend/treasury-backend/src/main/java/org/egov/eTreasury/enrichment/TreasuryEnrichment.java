package org.egov.eTreasury.enrichment;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.eTreasury.config.PaymentConfiguration;
import org.egov.eTreasury.model.ChallanData;
import org.egov.eTreasury.model.ChallanDetails;
import org.egov.eTreasury.model.HeadDetails;
import org.egov.eTreasury.util.IdgenUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TreasuryEnrichment {

    private final PaymentConfiguration config;

    private final IdgenUtil idgenUtil;

    public TreasuryEnrichment(PaymentConfiguration config, IdgenUtil idgenUtil) {
        this.config = config;
        this.idgenUtil = idgenUtil;
    }

    public ChallanDetails generateChallanDetails(ChallanData challanData, RequestInfo requestInfo) {

        String departmentId = idgenUtil.getIdList(requestInfo,config.getEgovStateTenantId(),config.getIdName(),null,1).get(0);

        String challanAmount;
        if (config.isTest()) {
            challanAmount = String.valueOf(4);
        } else {
            challanAmount = String.valueOf(challanData.getTotalDue());
        }
        String noOfHeads = String.valueOf(1);
        List<HeadDetails> headDetailsList = new ArrayList<>();
        HeadDetails headDetails  = HeadDetails.builder()
                .amount(challanAmount)
                .headId(config.getHeadId())
                .build();
        headDetailsList.add(headDetails);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(formatter);

        return ChallanDetails.builder()
                .fromDate(formattedDate)
                .toDate(formattedDate)
                .paymentMode("E")
                .challanAmount(challanAmount)
                .noOfHeads(noOfHeads)
                .headsDet(headDetailsList)
                .departmentId(departmentId)
                .serviceDeptCode(config.getServiceDeptCode())
                .officeCode(config.getOfficeCode())
                .partyName(challanData.getPaidBy())
                .tsbReceipts("N")
                .build();
    }
}
