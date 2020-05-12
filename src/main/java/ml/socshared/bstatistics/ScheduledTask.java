package ml.socshared.bstatistics;


import lombok.extern.slf4j.Slf4j;
import ml.socshared.bstatistics.client.ServiceWorkerClient;
import ml.socshared.bstatistics.domain.db.TargetPost;
import ml.socshared.bstatistics.repository.TargetPostRepository;
import ml.socshared.bstatistics.service.impl.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Component
@Slf4j
public class ScheduledTask {

    ServiceWorkerClient client;
    TargetPostRepository repository;
    Integer trackingNumDays;
    @Autowired
    ScheduledTask(ServiceWorkerClient swc, TargetPostRepository rep,
                  @Qualifier("getConstTrackingNewsNumDays") Integer tracking_num_days)
    {
        client = swc;
        repository = rep;
        trackingNumDays = tracking_num_days;
    }

    @Scheduled(fixedDelay = 5000)
    public void requestToInitDataCollectionToWorker() {
        log.info("Run scheduled operation: request to initialize operation of collection data of group in Service Worker");
        try {
            List<TargetPost> tps = repository.findRecordAddedAfter(Util.timeUtc().minusDays(trackingNumDays));
            client.executeTaskOfCollectionData();
        } catch (Exception exp) {
            log.warn("Oops! Something went wrong..." + exp.getMessage());
        }

    }
}
