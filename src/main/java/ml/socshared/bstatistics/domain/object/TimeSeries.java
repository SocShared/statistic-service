package ml.socshared.bstatistics.domain.object;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TimeSeries {
    SeriesType type;
    Date begin;
    Date end;
    List<Double> data;
}
