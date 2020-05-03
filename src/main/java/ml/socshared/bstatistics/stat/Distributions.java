package ml.socshared.bstatistics.stat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Distributions {



    public static List<Double> normal(List<Double> x, Double loc, Double scale) {
        List<Double> res = new LinkedList<>();
        for (Double el : x) {
            res.add(normal(el, loc, scale));
        }
        return res;
    }

    public static List<Double> linspace(Double begin, Double end, Integer size) {
        Double step =   (end - begin) / size;
        List<Double> arr = new ArrayList<>(size);
        arr.set(0, begin);
        for(int i = 1; i < size; i++) {
            arr.set(i, arr.get(i-1)+step);
        }
        return arr;
    }

    public static Double normal(Double x, Double loc, Double scale) {
        return Math.exp(-0.5*Math.pow((x-loc)/(scale), 2))/(scale*Math.sqrt(2*Math.PI));
    }
}
