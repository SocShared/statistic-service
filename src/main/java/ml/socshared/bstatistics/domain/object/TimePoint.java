package ml.socshared.bstatistics.domain.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimePoint <T> {
    T value;
    Long dateTime;
}
