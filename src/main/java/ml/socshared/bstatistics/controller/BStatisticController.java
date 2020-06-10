package ml.socshared.bstatistics.controller;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.bstatistics.api.v1.BStatisticApi;
import ml.socshared.bstatistics.domain.db.GroupInfo;
import ml.socshared.bstatistics.domain.object.*;
import ml.socshared.bstatistics.domain.response.GroupInfoResponse;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;
import ml.socshared.bstatistics.repository.PostInfoRepository;
import ml.socshared.bstatistics.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.*;

@RestController
@RequestMapping("api/v1/")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class BStatisticController implements BStatisticApi {

    private StatService service;
    @Autowired
    private PostInfoRepository rep;

    @Autowired
    BStatisticController(StatService s) {
        service = s;
    }



//    @Override
//    @PreAuthorize("hasRole('SERVICE')")
//    @GetMapping("private/social/{soc}/groups/{groupId}/online/time_series")
//    public TimeSeries<Integer> getGroupOnline(@PathVariable String groupId,
//                                              @PathVariable SocialNetwork soc,
//                                              @RequestParam(name="begin") Long begin,
//                                              @RequestParam(name="end") Long end) {
//        log.info("Get online of group");
//        return service.getOnlineByTime(groupId, soc,
//                LocalDate.from(Instant.ofEpochSecond( begin)), LocalDate.from(Instant.ofEpochSecond(end)));
//    }

    @Override
    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("private/social/{soc}/groups/{groupId}/subscribers/variability")
    public TimeSeries<Integer> getVariabilitySubscribersOfGroup(@PathVariable String groupId,
                                                                @PathVariable SocialNetwork soc,
                                                                @RequestParam Long begin,
                                                                @RequestParam Long end) {
        log.info("get time series of group (GroupID: " + groupId +") subscribers");
        //return service.getVariabilitySubscribers(groupId, LocalDate.from(Instant.ofEpochSecond( begin)),
        // LocalDate.from(Instant.ofEpochSecond( end)));
        return null;
    }
//
//    @Override
//    @PreAuthorize("hasRole('SERVICE')")
//    @GetMapping("private/social/{soc}/groups/{groupId}/subscribers")
//    public GroupInfo getNumberSubscribersOfGroup(@PathVariable  String groupId, @PathVariable SocialNetwork soc) {
//       log.info("request on get short information of group by id: " + groupId);
//        return service.getGroupSubscribers(groupId, soc);
//    }

    @Override
    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("private/social/{soc}/groups/{groupId}/posts/{postId}/time_series")
    public PostInfoByTime getInfoVariabilityByTimeOfPost(@PathVariable String groupId,
                                                         @PathVariable String postId,
                                                         @PathVariable SocialNetwork soc,
                                                         @RequestParam(name="begin") Long begin,
                                                         @RequestParam(name="end") Long end) {
        log.info("request on get info by time of post (GroupId: " + groupId + "; PostId: " + postId + ")");
        return service.getPostInfoByTime(groupId, postId, soc,LocalDate.ofInstant(Instant.ofEpochSecond( begin), ZoneOffset.UTC),
                LocalDate.ofInstant(Instant.ofEpochSecond( end), ZoneOffset.UTC));
    }


    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("private/social/{soc}/groups/{groupId}/info")
    public GroupInfoResponse getGroupInfo(@PathVariable String groupId, @PathVariable SocialNetwork soc,
                                          @RequestParam(name = "begin") Long begin,
                                          @RequestParam(name = "end") Long end) {
        log.info("request to get group info");
        return service.getGroupInfoByTime(groupId, soc, Instant.ofEpochSecond(begin).atZone(ZoneOffset.UTC).toLocalDate(),
                                          Instant.ofEpochSecond(end).atZone(ZoneOffset.UTC).toLocalDate());

    }

    @Override
    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("private/social/{soc}/groups/{groupId}/posts/{postId}/summary")
    public PostSummary getPostInfo(@PathVariable String groupId, @PathVariable String postId,
                                   @PathVariable SocialNetwork soc) {
        log.info("request on get info of post (GroupId: " + groupId + "; PostId: " + postId + ")");
        return service.getPostSummary(groupId, postId, soc);
    }

    @Override
    public void setTimeSeriesofPost(DataList<InformationOfPost> data) {

    }

    //TODO оотсутствует точка-входа для передачи данных по числу пользователей онлайн в социальной сети

//    @Override
//    @PreAuthorize("hasRole('SERVICE')")
//    @PostMapping("private/callback/post_info")
//    public void setTimeSeriesofPost(@RequestBody DataList<InformationOfPost> data) {
//        log.info("callback-update: information of post len: " + data.getSize());
//        service.updateInformationOfPost(data.getData());
//    }

//    @Override
//    @PreAuthorize("hasRole('SERVICE')")
//    @PostMapping("private/callback/group_info")
//    public void setTimeSeriesOfGroup(@RequestBody DataList<InformationOfGroup> data) {
//        log.info("callback-update: information of group len: " + data.getSize());
//        service.updateInformationOfGroup(data.getData());
//    }
    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("private/time")
    public LocalDate getTime(){
        return LocalDate.now();
    }

//    @GetMapping("all")
//    public Iterable<PostInfo> getPostInfoAll() {
//        return rep.findAll();
//    }
}
