package ml.socshared.bstatistics.domain.response;

import lombok.Data;
import ml.socshared.bstatistics.domain.object.TimePoint;
import ml.socshared.bstatistics.domain.object.TimeSeries;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;

import java.util.UUID;

@Data
public class GroupInfoResponse {
    UUID systemGroupId;
    SocialNetwork socialNetwork;
    TimeSeries<TimePoint<Integer>> online;
    TimeSeries<TimePoint<Integer>> subscribers;
 }
