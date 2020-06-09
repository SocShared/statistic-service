package ml.socshared.bstatistics;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.bstatistics.client.ServiceWorkerClient;
import ml.socshared.bstatistics.config.RabbitMQConfig;
import ml.socshared.bstatistics.domain.db.TargetPost;
import ml.socshared.bstatistics.domain.object.InformationOfGroup;
import ml.socshared.bstatistics.domain.object.InformationOfPost;
import ml.socshared.bstatistics.domain.rabbitmq.RabbitMqType;
import ml.socshared.bstatistics.domain.rabbitmq.request.RabbitMqSocialRequest;
import ml.socshared.bstatistics.domain.rabbitmq.response.RabbitMqPostResponse;
import ml.socshared.bstatistics.domain.rabbitmq.response.RabbitMqResponse;
import ml.socshared.bstatistics.domain.rabbitmq.response.RabbitMqResponseAll;
import ml.socshared.bstatistics.repository.TargetPostRepository;
import ml.socshared.bstatistics.service.StatService;
import ml.socshared.bstatistics.service.impl.Util;
import ml.socshared.bstatistics.service.sentry.SentrySender;
import ml.socshared.bstatistics.service.sentry.SentryTag;
import org.hibernate.resource.beans.container.spi.BeanLifecycleStrategy;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.sleuth.instrument.messaging.SleuthMessagingProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class ScheduledTask {

    StatService service;
    ServiceWorkerClient client;
    TargetPostRepository repository;
    Integer trackingNumDays;
    SentrySender sentrySender;
    RabbitTemplate rabbitTemplate;

    @Autowired
    ScheduledTask(ServiceWorkerClient swc, TargetPostRepository rep,
                  @Qualifier("getConstTrackingNewsNumDays") Integer tracking_num_days,
                  SentrySender sender,  RabbitTemplate rabbit, StatService sService)
    {
        client = swc;
        repository = rep;
        trackingNumDays = tracking_num_days;
        sentrySender = sender;
        rabbitTemplate = rabbit;
        service = sService;
    }

    @Scheduled(fixedDelay = 1728000)//28,8 minutes
    public void requestToInitDataCollectionToWorker() {
        log.info("Run scheduled operation: request to initialize operation of collection data of group in Service Worker");
        try {
            List<TargetPost> tps = repository.findRecordAddedAfter(Util.timeUtc().minusDays(trackingNumDays));
            ObjectMapper mapper = new ObjectMapper();
            Set<String> groupIds = new HashSet<>();
            for(TargetPost target : tps) {
                if(!groupIds.contains(target.getGroupId())) {
                    groupIds.add(target.getGroupId());
                    RabbitMqSocialRequest groupRequest = new RabbitMqSocialRequest();
                    groupRequest.setType(RabbitMqType.GROUP);
                    groupRequest.setGroupId(target.getGroupId());
                    groupRequest.setSystemUserId(target.getSystemUserId());
                    String serialize = mapper.writeValueAsString(groupRequest);
                    rabbitTemplate.convertAndSend(RabbitMQConfig.BSTAT_REQUEST_EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY,
                                                    serialize);
                }

                RabbitMqSocialRequest postRequest = new RabbitMqSocialRequest();
                postRequest.setType(RabbitMqType.POST);
                postRequest.setSystemUserId(target.getSystemUserId());
                postRequest.setGroupId(target.getGroupId());
                postRequest.setPostId(target.getPostId());
                postRequest.setSocialNetwork(target.getSocialNetwork());
                String serialize = mapper.writeValueAsString(postRequest);
                rabbitTemplate.convertAndSend(RabbitMQConfig.BSTAT_REQUEST_EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY,
                        serialize);
            }
        } catch (Exception exp) {
            log.warn("Oops! Something went wrong..." + exp.getMessage());
        }

        sentrySender.sentryMessage("Scheduled  task: Initialization of the collection of statistics from social networks",
                Collections.emptyMap(), Collections.singletonList(SentryTag.ScheduledStatisticCollection));

    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_BSTAT_RESPONSE_NAME)
    void getResultOfCollection(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            RabbitMqResponseAll response = mapper.readValue(message, RabbitMqResponseAll.class);

            if(response.getType().equals(RabbitMqType.POST)) {
                service.updateInformationOfPost(response);
            } else if(response.getType().equals(RabbitMqType.GROUP)){
                service.updateInformationOfGroup(response);
            }
            log.info("{}", message.toString());
        } catch(Exception exp) {
            log.error("Error receive result of collection statistic: {}", exp.getMessage());
        }
    }
}
