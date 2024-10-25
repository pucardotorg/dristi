package org.egov.sbi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrowserDetails {

    @JsonProperty("MerchantOrderNumber")
    private String merchantOrderNumber;

    @JsonProperty("SBIePayRefId")
    private String sbiEpayRefId;

    @JsonProperty("TransactionStatus")
    private String transactionStatus;

    @JsonProperty("Amount")
    private double amount;

    @JsonProperty("Currency")
    private String currency;

    @JsonProperty("PayMode")
    private String payMode;

    @JsonProperty("OtherDetails")
    private String otherDetails;

    @JsonProperty("Reason")
    private String reason;

    @JsonProperty("BankCode")
    private String bankCode;

    @JsonProperty("BankReferenceNumber")
    private String bankReferenceNumber;

    @JsonProperty("TransactionDate")
    private String transactionDate;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("CIN")
    private String cin;

    @JsonProperty("MerchantID")
    private String merchantId;

    @JsonProperty("TotalFeeGST")
    private double totalFeeGst;

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

    public static BrowserDetails fromString(String str) {
        String[] parts = str.split("\\|", -1);
        BrowserDetails browserDetails = new BrowserDetails();

        browserDetails.setMerchantOrderNumber(parts[0]);
        browserDetails.setSbiEpayRefId(parts[1]);
        browserDetails.setTransactionStatus(parts[2]);
        browserDetails.setAmount(Double.parseDouble(parts[3]));
        browserDetails.setCurrency(parts[4]);
        browserDetails.setPayMode(parts[5]);
        browserDetails.setOtherDetails(parts[6]);
        browserDetails.setReason(parts[7]);
        browserDetails.setBankCode(parts[8]);
        browserDetails.setBankReferenceNumber(parts[9]);
        browserDetails.setTransactionDate(parts[10]);
        browserDetails.setCountry(parts[11]);
        browserDetails.setCin(parts[12]);
        browserDetails.setMerchantId(parts[13]);
        //browserDetails.setTotalFeeGst(Double.parseDouble(parts[14]));
        browserDetails.setRef1(parts[15]);
        browserDetails.setRef2(parts[16]);
        browserDetails.setRef3(parts[17]);
        browserDetails.setRef4(parts[18]);
        browserDetails.setRef5(parts[19]);
        browserDetails.setRef6(parts[20]);
        browserDetails.setRef7(parts[21]);
        browserDetails.setRef8(parts[22]);
        browserDetails.setRef9(parts[23]);

        return browserDetails;
    }

    public static BrowserDetails doubleVerifyBrowserdetailsFromString(String browserDetailsString){
        String[] parts = browserDetailsString.split("\\|", -1);
        BrowserDetails browserDetails = new BrowserDetails();

        browserDetails.setMerchantId(parts[0]);
        browserDetails.setSbiEpayRefId(parts[1]);
        browserDetails.setTransactionStatus(parts[2]);
        browserDetails.setCountry(parts[3]);
        browserDetails.setCurrency(parts[4]);
        browserDetails.setOtherDetails(parts[5]);
        browserDetails.setMerchantOrderNumber(parts[6]);
        browserDetails.setAmount(Double.parseDouble(parts[7]));
        browserDetails.setBankCode(parts[9]);
        browserDetails.setBankReferenceNumber(parts[10]);
        browserDetails.setTransactionDate(parts[11]);
        browserDetails.setPayMode(parts[12]);
        browserDetails.setCin(parts[13]);
        browserDetails.setRef1(parts[15]);
        browserDetails.setRef2(parts[16]);
        browserDetails.setRef3(parts[17]);
        browserDetails.setRef4(parts[18]);
        browserDetails.setRef5(parts[19]);
        browserDetails.setRef6(parts[20]);
        browserDetails.setRef7(parts[21]);
        browserDetails.setRef8(parts[22]);
        browserDetails.setRef9(parts[23]);

        return browserDetails;
    }
}
