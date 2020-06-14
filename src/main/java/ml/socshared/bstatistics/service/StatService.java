package ml.socshared.bstatistics.service;

import ml.socshared.bstatistics.domain.object.PostInfoByTime;
import ml.socshared.bstatistics.domain.object.PostSummary;
import ml.socshared.bstatistics.domain.rabbitmq.response.RabbitMqResponseAll;
import ml.socshared.bstatistics.domain.response.GroupInfoResponse;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;

import java.time.LocalDate;
import java.util.UUID;


public interface StatService {
     GroupInfoResponse getGroupInfoByTime(UUID systemUserId, UUID groupId, SocialNetwork soc, LocalDate begin, LocalDate end);
    PostInfoByTime getPostInfoByTime(UUID systemUserId, UUID groupId, UUID postId, SocialNetwork soc, LocalDate begin, LocalDate end);
    PostSummary getPostSummary(UUID systemUserId, UUID groupId, UUID postId, SocialNetwork soc);
    void updateInformationOfPost(RabbitMqResponseAll data);
    void updateInformationOfGroup(RabbitMqResponseAll data);
}