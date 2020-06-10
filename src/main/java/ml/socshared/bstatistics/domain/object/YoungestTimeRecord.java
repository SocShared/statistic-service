package ml.socshared.bstatistics.domain.object;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class YoungestTimeRecord {
    LocalDateTime time;

    public YoungestTimeRecord(LocalDateTime t) {
        time = t;
    }
    public LocalDateTime getTime() {
        return time;
    }
}
