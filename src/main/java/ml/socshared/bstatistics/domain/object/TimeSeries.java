package ml.socshared.bstatistics.domain.object;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class TimeSeries {
    SeriesType type;
    LocalDate begin;
    LocalDate end;
    List<Double> data;
}
