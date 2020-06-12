package ml.socshared.bstatistics.api.v1;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ml.socshared.bstatistics.domain.db.GroupInfo;
import ml.socshared.bstatistics.domain.object.*;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

public interface BStatisticApi {
//    @ApiOperation(value = "Time Series of subscribers of group online by time.", response = TimeSeries.class)
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successfully retrieved Time Series"),
//            @ApiResponse(code = 404, message = "Not found information by group")
//    })
//    TimeSeries<Integer> getGroupOnline(String groupId, SocialNetwork soc, Long begin, Long end);

    TimeSeries<Integer> getVariabilitySubscribersOfGroup(UUID groupId, SocialNetwork soc, Long begin, Long end);
  //  GroupInfo getNumberSubscribersOfGroup(String groupId, SocialNetwork soc);

    @ApiOperation(value = "Return Time Series  number of comments, reposts, likes", response = TimeSeries.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Time Series"),
            @ApiResponse(code = 404, message = "Not found information by group or post")
    })
    PostInfoByTime getInfoVariabilityByTimeOfPost(UUID groupId, UUID postId,SocialNetwork soc, Long begin, Long end);

    @ApiOperation(value = "Value Engagement rate by pos of group")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Seccessfully returned engagement rate"),
            @ApiResponse(code = 404, message = "Not found group or post by id")
            })
    PostSummary getPostInfo(UUID groupId, UUID postId, SocialNetwork soc);

    //Post - callback for Worker
    @ApiOperation(value = "Callback of Service Worker, on witch return statistic information of group or posts")
    @ApiResponses(value = {
                 @ApiResponse(code = 200, message = "Data received successfully")
            }
    )
    void setTimeSeriesofPost(DataList<InformationOfPost> data);

    //void setTimeSeriesOfGroup(@RequestBody DataList<InformationOfGroup> data);

}
