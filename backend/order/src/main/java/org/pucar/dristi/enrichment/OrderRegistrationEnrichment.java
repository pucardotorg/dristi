package org.pucar.dristi.enrichment;


import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.web.models.OrderRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.pucar.dristi.config.ServiceConstants.ENRICHMENT_EXCEPTION;

@Component
@Slf4j
public class OrderRegistrationEnrichment {

    private IdgenUtil idgenUtil;
    private Configuration configuration;

    public OrderRegistrationEnrichment(IdgenUtil idgenUtil, Configuration configuration) {
        this.idgenUtil = idgenUtil;
        this.configuration = configuration;
    }

    public void enrichOrderRegistration(OrderRequest orderRequest) {
        try {
            if (orderRequest.getRequestInfo().getUserInfo() != null) {
                String tenantId = orderRequest.getOrder().getFilingNumber().replace("-", "");
                String idName = configuration.getOrderConfig();
                String idFormat = configuration.getOrderFormat();

                List<String> orderRegistrationIdList = idgenUtil.getIdList(orderRequest.getRequestInfo(), tenantId, idName, idFormat, 1, false);
                AuditDetails auditDetails = AuditDetails.builder().createdBy(orderRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(orderRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
                orderRequest.getOrder().setAuditDetails(auditDetails);

                orderRequest.getOrder().setId(UUID.randomUUID());

                orderRequest.getOrder().getStatuteSection().setId(UUID.randomUUID());
                orderRequest.getOrder().getStatuteSection().setAuditdetails(auditDetails);

                if (orderRequest.getOrder().getDocuments() != null) {
                    orderRequest.getOrder().getDocuments().forEach(document -> {
                        document.setId(String.valueOf(UUID.randomUUID()));
                        document.setDocumentUid(document.getId());
                    });
                }

                String orderNumber = orderRequest.getOrder().getFilingNumber() + "-" + orderRegistrationIdList.get(0);
                orderRequest.getOrder().setOrderNumber(orderNumber);
            }

        } catch (CustomException e) {
            log.error("Custom Exception occurred while enriching order :: {}", e.toString());
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred while enriching order :: {}", e.toString());
            throw e;
        }
    }

    public void enrichOrderRegistrationUponUpdate(OrderRequest orderRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in case of update
            orderRequest.getOrder().getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
            orderRequest.getOrder().getAuditDetails().setLastModifiedBy(orderRequest.getRequestInfo().getUserInfo().getUuid());

            if (orderRequest.getOrder().getDocuments() != null) {
                orderRequest.getOrder().getDocuments().forEach(document -> {
                    if (document.getId() == null)
                        document.setId(String.valueOf(UUID.randomUUID()));
                });
            }
        } catch (Exception e) {
            log.error("Error enriching advocate application upon update :: {}", e.toString());
            throw new CustomException(ENRICHMENT_EXCEPTION, "Error in order enrichment service during order update process: " + e.getMessage());
        }
    }
}