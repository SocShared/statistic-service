package ml.socshared.bstatistics.service.impl;

import ml.socshared.bstatistics.exception.InvalidArgumentException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

public class Util {
    public static <T>  int upperBound(List<? extends T> list, T key, Comparator<? super T> c, int lo, int hi) {
        int low = lo-1, high = hi;
        while (low+1 != high)
        {
            int mid = (low+high)>>>1;
            if (c.compare(list.get(mid), key) > 0) high=mid;
            else low=mid;
        }
        int p = low;
        if ( p >= hi || c.compare(list.get(p), key) != 0 )
            p=-1;//no key found
        return p;
    }

    public static void checkDate(LocalDate begin, LocalDate end) {
        if(begin.isAfter(end)) {
            throw new InvalidArgumentException("begin date cannot be greater than end date");
        }
    }

    public static ZonedDateTime timeUtc() {
        return ZonedDateTime.now(ZoneOffset.UTC);
    }
    public static ZonedDateTime toTimeUtc(LocalDate date, LocalTime time) {
        return ZonedDateTime.of(date, time, ZoneOffset.UTC);
    }

}
