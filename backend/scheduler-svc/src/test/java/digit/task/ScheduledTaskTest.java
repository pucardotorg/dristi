package digit.task;

import digit.service.CauseListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Future;

import static org.mockito.Mockito.*;

@EnableScheduling
class ScheduledTaskTest {

    @Mock
    private CauseListService causeListService;

    @InjectMocks
    private ScheduledTask scheduledTask;

    private ThreadPoolTaskScheduler taskScheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();
    }

    @Test
    void testGenerateCauseList() throws Exception {
        doNothing().when(causeListService).updateCauseListForTomorrow();

        Future<?> future = taskScheduler.submit(() -> scheduledTask.generateCauseList());
        future.get();

        verify(causeListService, times(1)).updateCauseListForTomorrow();
    }

    @Test
    void testGenerateCauseListLogs() throws Exception {
        doNothing().when(causeListService).updateCauseListForTomorrow();


        Future<?> future = taskScheduler.submit(() -> scheduledTask.generateCauseList());
        future.get();

        verify(causeListService, times(1)).updateCauseListForTomorrow();

    }
}
