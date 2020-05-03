package ml.socshared.bstatistics.service.impl;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.bstatistics.config.Constants;
import ml.socshared.bstatistics.domain.db.GroupInfo;
import ml.socshared.bstatistics.domain.object.SeriesType;
import ml.socshared.bstatistics.domain.object.TimeSeries;
import ml.socshared.bstatistics.exception.InvalidArgumentException;
import ml.socshared.bstatistics.repository.GroupInfoRepository;
import ml.socshared.bstatistics.service.StatService;
import ml.socshared.bstatistics.stat.Distributions;
import ml.socshared.bstatistics.stat.KDE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class StatServiceImpl implements StatService {
    private GroupInfoRepository rep;
    private KDE kde;
    private Integer kdeEstimationSize;

    @Autowired
    public StatServiceImpl(GroupInfoRepository rep, KDE kde_estimator,
                            @Qualifier("getConstKdeEstimateSize") Integer stat_kde_estimation_size) {
        this.rep = rep;
        kde = kde_estimator;
        kdeEstimationSize = stat_kde_estimation_size;
    }


    @Override
    public TimeSeries estimateOnlineOfHour(String groupId, LocalDate begin) {
        List<GroupInfo> info = rep.findBetweenDates(groupId, begin, begin);
        List<Double> onlineValues = new LinkedList<>();
        for(GroupInfo el : info) {
            onlineValues.add(Double.valueOf(el.getOnline()));
        }
        kde.fit(onlineValues);
        List<Double> x = Distributions.linspace(0.0, 1.0, Constants.STAT_KDE_ESTIMATE_SIZE);
        List<Double> kde_estimate = kde.predict(x);
        TimeSeries tsr = new TimeSeries();
        tsr.setData(kde_estimate);
        tsr.setType(SeriesType.ONLINE);
        tsr.setBegin(begin);
        tsr.setEnd(begin);
        return tsr;
    }

    @Override
    public TimeSeries estimateOnlineOfDay(String groupId, LocalDate begin, LocalDate end) {
        if (begin.isAfter(end)) {
            log.warn("Invalid arguments: Begin date, End date");
            throw new InvalidArgumentException("Begin date not be after the end date");
        }
        LocalDate date = begin;
        List<GroupInfo> info = rep.findBetweenDates(groupId, begin, end);

        do{

        } while(!date.equals(end));
    }

    @Override
    public TimeSeries estimateOnlineOfWeek(String groupId, LocalDate begin, LocalDate end) {
        return null;
    }

    @Override
    public TimeSeries estimatePostShareOfHour(String groupId, String postId, LocalDate begin, LocalDate end) {
        return null;
    }

    @Override
    public TimeSeries estimatePostLikesOfHour(String groupId, String postId) {
        return null;
    }

    @Override
    public TimeSeries estimatePostLikesOfDay(String groupId, String postId) {
        return null;
    }

    @Override
    public TimeSeries estimatePostLikes(String groupId, String postId, LocalDate begin, LocalDate end) {
        return null;
    }

    @Override
    public TimeSeries estimatePostViews(String groupId, String postId, LocalDate begin, LocalDate end) {
        return null;
    }

    @Override
    public TimeSeries estimatePostComments(String groupId, String postId, LocalDate begin, LocalDate end) {
        return null;
    }
}
