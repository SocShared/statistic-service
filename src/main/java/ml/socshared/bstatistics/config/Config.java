package ml.socshared.bstatistics.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ml.socshared.bstatistics.client.ServiceWorkerClient;
import ml.socshared.bstatistics.client.impl.ServiceWorkerClientMOCK;
import ml.socshared.bstatistics.config.json.LocalDateTimeSerializer;
import ml.socshared.bstatistics.stat.KDE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.ZonedDateTime;

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

    @Bean
    ObjectMapper getJacksonMapperBuilder() {
        SimpleModule timeModule = new SimpleModule("timeModule");
        timeModule.addSerializer(new LocalDateTimeSerializer());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(timeModule);
        return mapper;
    }
}
