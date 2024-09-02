package digit.task;

import digit.service.CauseListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableScheduling
public class ScheduledTask {

    private CauseListService causeListService;

    @Autowired
    public ScheduledTask(CauseListService causeListService) {
        this.causeListService = causeListService;

    }

    @Async
    @Scheduled(cron = "${config.causelist.generate}", zone = "Asia/Kolkata")
    public void generateCauseList() {
        log.info("Starting Cron Job For Generating CauseList");
        causeListService.updateCauseListForTomorrow();
        log.info("Completed Cron Job For Generating CauseList");
    }

}
