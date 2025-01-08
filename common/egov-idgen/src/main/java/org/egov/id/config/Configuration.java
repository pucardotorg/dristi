package org.egov.id.config;

import lombok.*;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@Import({TracerConfiguration.class})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Configuration {

    @Value("${dristi.kollam.court.id}")
    private String kollamCourtId;

    @Value("${sequence.list}")
    private List<String> sequenceList;

    @Value("${sequence.list.require.court-id}")
    private List<String> sequenceListRequireCourtId;

    @Value("${schedule.cron.expression}")
    private String cronExpression;

    @Bean
    public String scheduleCronExpression() {
        return cronExpression;
    }
}