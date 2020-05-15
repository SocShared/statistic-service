package ml.socshared.bstatistics.controller;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.bstatistics.api.v1.BStatisticApi;
import ml.socshared.bstatistics.domain.db.PostInfo;
import ml.socshared.bstatistics.domain.object.PostInfoByTime;
import ml.socshared.bstatistics.domain.object.PostSummary;
import ml.socshared.bstatistics.domain.object.TimeSeries;
import ml.socshared.bstatistics.domain.object.InformationOfPost;
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
    @GetMapping("groups/{groupId}/estimate/online")
    public TimeSeries<Integer> getOnline(@PathVariable String groupId,
                                        @RequestParam(name="begin") LocalDate begin,
                                        @RequestParam(name="end") LocalDate end) {
        log.info("Get online of group");
        return service.getOnlineByTime(groupId, begin, end);
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
    @PostMapping("callback")
    public void setTimeSeries(@RequestBody List<InformationOfPost> data) {
        log.info("callback-update");
        service.updateInformationOfPost(data);
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
