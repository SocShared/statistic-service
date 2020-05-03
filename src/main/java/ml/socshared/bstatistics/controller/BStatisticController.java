package ml.socshared.bstatistics.controller;

import ml.socshared.bstatistics.api.v1.BStatisticApi;
import ml.socshared.bstatistics.domain.object.TimeSeries;
import ml.socshared.bstatistics.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/")
public class BStatisticController implements BStatisticApi {

    private StatService service;

    @Autowired
    BStatisticController(StatService s) {
        service = s;
    }

    @Override
    @GetMapping("groups/{groupId}/estimate/online")
    public TimeSeries getEstimateOnline(@PathVariable String groupId,
                                        @RequestParam(name="begin") Date begin,
                                        @RequestParam(name="end") Date end) {
        return null;
    }

    @Override
    @GetMapping("groups/{groupId}/posts/{postId}/estimate/info")
    public TimeSeries getEstimatePostInfo(@PathVariable String groupId,
                                          @PathVariable String postId,
                                          @RequestParam Date begin,
                                          @RequestParam Date end) {
        return null;
    }

    @Override
    @PostMapping("callback/time_series")
    public void setTimeSeries(List<TimeSeries> data) {

    }
}
