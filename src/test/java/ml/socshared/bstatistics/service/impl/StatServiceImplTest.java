package ml.socshared.bstatistics.service.impl;

import ml.socshared.bstatistics.config.Constants;
import ml.socshared.bstatistics.domain.db.GroupOnline;
import ml.socshared.bstatistics.domain.db.PostInfo;
import ml.socshared.bstatistics.domain.object.InformationOfPost;
import ml.socshared.bstatistics.domain.object.TimeSeries;
import ml.socshared.bstatistics.repository.GroupOnlineRepository;
import ml.socshared.bstatistics.repository.PostInfoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class StatServiceImplTest {

    StatServiceImpl service;
    GroupOnlineRepository groupInfoRep = Mockito.mock(GroupOnlineRepository.class);
    PostInfoRepository postInfoRepository = Mockito.mock(PostInfoRepository.class);
    List<GroupOnline> gl;
    List<Double> onlineValueOfDay;
    List<Double> onlineValueOfTime19or20;
    List<Double> onlineValueOfTime21;

    @BeforeEach
    public void cookData() {
        gl = new ArrayList<>();
        gl.add(new GroupOnline().setOnline(10)
                              .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 19, 0, 0,0,0, ZoneOffset.UTC)));
        gl.add(new GroupOnline().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 19, 1, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupOnline().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 19, 2, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupOnline().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 20, 0, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupOnline().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 20, 1, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupOnline().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 0, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupOnline().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 1, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupOnline().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 2, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupOnline().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 3, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupOnline().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 4, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupOnline().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 5, 0, 0, 0, ZoneOffset.UTC)));

        onlineValueOfDay = Arrays.asList(10.0, 10.0, 10.0);


    }


    @Test
    public void getOnlineByTimeTest() {
        ZonedDateTime begin1 = gl.get(0).getTimeAddedRecord();
        ZonedDateTime end1 = gl.get(gl.size()-1).getTimeAddedRecord();
        List<GroupOnline> returnOneDay = Arrays.asList(gl.get(0), gl.get(1), gl.get(2));
        Mockito.when(groupInfoRep.findBetweenDates(Mockito.any(), Mockito.eq(LocalDate.from(begin1)),
                Mockito.eq(LocalDate.from(begin1)))).thenReturn(returnOneDay);
        Mockito.when(groupInfoRep.findBetweenDates(Mockito.any(), Mockito.eq(LocalDate.from(begin1)),
                Mockito.eq(LocalDate.from(end1)))).thenReturn(gl);

        service = new StatServiceImpl(groupInfoRep, postInfoRepository);
        TimeSeries<Integer> res =  service.getOnlineByTime("111", LocalDate.from(begin1), LocalDate.from(begin1));

        Assertions.assertEquals(res.getData(), Arrays.asList(10, 10, 10));
        Assertions.assertEquals(res.getStep(), Duration.ofDays(1).dividedBy(Constants.NEWS_NUM_CHECK_PER_DAY));

        res =  service.getOnlineByTime("111", LocalDate.from(begin1), LocalDate.from(end1));

        Assertions.assertEquals(res.getData(), Arrays.asList(30, 20, 60));
        Assertions.assertEquals(res.getStep(), Duration.ofDays(1));
    }

    @Test
    public void updateInformationOfPostTestWhenOldInformationIsMissing() {

        Mockito.when(postInfoRepository.findPostInfoByGroupIdAndPostId(
                Mockito.anyString(), Mockito.anyString()
        ))
                .thenReturn(Collections.emptyList());
        ArgumentCaptor<PostInfo> args = ArgumentCaptor.forClass(PostInfo.class);
        service = new StatServiceImpl(groupInfoRep, postInfoRepository);

        InformationOfPost info = new InformationOfPost();
        info.setGroupId("111");
        info.setPostId("222");
        info.setComments(100);
        info.setLikes(101);
        info.setShares(102);
        info.setViews(103);
        info.setTime(Util.timeUtc());

        service.updateInformationOfPost(Collections.singletonList(info));
        Mockito.verify(postInfoRepository, Mockito.times(1)).save(args.capture());
        PostInfo result = args.getValue();
        Assertions.assertEquals(info.getGroupId(), result.getGroupId());
        Assertions.assertEquals(info.getPostId(), result.getPostId());
        Assertions.assertEquals(info.getComments(), result.getComments());
        Assertions.assertEquals(info.getLikes(), result.getLikes());
        Assertions.assertEquals(info.getShares(), result.getShare());
        Assertions.assertEquals(info.getViews(), result.getViews());
        Assertions.assertEquals(info.getTime(), result.getDateAddedRecord());
    }

    @Test
    public void  updateInformationOfPostTestWhenDbHaveData() {
        final String groupId = "111";
        final String postId  = "222";
        final ZonedDateTime time1 = ZonedDateTime.now(ZoneOffset.UTC);
        final ZonedDateTime time2 = ZonedDateTime.now(ZoneOffset.UTC).plusHours(2);
        final ZonedDateTime time3 = ZonedDateTime.now(ZoneOffset.UTC).plusHours(4);
        PostInfo info1 = new PostInfo();
        PostInfo info2 = new PostInfo();
        info1.setGroupId(groupId);
        info1.setPostId(postId);
        info1.setLikes(1);
        info1.setShare(2);
        info1.setComments(3);
        info1.setViews(4);
        info1.setDateAddedRecord(time1);

        info2.setGroupId(groupId);
        info2.setPostId(postId);
        info2.setLikes(5);
        info2.setShare(6);
        info2.setComments(7);
        info2.setViews(8);
        info2.setDateAddedRecord(time2);

        Mockito.when(postInfoRepository.findPostInfoByGroupIdAndPostId(
                Mockito.anyString(), Mockito.anyString()
        ))
                .thenReturn(Arrays.asList(info1, info2));
        ArgumentCaptor<PostInfo> args = ArgumentCaptor.forClass(PostInfo.class);
        service = new StatServiceImpl(groupInfoRep, postInfoRepository);

        InformationOfPost postInfo = new InformationOfPost();
        postInfo.setGroupId(groupId);
        postInfo.setPostId(postId);
        postInfo.setLikes(10);
        postInfo.setShares(11);
        postInfo.setComments(13);
        postInfo.setViews(14);
        postInfo.setTime(time3);

        service.updateInformationOfPost(Collections.singletonList(postInfo));

        Mockito.verify(postInfoRepository, Mockito.times(1)).save(args.capture());
        PostInfo result = args.getValue();
        Assertions.assertEquals(4, result.getLikes());
        Assertions.assertEquals(3, result.getShare());
        Assertions.assertEquals(3, result.getComments());
        Assertions.assertEquals(2, result.getViews());
        Assertions.assertEquals(time3, result.getDateAddedRecord());
    }
}