package ml.socshared.bstatistics.service.impl;

import ml.socshared.bstatistics.exception.InvalidArgumentException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

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

    /**
     * Метод принимающий список объектов <ContainerT>, которому выполняет гуппировку по полю groupingValueGetter
     * , а затем по группе выполняет бинарную операцию op по полю valueGetter
     *     объекта <ContainerT>. Пр
     * @param listObjects - список объектов, к котором выполнятся бинаррная опперация по группе
     * @param valueGetter - геттер поля, к которому применяется бинарная операция
     * @param groupingValueGetter - геттер поля, по которому выполняется группировка
     * @param comparator - компоратор группирующего поля
     * @param op - бинарная операция
     * @param defaultGetter - функция возвращающее значение по умолчанию для valueGetter. Например, для чисел это может быть 0
     * @param <ValueApply> - тип поля, к которому применяется бинарная операция
     * @param <ValueGrouping> - тип поля, по которому выполняется группировка
     * @param <ContainerT> - тип класса, к полям которого выполняется операции.
     * @return
     */
    public static <ValueApply, ValueGrouping, ContainerT>  Map<ValueGrouping,ValueApply> applyToGroupBy(
            List<ContainerT> listObjects, Function<ContainerT, ValueApply> valueGetter,
            Function<ContainerT, ValueGrouping> groupingValueGetter,
            BinaryOperator<ValueApply> op, Supplier<ValueApply> defaultGetter) {

            HashMap<ValueGrouping, ValueApply> grouped = new HashMap<>();
            for(ContainerT el : listObjects) {
                ValueGrouping gv = groupingValueGetter.apply(el);
                grouped.put(gv, op.apply(grouped.getOrDefault(gv, defaultGetter.get()), valueGetter.apply(el)));
            }
        return grouped;
    }


}
