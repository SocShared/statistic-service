package ml.socshared.bstatistics.service;

import ml.socshared.bstatistics.domain.object.InformationOfPost;
import ml.socshared.bstatistics.domain.object.TimeSeries;

import java.time.LocalDate;
import java.util.List;


public interface StatService {
    TimeSeries<Integer> getOnlineByTime(String groupId, LocalDate begin, LocalDate end);
    void updateInformationOfPost(List<InformationOfPost> data);
}