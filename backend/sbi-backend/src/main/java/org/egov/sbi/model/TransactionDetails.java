package org.egov.sbi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.models.AuditDetails;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetails {

    @JsonProperty("MerchantId")
    private String merchantId;

    @JsonProperty("OperatingMode")
    private String operatingMode;

    @JsonProperty("MerchantCountry")
    private String merchantCountry;

    @JsonProperty("MerchantCurrency")
    private String merchantCurrency;

    @JsonProperty("amountDetails")
    private List<AmountDetails> amountDetails;

    @JsonProperty("PostingAmount")
    private double postingAmount;

    @JsonProperty("OtherDetails")
    private String otherDetails;

    @JsonProperty("SuccessURL")
    private String successUrl;

    @JsonProperty("FailURL")
    private String failUrl;

    @JsonProperty("AggregatorId")
    private String aggregatorId;

    @JsonProperty("MerchantOrderNumber")
    private String merchantOrderNumber;

    @JsonProperty("MerchantCustomerId")
    private String merchantCustomerId;

    @JsonProperty("PayMode")
    private String payMode;

    @JsonProperty("AccessMedium")
    private String accessMedium;

    @JsonProperty("TransactionSource")
    private String transactionSource;

    @JsonProperty("AuditDetails")
    private AuditDetails auditDetails;

    @JsonProperty("SBIePayRefId")
    private String sbiEpayRefId;

    @JsonProperty("TransactionStatus")
    private String transactionStatus;

    @JsonProperty("Reason")
    private String reason;

    @JsonProperty("BankCode")
    private String bankCode;

    @JsonProperty("BankReferenceNumber")
    private String bankReferenceNumber;

    @JsonProperty("TransactionDate")
    private String transactionDate;

    @JsonProperty("CIN")
    private String cin;

    @JsonProperty("TotalFeeGST")
    private double totalFeeGst;

    @JsonProperty("rowNumber")
    private Integer rowNumber;

    @JsonProperty("Ref1")
    private String ref1;

    @JsonProperty("Ref2")
    private String ref2;

    @JsonProperty("Ref3")
    private String ref3;

    @JsonProperty("Ref4")
    private String ref4;

    @JsonProperty("Ref5")
    private String ref5;

    @JsonProperty("Ref6")
    private String ref6;

    @JsonProperty("Ref7")
    private String ref7;

    @JsonProperty("Ref8")
    private String ref8;

    @JsonProperty("Ref9")
    private String ref9;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("billId")
    private String billId;

    @JsonProperty("totalDue")
    private double totalDue;

    @JsonProperty("businessService")
    private String businessService;

    @JsonProperty("serviceNumber")
    private String serviceNumber;

    @JsonProperty("payerName")
    private String payerName;

    @JsonProperty("paidBy")
    private String paidBy;

    @JsonProperty("mobileNumber")
    private String mobileNumber;

    public String toSingleRequestString() {
        return merchantId + "|" +
                operatingMode + "|" +
                merchantCountry + "|" +
                merchantCurrency + "|" +
                postingAmount + "|" +
                otherDetails + "|" +
                successUrl + "|" +
                failUrl + "|" +
                aggregatorId + "|" +
                merchantOrderNumber + "|" +
                merchantCustomerId + "|" +
                payMode + "|" +
                accessMedium + "|" +
                transactionSource;
    }

    public String toMultiAccountPaymentString() {
        if (amountDetails == null || amountDetails.isEmpty()) {
            return "";
        }
        return amountDetails.stream()
                .map(detail -> String.format("%s|%s|%s",
                        detail.getPostingAmount(),
                        detail.getMerchantCurrency(),
                        detail.getAccountIdentifier()))
                .collect(Collectors.joining("||"));
    }
}
