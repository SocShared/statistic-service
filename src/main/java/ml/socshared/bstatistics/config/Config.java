package ml.socshared.bstatistics.config;


import ml.socshared.bstatistics.client.ServiceWorkerClient;
import ml.socshared.bstatistics.client.impl.ServiceWorkerClientMOCK;
import ml.socshared.bstatistics.repository.GroupInfoRepository;
import ml.socshared.bstatistics.service.StatService;
import ml.socshared.bstatistics.service.impl.StatServiceImpl;
import ml.socshared.bstatistics.stat.KDE;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

@Configuration
@EnableJpaRepositories("ml.socshared.bstatistics.repository")
public class Config{

    ApplicationContext contextApp;
    @Autowired
    Config(ApplicationContext app) {
        contextApp = app;
    }
    @Bean
    ServiceWorkerClient getServiceWorkerClient() {
        return new ServiceWorkerClientMOCK();
    }

    @Bean
    KDE getKdeEstimator() {
        return new KDE(Constants.STAT_KDE_H);
    }
    @Bean
    Integer getConstKdeEstimateSize() {
        return Constants.STAT_KDE_ESTIMATE_SIZE;
    }

    @Bean
    Integer getConstTrackingNewsNumDays() {
        return Constants.NEWS_TRACKING_NUM_DAYS;
    }
}
