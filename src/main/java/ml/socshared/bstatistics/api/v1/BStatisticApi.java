package ml.socshared.bstatistics.api.v1;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ml.socshared.bstatistics.domain.object.TimeSeries;

import java.util.Date;
import java.util.List;

public interface BStatisticApi {
    @ApiOperation(value = "Time Series of subscribers of group online by time.", response = TimeSeries.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Time Series"),
            @ApiResponse(code = 404, message = "Not found information by group")
    })
    TimeSeries getEstimateOnline(String groupId, Date begin, Date end);

    @ApiOperation(value = "Time Series of post info of group online by time.", response = TimeSeries.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Time Series"),
            @ApiResponse(code = 404, message = "Not found information by group or post")
    })
    TimeSeries getEstimatePostInfo(String groupId, String postId, Date begin, Date end);

    //Post - callback for Worker
    @ApiOperation(value = "Callback of Service Worker, on witch return statistic information of group or posts")
    @ApiResponses(value = {
                 @ApiResponse(code = 200, message = "Data received successfully")
            }
    )
    void setTimeSeries(List<TimeSeries> data);

}
