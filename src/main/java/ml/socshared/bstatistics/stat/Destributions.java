package ml.socshared.bstatistics.stat;

import java.util.LinkedList;
import java.util.List;

public class Destributions {



    public static List<Double> normal(List<Double> x, Double loc, Double scale) {
        List<Double> res = new LinkedList<>();
        for (Double el : x) {
            res.add(normal(el, loc, scale));
        }
        return res;
    }


    public static Double normal(Double x, Double loc, Double scale) {
        return Math.exp(-0.5*Math.pow((x-loc)/(scale), 2))/(scale*Math.sqrt(2*Math.PI));
    }
}
