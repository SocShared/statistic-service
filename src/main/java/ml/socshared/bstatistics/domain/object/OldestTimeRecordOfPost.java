package ml.socshared.bstatistics.domain.object;

import java.time.ZonedDateTime;

public class OldestTimeRecordOfPost {
    ZonedDateTime time;
    public OldestTimeRecordOfPost(ZonedDateTime t) {
        time = t;
    }
    public ZonedDateTime getTime() {
        return time;
    }
}
