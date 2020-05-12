package ml.socshared.bstatistics.domain.object;

import jdk.vm.ci.meta.Local;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class TimeSeries<T> extends DataList<T> {
    LocalDate begin;
    LocalDate end;
    Duration step;
}
