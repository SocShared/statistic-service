package ml.socshared.bstatistics.api.v1;

import ml.socshared.bstatistics.domain.object.TimeSeries;

import java.util.Date;
import java.util.List;

public interface BStatisticApi {
    //Get
    TimeSeries getEstimateOnline(String groupId, Date begin, Date end);
    TimeSeries getEstimatePostInfo(String groupId, String postId, Date begin, Date end);

    //Post - callback for Worker
    void setTimeSeries(List<TimeSeries> data);

}
