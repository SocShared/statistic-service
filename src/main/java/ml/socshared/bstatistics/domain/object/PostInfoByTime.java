package ml.socshared.bstatistics.domain.object;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class PostInfoByTime {
    UUID systemGroupId;
    UUID systemPostId;
    LocalDateTime begin;
    LocalDateTime end;
    DataList<TimePoint<Integer>> variabilityNumberViews;
    DataList<TimePoint<Integer>> variabilityNumberLikes;
    DataList<TimePoint<Integer>> variabilityNumberShares;
    DataList<TimePoint<Integer>> variabilityNumberComments;
}
