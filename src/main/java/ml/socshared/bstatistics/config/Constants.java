package ml.socshared.bstatistics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

public class Constants {
    @Value("${service.stat.kde-h}")
    public final static Double STAT_KDE_H = 0.3;

    @Value("${service.stat.kde_estimate_size}")
    public static final Integer STAT_KDE_ESTIMATE_SIZE = 200;

    @Value("${service.news.tracking_num_days}")
    public static final Integer NEWS_TRACKING_NUM_DAYS = 3;

    @Value("${service.news.number_of_checks_per_day}")
    public static final Long NEWS_NUM_CHECK_PER_DAY = 50L;
}
