package ml.socshared.bstatistics.service;

import ml.socshared.bstatistics.domain.db.GroupInfo;
import ml.socshared.bstatistics.domain.object.*;
import ml.socshared.bstatistics.domain.rabbitmq.response.RabbitMqResponseAll;
import ml.socshared.bstatistics.domain.response.GroupInfoResponse;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;

import java.time.LocalDate;


public interface StatService {
  //  TimeSeries<Integer> getOnlineByTime(String groupId,SocialNetwork soc, LocalDate begin, LocalDate end);
  public GroupInfoResponse getGroupInfoByTime(String groupId, SocialNetwork soc, LocalDate begin, LocalDate end);
    PostInfoByTime getPostInfoByTime(String groupId, String postId,SocialNetwork soc, LocalDate begin, LocalDate end);
    PostSummary getPostSummary(String groupId, String postId, SocialNetwork soc);
   // GroupInfo getGroupSubscribers(String groupId, SocialNetwork soc);
    void updateInformationOfPost(RabbitMqResponseAll data);
    void updateInformationOfGroup(RabbitMqResponseAll data);
}