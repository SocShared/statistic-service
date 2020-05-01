package ml.socshared.bstatistics.service;

import ml.socshared.bstatistics.domain.object.TimeSeries;
import ml.socshared.bstatistics.repository.GroupInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;

@Service
public interface StatService {

    TimeSeries estimateOnlineOfHour(String groupId, Data begin);
    TimeSeries estimateOnlineOfDay(String groupId, Data begin, Data end);
    TimeSeries estimateOnlineOfWeek(String groupId, Data begin, Data end);

    TimeSeries estimatePostShareOfHour(String groupId, String postId, Data begin, Data end);

    TimeSeries estimatePostLikesOfHour(String groupId, String postId);
    TimeSeries estimatePostLikesOfDay(String groupId, String postId);
    TimeSeries estimatePostLikes(String groupId, String postId, Data begin, Data end);

    TimeSeries estimatePostViews(String groupId, String postId, Data begin, Data end);
    TimeSeries estimatePostComments(String groupId, String postId, Data begin, Data end);

}