package org.egov.id.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SequenceResetService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Configuration configuration;

    private static final String TIME_ZONE = "Asia/Kolkata";

    // This runs at midnight on December 31st. One second before transitioning to january 1st every year
    //59 59 23 31 12 *

    @Scheduled(cron = "#{@scheduleCronExpression}", zone = TIME_ZONE)
    public void resetSequence() {
        log.info("Starting cron job for resetting sequences");

        List<String> finalSequnceList = configuration.getSequenceList();
        configuration.getSequenceListRequireCourtId().forEach(seq-> finalSequnceList.add(seq+configuration.getKollamCourtId()));

        finalSequnceList.forEach(sequence -> {
            try {
                runQuery(sequence);
            } catch (Exception ex) {
                log.error("Error restarting sequence :: {}. Continuing with the next.", sequence, ex);
            }
        });

        log.info("Cron job completed for resetting sequences");
    }

    public void runQuery(String sequence) {
        try {
            String curValueSql = "SELECT last_value FROM " + sequence + ";";
            log.info("Executing current value query :: {}", curValueSql);

            Integer currentValue = jdbcTemplate.queryForObject(curValueSql, Integer.class);
            log.info("Current value for Sequence:: {}, is {}", sequence, currentValue);

            String alterSeqSql = "ALTER SEQUENCE " + sequence + " RESTART WITH 1;";
            log.info("Executing alter sequence query :: {}", alterSeqSql);
            jdbcTemplate.execute(alterSeqSql);
            log.info("Execution Successful for alter sequence query :: {}", alterSeqSql);

        } catch (Exception ex) {
            log.error("Exception occurred while resetting sequence :: {}", sequence, ex);
            throw new RuntimeException("Failed to reset sequence: " + sequence, ex);
        }
    }
}