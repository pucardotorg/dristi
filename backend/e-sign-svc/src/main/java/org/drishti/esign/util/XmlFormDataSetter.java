package org.drishti.esign.util;


import org.drishti.esign.config.Configuration;
import org.drishti.esign.web.models.ESignXmlData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

@Component
public class XmlFormDataSetter {

    private final Configuration configuration;

    @Autowired
    public XmlFormDataSetter(Configuration configuration) {
        this.configuration = configuration;
    }

    public ESignXmlData setFormXmlData(String fileHash, ESignXmlData xmlData) {
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

        xmlData.setVer(configuration.getVersion());
        xmlData.setSc(configuration.getConsent());
        xmlData.setTs(dateFormat.format(now));
        xmlData.setEkycId("");
        xmlData.setEkycIdType(configuration.getEkycIdType());
        xmlData.setAspId(configuration.getAspId());
        xmlData.setAuthMode(configuration.getAuthMode());
        xmlData.setResponseSigType(configuration.getResponseSigType());
        xmlData.setResponseUrl(configuration.getResponseUrl());
        xmlData.setId(configuration.getId());
        xmlData.setHashAlgorithm(configuration.getHashAlgorithm());
        xmlData.setDocInfo(configuration.getDocInfo());
        xmlData.setDocHashHex(fileHash);
        return xmlData;
    }
}
