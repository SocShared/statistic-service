package ml.socshared.bstatistics;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.bstatistics.client.StorageClient;
import ml.socshared.bstatistics.config.RabbitMQConfig;
import ml.socshared.bstatistics.domain.RestResponsePage;
import ml.socshared.bstatistics.domain.db.Post;
import ml.socshared.bstatistics.domain.rabbitmq.RabbitMqType;
import ml.socshared.bstatistics.domain.rabbitmq.request.RabbitMqSocialRequest;
import ml.socshared.bstatistics.domain.rabbitmq.response.RabbitMqResponseAll;
import ml.socshared.bstatistics.domain.storage.GroupPostStatus;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;
import ml.socshared.bstatistics.domain.storage.response.GroupResponse;
import ml.socshared.bstatistics.domain.storage.response.PublicationResponse;
import ml.socshared.bstatistics.repository.PostRepository;
import ml.socshared.bstatistics.security.model.TokenObject;
import ml.socshared.bstatistics.service.StatService;
import ml.socshared.bstatistics.service.StorageService;
import ml.socshared.bstatistics.service.impl.Util;
import ml.socshared.bstatistics.service.sentry.SentrySender;
import ml.socshared.bstatistics.service.sentry.SentryTag;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledTask {

    private final StatService service;
    private final  StorageService storageService;
    private final  PostRepository repository;
    @Value("${service.news.tracking_num_days}")
    private  Integer trackingNumDays;
    private final  SentrySender sentrySender;
    private final  RabbitTemplate rabbitTemplate;
    private final  StorageClient storageClient;

    private  final int delay = 1728000;
    private  final int milli = 1000;

    @Value("#{tokenGetter.tokenStorageService}")
    private TokenObject storageToken;

    private String authTokenStorage() {
        return "Bearer " + storageToken.getToken();
    }


    @Scheduled(fixedDelay = delay)//28,8 minutes
    public void requestToInitDataCollectionToWorker() {
        log.info("Run scheduled operation: request to initialize operation of collection data of group in Service Worker");
//        try {
//            Map<UUID, Pair<String, SocialNetwork> > groupIds = new HashMap<>();
//            RestResponsePage<PublicationResponse> publications = storageClient.findPostAfterDate(
//                    ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(delay/milli).toInstant().toEpochMilli(),
//                    0, 100, authTokenStorage());
//            int i = 1;
//            do{
//                for(PublicationResponse p : publications) {
//                    Post target = new Post();
//                    for(GroupPostStatus status : p.getPostStatus()) {
//                        Pair<String, SocialNetwork> groupId = groupIds.getOrDefault(status.getGroupId(), null);
//                        if(groupId == null) {
//                            GroupResponse g = storageClient.findGroupById(status.getGroupId(), authTokenStorage());
//
//                        }
//                    }
//                }
//
//                i++;
//            } while(i < publications.getTotalPages());
//            List<Post> tps = repository.findRecordAddedAfter(Util.timeUtc().minusDays(trackingNumDays));
//            ObjectMapper mapper = new ObjectMapper();
//            groupIds = new HashSet<>();
//            for(Post target : tps) {
//                if(!groupIds.contains(target.getGroupId())) {
//                    groupIds.add(target.getGroupId());
//                    RabbitMqSocialRequest groupRequest = new RabbitMqSocialRequest();
//                    groupRequest.setType(RabbitMqType.GROUP);
//                    groupRequest.setGroupId(target.getGroupId());
//                    groupRequest.setSystemUserId(target.getSystemUserId());
//                    String serialize = mapper.writeValueAsString(groupRequest);
//                    rabbitTemplate.convertAndSend(RabbitMQConfig.BSTAT_REQUEST_EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY,
//                                                    serialize);
//                }
//
//                RabbitMqSocialRequest postRequest = new RabbitMqSocialRequest();
//                postRequest.setType(RabbitMqType.POST);
//                postRequest.setSystemUserId(target.getSystemUserId());
//                postRequest.setGroupId(target.getGroupId());
//                postRequest.setPostId(target.getPostId());
//                postRequest.setSocialNetwork(target.getSocialNetwork());
//                String serialize = mapper.writeValueAsString(postRequest);
//                rabbitTemplate.convertAndSend(RabbitMQConfig.BSTAT_REQUEST_EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY,
//                        serialize);
//            }
//        } catch (Exception exp) {
//            log.warn("Oops! Something went wrong..." + exp.getMessage());
//        }
//
//        sentrySender.sentryMessage("Scheduled  task: Initialization of the collection of statistics from social networks",
//                Collections.emptyMap(), Collections.singletonList(SentryTag.ScheduledStatisticCollection));

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
