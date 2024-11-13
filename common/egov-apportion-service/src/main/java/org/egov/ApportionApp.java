package org.egov;


import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.TimeZone;

@SpringBootApplication
@ComponentScan(basePackages = { "org.egov", "org.egov.web.controllers" , "org.egov.config"})
@Import({ TracerConfiguration.class })
public class ApportionApp {

    @Value("${app.timezone}")
    private String timeZone;

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		DeserializationConfig originalConfig = objectMapper.getDeserializationConfig();
		DeserializationConfig newConfig = originalConfig.with(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
		objectMapper.setConfig(newConfig).setTimeZone(TimeZone.getTimeZone(timeZone));

		return objectMapper;
                
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApportionApp.class, args);
    }

}
