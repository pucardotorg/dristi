package com.pucar.drishti.config;


import org.springframework.stereotype.Component;


@Component
public class ServiceConstants {

    public static final String EXTERNAL_SERVICE_EXCEPTION = "External Service threw an Exception: ";
    public static final String SEARCHER_SERVICE_EXCEPTION = "Exception while fetching from searcher: ";
    public static final String AUTHORIZATION = "Basic ZWdvdi11c2VyLWNsaWVudDo=";

    private ServiceConstants() {
    }
}
