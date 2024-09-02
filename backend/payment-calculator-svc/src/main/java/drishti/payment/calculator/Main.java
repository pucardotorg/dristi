package drishti.payment.calculator;


import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import({TracerConfiguration.class})
@SpringBootApplication
@ComponentScan(basePackages = {"drishti.payment.calculator", "drishti.payment.calculator.web.controllers", "drishti.payment.calculator.config"})
public class Main {


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
