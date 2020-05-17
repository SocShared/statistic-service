package ml.socshared.bstatistics.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ml.socshared.bstatistics.client.ServiceWorkerClient;
import ml.socshared.bstatistics.client.impl.ServiceWorkerClientMOCK;
import ml.socshared.bstatistics.config.json.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Configuration
@EnableJpaRepositories("ml.socshared.bstatistics.repository")
public class Config{

    @Bean
    ServiceWorkerClient getServiceWorkerClient() {
        return new ServiceWorkerClientMOCK();
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
        timeModule.addSerializer(new ZonedDateTimeSerializer());
        timeModule.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
        timeModule.addSerializer(new LocalDateSerializer());
        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        timeModule.addSerializer(new LocalDateTimeSerializer());
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(timeModule);
        return mapper;
    }
}
