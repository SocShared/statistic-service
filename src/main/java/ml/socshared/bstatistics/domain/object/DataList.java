package ml.socshared.bstatistics.domain.object;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class DataList <T>{
    Integer size;
    List<T> data;
}
