package ml.socshared.bstatistics.domain.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InformationOfGroup {
    String groupId;
    Integer subscribersOnline;
    Integer subscribersNumber;
    LocalDateTime time;
}
