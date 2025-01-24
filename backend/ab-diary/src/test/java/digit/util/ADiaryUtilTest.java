package digit.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ADiaryUtilTest {

    private ADiaryUtil aDiaryUtil;

    @BeforeEach
    void setUp() {
        aDiaryUtil = new ADiaryUtil();
    }

    @Test
    void testGetCurrentTimeInMilliSec() {
        Long currentTime = aDiaryUtil.getCurrentTimeInMilliSec();

        assertNotNull(currentTime);
        assertTrue(currentTime > 0);

        long systemTime = System.currentTimeMillis();
        assertTrue(Math.abs(systemTime - currentTime) < 1000);
    }

    @Test
    void testGenerateUUID() {
        UUID uuid = aDiaryUtil.generateUUID();

        assertNotNull(uuid);

        assertDoesNotThrow(() -> UUID.fromString(uuid.toString()));

        UUID anotherUuid = aDiaryUtil.generateUUID();
        assertNotNull(anotherUuid);
        assertNotEquals(uuid, anotherUuid);
    }
}
