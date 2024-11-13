package org.drishti.esign.config;

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

    //MDMS
    @Value("${egov.mdms.host}")
    private String mdmsHost;

    @Value("${egov.mdms.search.endpoint}")
    private String mdmsEndPoint;

    //filestore
    @Value("${egov.filestore.host}")
    private String filestoreHost;

    @Value("${egov.filestore.search.endpoint}")
    private String filestoreSearchEndPoint;

    @Value("${egov.filestore.create.endpoint}")
    private String filestoreCreateEndPoint;


    //ESign
    @Value("${esing.ver}")
    private String version;

    @Value("${esing.sc}")
    private String consent;

    @Value("${esing.asp.id}")
    private String aspId;

    @Value("${esing.auth.mode}")
    private String authMode;

    @Value("${esing.response.sig.type}")
    private String responseSigType;

    @Value("${esing.response.url}")
    private String responseUrl;

    @Value("${esing.id}")
    private String id;

    @Value("${esing.hash.algorithm}")
    private String hashAlgorithm;

    @Value("${esing.doc.info}")
    private String docInfo;

    @Value("${esing.ekyc.id.type}")
    private String ekycIdType;

}
