package org.pucar.dristi.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import org.pucar.dristi.config.Configuration;
import static org.pucar.dristi.config.ServiceConstants.*;

@Slf4j
@Component
public class UrlShortenerUtil {
    private final RestTemplate restTemplate;
    private final Configuration configs;

    @Autowired
    public UrlShortenerUtil(
            RestTemplate restTemplate,
            Configuration configs) {
        this.restTemplate = restTemplate;
        this.configs = configs;
    }


    public String getShortenedUrl(String url){

        HashMap<String,String> body = new HashMap<>();
        body.put(URL,url);
        StringBuilder builder = new StringBuilder(configs.getUrlShortnerHost());
        builder.append(configs.getUrlShortnerEndpoint());
        String res = restTemplate.postForObject(builder.toString(), body, String.class);

        if(StringUtils.isEmpty(res)){
            log.error(URL_SHORTENING_ERROR_CODE, URL_SHORTENING_ERROR_MESSAGE + url); ;
            return url;
        }
        else return res;
    }


}