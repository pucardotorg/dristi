package com.egov.icops_integrationkerala;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ TracerConfiguration.class })
@ComponentScan(basePackages = { "com.egov.icops_integrationkerala.*"})
public class IcopsIntegrationKeralaApplication {

	public static void main(String[] args) {
		SpringApplication.run(IcopsIntegrationKeralaApplication.class, args);
	}

}
