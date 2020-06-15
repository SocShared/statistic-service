package ml.socshared.bstatistics;


import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
    @Value("${service.tracking_num_days}")
    private  Integer trackingNumDays = 3;
    @Value("${service.data_page_size_for_step}")
    private Integer pageDataSizeForOneStep = 100;
    private final  SentrySender sentrySender;
    private final  RabbitTemplate rabbitTemplate;

    private  final int delay =10000; //1728000;
    private  final int milli = 1000;

    LocalDateTime beforeDelayStart = LocalDateTime.now().minusMinutes(delay/milli);



    @Scheduled(fixedDelay = delay)//28,8 minutes
    public void requestToInitDataCollectionToWorker() {
        log.info("Run scheduled operation: request to initialize operation of collection data of group in Service Worker");
       try {
           LocalDateTime beforeStart = beforeDelayStart;
           beforeDelayStart = LocalDateTime.now();
           storageService.storageLoadPostNotOlderThat(beforeStart);
           Page<Post> postsPage = null;
           ObjectMapper mapper = new ObjectMapper();
           int i = 0;
           int countGroups = 0;
           do {
               postsPage = storageService.getPostNotOlderThat(LocalDateTime.now().minusDays(trackingNumDays), PageRequest.of(i, pageDataSizeForOneStep));
               Set<Pair<String, SocialNetwork>> groupIds = new HashSet<>();
               for (Post el : postsPage) {
                   RabbitMqSocialRequest request = new RabbitMqSocialRequest();
                   request.setSocialNetwork(el.getGroup().getSocialNetwork());
                   request.setGroupId(el.getGroup().getSocialId());
                   request.setPostId(el.getSocId());
                   request.setType(RabbitMqType.POST);
                   request.setSystemUserId(el.getGroup().getSystemUserId());
                   String serialized = mapper.writeValueAsString(request);
                   rabbitTemplate.convertAndSend(RabbitMQConfig.BSTAT_REQUEST_EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, serialized);
                   Pair<String, SocialNetwork> groupId = Pair.of(el.getGroup().getSocialId(), el.getGroup().getSocialNetwork());
                   if(!groupIds.contains(groupId)) {
                       groupIds.add(groupId);
                       request.setType(RabbitMqType.GROUP);
                       serialized = mapper.writeValueAsString(request);
                       rabbitTemplate.convertAndSend(RabbitMQConfig.BSTAT_REQUEST_EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, serialized);
                   }
               }
               countGroups += groupIds.size();
               i++;
           } while (i < postsPage.getTotalPages());

           log.info("Request was sent for collection statistic on {} posts and {} groups", postsPage.getTotalElements(),
                   countGroups);
//           sentrySender.sentryMessage("Scheduled  task: Initialization of the collection of statistics from social networks",
//                   Collections.emptyMap(), Collections.singletonList(SentryTag.SCHEDULED_STATISTIC_COLLECTION));

       } catch (JsonProcessingException e) {
           log.error(e.getMessage());
           e.printStackTrace();
       }
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
