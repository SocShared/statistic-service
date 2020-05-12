package ml.socshared.bstatistics.domain.object;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
public class InformationOfPost {
   String groupId;
   String postId;
   Integer shares;
   Integer likes;
   Integer comments;
   Integer views;
   ZonedDateTime time;
}
