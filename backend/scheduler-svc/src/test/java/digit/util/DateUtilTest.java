package digit.util;

import digit.config.Configuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DateUtilTest {

    @Mock
    private Configuration mockConfig;

    @InjectMocks
    private DateUtil dateUtil;

    @Test
    public void testGetLocalDateTimeFromEpoch() {
        long starTime = 1614556800000L;
        when(mockConfig.getZoneId()).thenReturn("UTC");
        LocalDateTime localDateTime = dateUtil.getLocalDateTimeFromEpoch(starTime);
        assertEquals("2021-03-01T00:00", localDateTime.toString());
    }

    @Test
    public void testGetLocalTime() {
        String time = "12:00:00";
        LocalDateTime localDateTime = LocalDateTime.of(2021, 3, 1, 0, 0);
        assertEquals("2021-03-01T12:00", dateUtil.getLocalDateTime(localDateTime, time).toString());
    }

    @Test
    public void testGetLocalDateTime(){
        LocalDateTime startTime = LocalDateTime.of(2021, 3, 1, 0, 0);
        String newTime = "12:00:00";
        assertEquals("2021-03-01T12:00", dateUtil.getLocalDateTime(startTime, newTime).toString());

    }


    @Test
    public void testGetLocalDateFromEpoch() {
        long starTime = 1614556800000L;
        when(mockConfig.getZoneId()).thenReturn("UTC");
        assertEquals("2021-03-01", dateUtil.getLocalDateFromEpoch(starTime).toString());
    }

    @Test
    public void testGetEPochFromLocalDate(){
        LocalDate localDate = LocalDate.of(2021, 3, 1);
        when(mockConfig.getZoneId()).thenReturn("UTC");
        assertEquals(1614556800000L, dateUtil.getEPochFromLocalDate(localDate));
    }

    @Test
    public void testGetEpochFromLocalDateTime(){
        LocalDateTime localDateTime = LocalDateTime.of(2021, 3, 1, 0, 0);
        when(mockConfig.getZoneId()).thenReturn("UTC");
        assertEquals(1614556800000L, dateUtil.getEpochFromLocalDateTime(localDateTime));
    }

    @Test
    public void testGetStartOfTheDayForEpoch(){
        long starTime = 1614556800000L;
        when(mockConfig.getZoneId()).thenReturn("UTC");
        assertEquals(1614556800000L, dateUtil.getStartOfTheDayForEpoch(starTime));
    }
}
