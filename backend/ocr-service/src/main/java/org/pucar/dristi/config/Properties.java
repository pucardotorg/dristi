package org.pucar.dristi.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class Properties {

    @Value("${egov.ocr.host}")
    private String ocrHost;

    @Value("${egov.ocr.endpoint}")
    private String ocrEndPoint;

    @Value("${egov.mdms.host}")
    private String mdmsHost;

    @Value("${egov.mdms.search.endpoint}")
    private String mdmsSearchEndpoint;

    @Value("${egov.file.store.host}")
    private String fileStoreHost;

    @Value("${egov.file.store.endpoint}")
    private String fileStoreEndpoint;

    @Value("${egov.ocr.topic}")
    private String ocrTopic;

    @Value("${state.level.tenant.id}")
    private String stateLevelTenantId;
}
