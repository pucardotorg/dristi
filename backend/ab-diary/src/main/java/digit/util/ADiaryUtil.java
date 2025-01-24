package digit.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ADiaryUtil {

    public Long getCurrentTimeInMilliSec() {
        return System.currentTimeMillis();
    }

    public UUID generateUUID() {
        return UUID.randomUUID();
    }

}
