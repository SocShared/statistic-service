package ml.socshared.bstatistics.service;

import ml.socshared.bstatistics.domain.db.GroupInfo;
import ml.socshared.bstatistics.domain.object.*;
import ml.socshared.bstatistics.domain.rabbitmq.response.RabbitMqResponseAll;
import ml.socshared.bstatistics.domain.response.GroupInfoResponse;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;

import java.time.LocalDate;
import java.util.UUID;


public interface StatService {
  //  TimeSeries<Integer> getOnlineByTime(String groupId,SocialNetwork soc, LocalDate begin, LocalDate end);
  public GroupInfoResponse getGroupInfoByTime(UUID groupId, SocialNetwork soc, LocalDate begin, LocalDate end);
    PostInfoByTime getPostInfoByTime(UUID groupId, UUID postId, SocialNetwork soc, LocalDate begin, LocalDate end);
    PostSummary getPostSummary(UUID groupId, UUID postId, SocialNetwork soc);
   // GroupInfo getGroupSubscribers(String groupId, SocialNetwork soc);
    void updateInformationOfPost(RabbitMqResponseAll data);
    void updateInformationOfGroup(RabbitMqResponseAll data);
}