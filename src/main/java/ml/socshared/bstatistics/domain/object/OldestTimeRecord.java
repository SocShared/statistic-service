package ml.socshared.bstatistics.domain.object;

import java.time.ZonedDateTime;

public class OldestTimeRecord {
    ZonedDateTime time;
    public OldestTimeRecord(ZonedDateTime t) {
        time = t;
    }
    public ZonedDateTime getTime() {
        return time;
    }
}
