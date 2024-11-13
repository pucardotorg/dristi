package org.egov.transformer;

import org.egov.tracer.config.TracerConfiguration;
import org.egov.transformer.config.MainConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
@Import({ MainConfiguration.class })
@SpringBootApplication
public class TransformerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransformerApplication.class, args);
    }
}
