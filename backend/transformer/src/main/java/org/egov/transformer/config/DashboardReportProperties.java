package org.egov.transformer.config;

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
public class DashboardReportProperties {

    @Value("${elasticsearch.server.host}")
    private String elasticsearchServerHost;

    @Value("${elasticsearch.server.port}")
    private String elasticsearchServerPort;

    @Value("${elasticsearch.connection.type}")
    private String elasticsearchConnectionType;

    @Value("${elasticsearch.user.name}")
    private String elasticsearchUserName;

    @Value("${elasticsearch.user.password}")
    private String elasticsearchUserPassword;

}
