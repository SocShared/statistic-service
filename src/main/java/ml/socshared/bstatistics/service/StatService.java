package ml.socshared.bstatistics.service;

import ml.socshared.bstatistics.domain.db.PostInfo;
import ml.socshared.bstatistics.domain.object.InformationOfPost;
import ml.socshared.bstatistics.domain.object.PostInfoByTime;
import ml.socshared.bstatistics.domain.object.PostSummary;
import ml.socshared.bstatistics.domain.object.TimeSeries;

import java.time.LocalDate;
import java.util.List;


public interface StatService {
    TimeSeries<Integer> getOnlineByTime(String groupId, LocalDate begin, LocalDate end);
    PostInfoByTime getPostInfoByTime(String groupId, String postId, LocalDate begin, LocalDate end);
    PostSummary getPostSummary(String groupId, String postId);
    void updateInformationOfPost(List<InformationOfPost> data);
}