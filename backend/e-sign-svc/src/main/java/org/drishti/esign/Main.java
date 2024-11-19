package org.drishti.esign;


import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import({TracerConfiguration.class})
@SpringBootApplication()
@ComponentScan(basePackages = {"org.drishti.esign", "org.drishti.esign.web.controllers", "org.drishti.esign.config"})
public class Main {


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
