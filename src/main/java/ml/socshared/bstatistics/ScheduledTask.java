package ml.socshared.bstatistics;


import lombok.extern.slf4j.Slf4j;
import ml.socshared.bstatistics.client.ServiceWorkerClient;
import ml.socshared.bstatistics.domain.db.TargetPost;
import ml.socshared.bstatistics.repository.TargetPostRepository;
import ml.socshared.bstatistics.service.impl.Util;
import ml.socshared.bstatistics.service.sentry.SentrySender;
import ml.socshared.bstatistics.service.sentry.SentryTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class ScheduledTask {

    ServiceWorkerClient client;
    TargetPostRepository repository;
    Integer trackingNumDays;
    SentrySender sentrySender;
    @Autowired
    ScheduledTask(ServiceWorkerClient swc, TargetPostRepository rep,
                  @Qualifier("getConstTrackingNewsNumDays") Integer tracking_num_days,
                  SentrySender sender)
    {
        client = swc;
        repository = rep;
        trackingNumDays = tracking_num_days;
        sentrySender = sender;
    }

    @Scheduled(fixedDelay = 1728000)//28,8 minutes
    public void requestToInitDataCollectionToWorker() {
        log.info("Run scheduled operation: request to initialize operation of collection data of group in Service Worker");
        try {
            List<TargetPost> tps = repository.findRecordAddedAfter(Util.timeUtc().minusDays(trackingNumDays));
            //Обращение к service worker из BSTAT
            client.executeTaskOfCollectionData();
        } catch (Exception exp) {
            log.warn("Oops! Something went wrong..." + exp.getMessage());
        }

        sentrySender.sentryMessage("Scheduled  task: Initialization of the collection of statistics from social networks",
                Collections.emptyMap(), Collections.singletonList(SentryTag.ScheduledStatisticCollection));

    }
}
