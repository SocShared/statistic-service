package ml.socshared.bstatistics.api.v1;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ml.socshared.bstatistics.domain.object.PostInfoByTime;
import ml.socshared.bstatistics.domain.object.PostSummary;
import ml.socshared.bstatistics.domain.object.TimeSeries;
import ml.socshared.bstatistics.domain.object.InformationOfPost;

import java.time.LocalDate;
import java.util.List;

public interface BStatisticApi {
    @ApiOperation(value = "Time Series of subscribers of group online by time.", response = TimeSeries.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Time Series"),
            @ApiResponse(code = 404, message = "Not found information by group")
    })
    TimeSeries<Integer> getOnline(String groupId, LocalDate begin, LocalDate end);

    @ApiOperation(value = "Return Time Series  number of comments, reposts, likes", response = TimeSeries.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Time Series"),
            @ApiResponse(code = 404, message = "Not found information by group or post")
    })
    PostInfoByTime getInfoVariabilityByTimeOfPost(String groupId, String postId, LocalDate begin, LocalDate end);

    @ApiOperation(value = "Value Engagement rate by pos of group")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Seccessfully returned engagement rate"),
            @ApiResponse(code = 404, message = "Not found group or post by id")
            })
    PostSummary getPostInfo(String groupId, String postId);

    //Post - callback for Worker
    @ApiOperation(value = "Callback of Service Worker, on witch return statistic information of group or posts")
    @ApiResponses(value = {
                 @ApiResponse(code = 200, message = "Data received successfully")
            }
    )
    void setTimeSeries(List<InformationOfPost> data);

}
