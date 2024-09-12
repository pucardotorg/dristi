package org.egov.sbi.enrichemnt;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.sbi.config.PaymentConfiguration;
import org.egov.sbi.model.BrowserDetails;
import org.egov.sbi.model.TransactionDetails;
import org.egov.sbi.model.TransactionRequest;
import org.egov.sbi.util.IdgenUtil;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import static org.egov.sbi.config.ServiceConstants.ENRICHMENT_EXCEPTION;

@Component
@Slf4j
public class PaymentEnrichment {

    private final IdgenUtil idgenUtil;

    private final PaymentConfiguration config;

    public PaymentEnrichment(IdgenUtil idgenUtil, PaymentConfiguration config) {
        this.idgenUtil = idgenUtil;
        this.config = config;
    }

    public void enrichTransaction(TransactionRequest request) {
        try {
            String merchantOrderNumber = idgenUtil.getIdList(request.getRequestInfo(), config.getEgovStateTenantId(), config.getIdName(), null, 1).get(0);
            request.getTransactionDetails().setMerchantOrderNumber(merchantOrderNumber);
            AuditDetails auditDetails = createAuditDetails(request.getRequestInfo());
            request.getTransactionDetails().setAuditDetails(auditDetails);
            request.getTransactionDetails().setSuccessUrl(config.getSbiTransactionSuccessUrl());
            request.getTransactionDetails().setFailUrl(config.getSbiTransactionFailUrl());
            request.getTransactionDetails().setMerchantId(config.getSbiMerchantId());
            request.getTransactionDetails().setAggregatorId("SBIEPAY");
            request.getTransactionDetails().setMerchantCustomerId("NA");
            request.getTransactionDetails().setAccessMedium("ONLINE");
            request.getTransactionDetails().setTransactionSource("ONLINE");
        } catch (Exception e) {
            log.error("Error enriching transaction request :: {}", e.toString());
            throw new CustomException(ENRICHMENT_EXCEPTION, e.getMessage());
        }
    }

    public void enrichTransactionResponse(TransactionDetails transactionDetails, BrowserDetails browserDetails, RequestInfo requestInfo) {


        Long currentTime = System.currentTimeMillis();
        transactionDetails.getAuditDetails().setLastModifiedTime(currentTime);
        transactionDetails.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUuid());

            transactionDetails.setSbiEpayRefId(browserDetails.getSbiEpayRefId());
            transactionDetails.setTransactionStatus(browserDetails.getTransactionStatus());
            transactionDetails.setPostingAmount(browserDetails.getAmount());
            transactionDetails.setMerchantCurrency(browserDetails.getCurrency());
            transactionDetails.setPayMode(browserDetails.getPayMode());
            transactionDetails.setOtherDetails(browserDetails.getOtherDetails());
            transactionDetails.setReason(browserDetails.getReason());
            transactionDetails.setBankCode(browserDetails.getBankCode());
            transactionDetails.setBankReferenceNumber(browserDetails.getBankReferenceNumber());
            transactionDetails.setTransactionDate(browserDetails.getTransactionDate());
            transactionDetails.setMerchantCountry(browserDetails.getCountry());
            transactionDetails.setCin(browserDetails.getCin());
            transactionDetails.setMerchantId(browserDetails.getMerchantId());
            transactionDetails.setTotalFeeGst(browserDetails.getTotalFeeGst());
            transactionDetails.setRef1(browserDetails.getRef1());
            transactionDetails.setRef2(browserDetails.getRef2());
            transactionDetails.setRef3(browserDetails.getRef3());
            transactionDetails.setRef4(browserDetails.getRef4());
            transactionDetails.setRef5(browserDetails.getRef5());
            transactionDetails.setRef6(browserDetails.getRef6());
            transactionDetails.setRef7(browserDetails.getRef7());
            transactionDetails.setRef8(browserDetails.getRef8());
            transactionDetails.setRef9(browserDetails.getRef9());
            transactionDetails.setRowNumber(transactionDetails.getRowNumber() + 1);
    }

    private AuditDetails createAuditDetails(RequestInfo requestInfo) {
        long currentTime = System.currentTimeMillis();
        String userId = requestInfo.getUserInfo().getUuid();
        return AuditDetails.builder()
                .createdBy(userId)
                .createdTime(currentTime)
                .lastModifiedBy(userId)
                .lastModifiedTime(currentTime)
                .build();
    }
}
