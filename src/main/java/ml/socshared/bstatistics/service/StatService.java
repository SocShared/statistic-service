package ml.socshared.bstatistics.service;

import ml.socshared.bstatistics.domain.object.TimeSeries;
import ml.socshared.bstatistics.repository.GroupInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


public interface StatService {

    TimeSeries estimateOnlineOfHour(String groupId, LocalDate begin);
    TimeSeries estimateOnlineOfDay(String groupId, LocalDate begin, LocalDate end);
    TimeSeries estimateOnlineOfWeek(String groupId, LocalDate begin, LocalDate end);

    TimeSeries estimatePostShareOfHour(String groupId, String postId, LocalDate begin, LocalDate end);

    TimeSeries estimatePostLikesOfHour(String groupId, String postId);
    TimeSeries estimatePostLikesOfDay(String groupId, String postId);
    TimeSeries estimatePostLikes(String groupId, String postId, LocalDate begin, LocalDate end);

    TimeSeries estimatePostViews(String groupId, String postId, LocalDate begin, LocalDate end);
    TimeSeries estimatePostComments(String groupId, String postId, LocalDate begin, LocalDate end);

}