package ml.socshared.bstatistics.service;

import ml.socshared.bstatistics.domain.db.Group;
import ml.socshared.bstatistics.domain.db.GroupInfo;
import ml.socshared.bstatistics.domain.db.PostInfo;
import ml.socshared.bstatistics.domain.object.*;

import java.time.LocalDate;
import java.util.List;


public interface StatService {
    TimeSeries<Integer> getOnlineByTime(String groupId, LocalDate begin, LocalDate end);
    PostInfoByTime getPostInfoByTime(String groupId, String postId, LocalDate begin, LocalDate end);
    PostSummary getPostSummary(String groupId, String postId);
    Group getGroupSubscribers(String groupId);
    void updateInformationOfPost(List<InformationOfPost> data);
    void updateInformationOfGroup(List<InformationOfGroup> data);
}