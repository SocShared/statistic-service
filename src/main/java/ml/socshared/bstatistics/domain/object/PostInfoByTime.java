package ml.socshared.bstatistics.domain.object;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
public class PostInfoByTime {
    String groupId;
    String postId;
    LocalDateTime begin;
    LocalDateTime end;
    DataList<Integer> variabilityNumberViews;
    DataList<Integer> variabilityNumberLikes;
    DataList<Integer> variabilityNumberShares;
    DataList<Integer> variabilityNumberComments;
}
