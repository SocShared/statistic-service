package ml.socshared.bstatistics.domain.object;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class TimeSeries<T> extends DataList<T> {
    LocalDate begin;
    LocalDate end;
    Duration step;
}
