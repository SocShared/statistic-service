package ml.socshared.bstatistics.controller;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.bstatistics.api.v1.BStatisticApi;
import ml.socshared.bstatistics.domain.object.*;
import ml.socshared.bstatistics.domain.response.GroupInfoResponse;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;
import ml.socshared.bstatistics.repository.PostInfoRepository;
import ml.socshared.bstatistics.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

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





    @Override
    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("private/social/{soc}/groups/{groupId}/subscribers/variability")
    public TimeSeries<Integer> getVariabilitySubscribersOfGroup(@PathVariable UUID groupId,
                                                                @PathVariable SocialNetwork soc,
                                                                @RequestParam Long begin,
                                                                @RequestParam Long end) {
        log.info("get time series of group (GroupID: " + groupId +") subscribers");
        //return service.getVariabilitySubscribers(groupId, LocalDate.from(Instant.ofEpochSecond( begin)),
        // LocalDate.from(Instant.ofEpochSecond( end)));
        return null;
    }


    @Override
    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("private/social/{soc}/groups/{systemGroupId}/posts/{sytemPostId}/time_series")
    public PostInfoByTime getInfoVariabilityByTimeOfPost(@PathVariable UUID systemGroupId,
                                                         @PathVariable UUID systemPostId,
                                                         @PathVariable SocialNetwork soc,
                                                         @RequestParam(name="begin") Long begin,
                                                         @RequestParam(name="end") Long end) {
        log.info("request on get info by time of post (Social: {}; GroupId: {}; PostId: {})", soc, systemGroupId, systemPostId);
        return service.getPostInfoByTime(systemGroupId, systemPostId, soc,LocalDate.ofInstant(Instant.ofEpochSecond( begin), ZoneOffset.UTC),
                LocalDate.ofInstant(Instant.ofEpochSecond( end), ZoneOffset.UTC));
    }


    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("private/social/{soc}/groups/{groupId}/info")
    public GroupInfoResponse getGroupInfo(@PathVariable UUID systemGroupId, @PathVariable SocialNetwork soc,
                                          @RequestParam(name = "begin") Long begin,
                                          @RequestParam(name = "end") Long end) {
        log.info("request to get group info");
        return service.getGroupInfoByTime(systemGroupId, soc, Instant.ofEpochSecond(begin).atZone(ZoneOffset.UTC).toLocalDate(),
                                          Instant.ofEpochSecond(end).atZone(ZoneOffset.UTC).toLocalDate());

    }

    @Override
    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("private/social/{soc}/groups/{groupId}/posts/{postId}/summary")
    public PostSummary getPostInfo(@PathVariable UUID systemGroupId, @PathVariable UUID systemPostId,
                                   @PathVariable SocialNetwork soc) {
        log.info("request on get info of post (SocialNetwork: {}; GroupId: {}; PostId: {})", soc, systemGroupId, systemPostId);
        return service.getPostSummary(systemPostId, systemGroupId, soc);
    }

    @Override
    public void setTimeSeriesofPost(DataList<InformationOfPost> data) {

    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("private/time")
    public LocalDate getTime(){
        return LocalDate.now();
    }

}
