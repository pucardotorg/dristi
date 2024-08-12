package org.pucar.dristi;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import({ TracerConfiguration.class })
@SpringBootApplication
@ComponentScan(basePackages = { "org.pucar.dristi", "org.pucar.dristi.web.controllers", "org.pucar.dristi.config" })
public class Main {

<<<<<<< HEAD
	public static void main(String[] args){
=======
	public static void main(String[] args) throws Exception {
>>>>>>> main
		SpringApplication.run(Main.class, args);
	}

}
