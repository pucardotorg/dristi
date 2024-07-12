package org.pucar.dristi.service;

import org.springframework.stereotype.Service;

@Service
public class ServiceUrlMapperVCService {

    public String getSVcUrlMapping(String refCode){
        String urlMapping=null;
        switch (refCode) {
            case "summon":
                urlMapping = "https://dristi-dev.pucar.org/task/v1/search";
                break;
        }
        return urlMapping;
    }
}
