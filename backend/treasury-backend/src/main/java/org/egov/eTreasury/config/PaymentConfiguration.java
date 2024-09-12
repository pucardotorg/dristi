package org.egov.eTreasury.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PaymentConfiguration {

    //Tenant Id
    @Value("${egov-state-level-tenant-id}")
    private String egovStateTenantId;

    //ETreasury
    @Value("${treasury-public-key}")
    private String publicKey;

    @Value("${treasury-client-secret}")
    private String clientSecret;

    @Value("${treasury-client-id}")
    private String clientId;

    @Value("${service-dept-code}")
    private String serviceDeptCode;

    @Value("${office-code}")
    private String officeCode;

    @Value("${dept-reference-id}")
    private String deptReferenceId;

    @Value("${treasury-server-status-url}")
    private String serverStatusUrl;

    @Value("${treasury-auth-url}")
    private String authUrl;

    @Value("${treasury-challan-generate-url}")
    private String challanGenerateUrl;

    @Value("${treasury-double-verification-url}")
    private String doubleVerificationUrl;

    @Value("${treasury-print-slip-url}")
    private String printSlipUrl;

    @Value("${treasury-transaction-details-url}")
    private String transactionDetailsUrl;

    @Value("${treasury-refund-request-url}")
    private String refundRequestUrl;

    @Value("${treasury-refund-status-url}")
    private String refundStatusUrl;

    @Value("${egov.collectionservice.host}")
    private String collectionServiceHost;
    
    @Value("${egov.collectionservice.payment.create}")
    private String collectionsPaymentCreatePath;

    @Value("${egov.file.store.treasury.module}")
    private String treasuryFileStoreModule;

    @Value("${egov.file.store.host}")
    private String fileStoreHost;

    @Value("${egov.file.store.save.endpoint}")
    private String fileStoreEndPoint;

    @Value("${egov.idgen.host}")
    private String idGenHost;

    @Value("${egov.idgen.path}")
    private String idGenPath;

    @Value("${egov.idgen.name}")
    private String idName;

    @Value("${egov.pdf.service.host}")
    private String pdfServiceHost;

    @Value("${egov.pdf.service.create.endpoint}")
    private String pdfServiceEndpoint;

    @Value("${egov.pdf.template.key}")
    private String pdfTemplateKey;

    @Value("${kafka.topic.create.treasury.payment.data}")
    private String saveTreasuryPaymentData;

    @Value(("${isTest.enabled}"))
    private boolean isTest;
}
