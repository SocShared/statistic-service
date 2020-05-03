package ml.socshared.bstatistics.stat;

import java.util.ArrayList;
import java.util.List;

public class KDE {
    private Double h;
    private List<Double> data;
    public KDE(Double h) {
        this.h = h;
    }

    public void fit(List<Double> x) {
        data = x;
    }

    public List<Double> predict(List<Double> x) {
        List<Double> res = new ArrayList<>(x.size());
        int index = 0;
        for(Double el : x) {
            res.set(index, predict(el));
        }
        return res;
    }

    public Double predict(Double x) {
        Double res = 0.0;
        for(Double el : data) {
            res +=  Distributions.normal((x-el)/h, 0.0, 1.0);
        }
        return res/(data.size()*h);
    }
}
