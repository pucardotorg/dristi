package org.egov.sbi.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    //Sbi
    @Value("${sbi-secret-key}")
    private String sbiSecretKey;

    @Value("${sbi-merchant-id}")
    private String sbiMerchantId;

    @Value("${sbi-transaction-url}")
    private String sbiTransactionUrl;

    @Value("${sbi-double-verification-url}")
    private String sbiDoubleVerificationUrl;

    @Value("${sbi-transaction-success-url}")
    private String sbiTransactionSuccessUrl;

    @Value("${sbi-transaction-fail-url}")
    private String sbiTransactionFailUrl;

    @Value(("${sbi-aggregator-id}"))
    private String sbiAggregatorId;

    // Collection Service
    @Value("${egov.collectionservice.host}")
    private String collectionServiceHost;
    
    @Value("${egov.collectionservice.payment.create}")
    private String collectionsPaymentCreatePath;

    //IdGen Service
    @Value("${egov.idgen.host}")
    private String idGenHost;

    @Value("${egov.idgen.path}")
    private String idGenPath;

    @Value("${egov.idgen.name}")
    private String idName;

    @Value(("${kafka.topic.insert.sbi.transaction.details}"))
    private String createTransactionDetails;

    @Value("${kafka.topic.update.sbi.transaction.details}")
    private String updateTransactionDetails;
}
