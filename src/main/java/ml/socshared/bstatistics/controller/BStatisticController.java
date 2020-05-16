package ml.socshared.bstatistics.controller;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.bstatistics.api.v1.BStatisticApi;
import ml.socshared.bstatistics.domain.db.PostInfo;
import ml.socshared.bstatistics.domain.object.*;
import ml.socshared.bstatistics.repository.PostInfoRepository;
import ml.socshared.bstatistics.service.StatService;
import ml.socshared.bstatistics.service.impl.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/")
@Slf4j
public class BStatisticController implements BStatisticApi {

    private StatService service;
    @Autowired
    private PostInfoRepository rep;

    @Autowired
    BStatisticController(StatService s) {
        service = s;
    }



    @Override
    @GetMapping("groups/{groupId}/online")
    public TimeSeries<Integer> getGroupOnline(@PathVariable String groupId,
                                              @RequestParam(name="begin") LocalDate begin,
                                              @RequestParam(name="end") LocalDate end) {
        log.info("Get online of group");
        return service.getOnlineByTime(groupId, begin, end);
    }

    @Override
    @GetMapping("groups/{groupId}/subscribers/variability")
    public TimeSeries<Integer> getVariabilitySubscribersOfGroup(@PathVariable String groupId,
                                                                @RequestParam LocalDate begin,
                                                                @RequestParam LocalDate end) {
        log.info("get time series of group (GroupID: " + groupId +") subscribers");
       // return service.getVariabilitySubscribers(groupId, begin, end);
        return null;
    }

    @Override
    @GetMapping("groups/{groupId}/subscribers")
    public Integer getNumberSubscribersOfGroup(@PathVariable  String groupId) {
       // return service.getNumberSubscribersOfGroup(groupId);
        return null;
    }

    @Override
    @GetMapping("groups/{groupId}/posts/{postId}/time_series")
    public PostInfoByTime getInfoVariabilityByTimeOfPost(@PathVariable String groupId,
                                                         @PathVariable String postId,
                                                         @RequestParam(name="begin") LocalDate begin,
                                                         @RequestParam(name="end") LocalDate end) {
        log.info("request on get info by time of post (GroupId: " + groupId + "; PostId: " + postId + ")");
        return service.getPostInfoByTime(groupId, postId, begin, end);
    }

    @Override
    @GetMapping("groups/{groupId}/posts/{postId}/summary")
    public PostSummary getPostInfo(@PathVariable String groupId,
                                   @PathVariable String postId) {
        log.info("request on get info of post (GroupId: " + groupId + "; PostId: " + postId + ")");
        return service.getPostSummary(groupId, postId);
    }

    //TODO оотсутствует точка-входа для передачи данных по числу пользователей онлайн в социальной сети

    @Override
    @PostMapping("callback/post_info")
    public void setTimeSeriesofPost(@RequestBody DataList<InformationOfPost> data) {
        log.info("callback-update: information of post len: " + data.getSize());
        service.updateInformationOfPost(data.getData());
    }

    @Override
    @PostMapping("callback/group_info")
    public void setTimeSeriesOfGroup(@RequestBody DataList<InformationOfGroup> data) {
        log.info("callback-update: information of group len: " + data.getSize());
        service.updateInformationOfGroup(data.getData());
    }

    @GetMapping("time")
    public ZonedDateTime getTime(){
        return Util.timeUtc();
    }

    @GetMapping("all")
    public Iterable<PostInfo> getPostInfoAll() {
        return rep.findAll();
    }
}
