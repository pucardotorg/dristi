package com.egov.icops_integrationkerala.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Data
@Configuration
public class IcopsConfiguration {

    //Tenant Id
    @Value("${egov-state-level-tenant-id}")
    private String egovStateTenantId;

    //TCops Config data
    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    @Value("${grant.type}")
    private String grantType;

    @Value("${icops.url}")
    private String icopsUrl;

    @Value("${auth.endpoint}")
    private String authEndpoint;

    @Value("${process.request.endpoint}")
    private String processRequestEndPoint;

    @Value("${location.jurisdiction.endpoint}")
    private String locationBasedJurisdiction;

    // File Store Service
    @Value("${egov.file.store.host}")
    private String fileStoreHost;

    @Value("${egov.file.store.search.endpoint}")
    private String fileStoreSearchEndPoint;

    @Value("${egov.file.store.save.endpoint}")
    private String fileStoreSaveEndPoint;

    @Value("${egov.file.store.summons.module}")
    private String summonsFileStoreModule;

    //MDMS
    @Value("${egov.mdms.host}")
    private String mdmsHost;

    @Value("${egov.mdms.search.endpoint}")
    private String mdmsEndPoint;

    //Summons
    @Value("${egov.summons.host}")
    private String summonsHost;

    @Value("${egov.summons.update.endpoint}")
    private String summonsUpdateEndPoint;

    @Value("${egov.process.origin}")
    private String processOrigin;

    @Value("${egov.process.inv.agency}")
    private String processInvAgency;

    @Value("${egov.mdms.icops.business.service.name}")
    private String icopsBusinessServiceName;

    @Value("${egov.idgen.host}")
    private String idGenHost;

    @Value("${egov.idgen.path}")
    private String idGenPath;

    @Value("${egov.idgen.name}")
    private String idName;

    @Value("${egov.oauth.url}")
    private String url;

    @Value("${egov.user.username}")
    private String username;

    @Value("${egov.user.password}")
    private String password;

}
