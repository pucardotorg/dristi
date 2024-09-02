package com.pucar.drishti.config;

import lombok.*;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@Data
@Import({TracerConfiguration.class})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Configuration {

    //filestore
    @Value("${egov.filestore.host}")
    private String filestoreHost;

    @Value("${egov.filestore.search.endpoint}")
    private String filestoreEndPoint;

    //e-sign
    @Value("${drishti.esign.host}")
    private String eSignHost;

    @Value("${drishti.esign.endpoint}")
    private String eSignEndPoint;

    @Value("${drishti.esign.redirect.url}")
    private String redirectUrl;

    // oath-token
    @Value("${drishti.oath.host}")
    private String oathHost;

    @Value("${drishti.oath.endpoint}")
    private String oathEndPoint;

    @Value("${dristhi.oath.username}")
    private String userName;

    @Value("${dristhi.oath.password}")
    private String password;

    @Value("${dristhi.oath.tenantId}")
    private String tenantId;

    @Value("${dristhi.oath.usertype}")
    private String userType;

    @Value("${dristhi.oath.scope}")
    private String scope;

    @Value("${dristhi.oath.grantType}")
    private String grantType;
}
