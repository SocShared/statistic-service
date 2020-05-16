package ml.socshared.bstatistics.service.impl;

import ml.socshared.bstatistics.config.Constants;
import ml.socshared.bstatistics.domain.db.GroupInfo;
import ml.socshared.bstatistics.domain.db.PostInfo;
import ml.socshared.bstatistics.domain.object.*;
import ml.socshared.bstatistics.exception.HttpIllegalBodyRequest;
import ml.socshared.bstatistics.exception.HttpNotFoundException;
import ml.socshared.bstatistics.repository.GroupOnlineRepository;
import ml.socshared.bstatistics.repository.PostInfoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.*;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class StatServiceImplTest {

    StatServiceImpl service;
    GroupOnlineRepository groupInfoRep = Mockito.mock(GroupOnlineRepository.class);
    PostInfoRepository postInfoRepository = Mockito.mock(PostInfoRepository.class);
    List<GroupInfo> gl;
    List<Double> onlineValueOfDay;
    List<Double> onlineValueOfTime19or20;
    List<Double> onlineValueOfTime21;

    @BeforeEach
    public void cookData() {
        gl = new ArrayList<>();
        gl.add(new GroupInfo().setOnline(10)
                              .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 19, 0, 0,0,0, ZoneOffset.UTC)));
        gl.add(new GroupInfo().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 19, 1, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupInfo().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 19, 2, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupInfo().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 20, 0, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupInfo().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 20, 1, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupInfo().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 0, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupInfo().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 1, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupInfo().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 2, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupInfo().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 3, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupInfo().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 4, 0, 0, 0, ZoneOffset.UTC)));
        gl.add(new GroupInfo().setOnline(10)
                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 5, 0, 0, 0, ZoneOffset.UTC)));

        onlineValueOfDay = Arrays.asList(10.0, 10.0, 10.0);


    }


    @Test
    public void getOnlineByTimeTest() {
        ZonedDateTime begin1 = gl.get(0).getTimeAddedRecord();
        ZonedDateTime end1 = gl.get(gl.size()-1).getTimeAddedRecord();
        List<GroupInfo> returnOneDay = Arrays.asList(gl.get(0), gl.get(1), gl.get(2));
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
    public void getOnlineByTimeTestNotFoundGroup() {
        Mockito.when(groupInfoRep.findBetweenDates(
                Mockito.anyString(), Mockito.any(), Mockito.any()
        ))
                .thenReturn(Collections.emptyList());
        service = new StatServiceImpl(groupInfoRep, postInfoRepository);

        Assertions.assertThrows(HttpNotFoundException.class,
                ()-> service.getOnlineByTime("1", LocalDate.now(), LocalDate.now()));
    }

    @Test
    public void getPostInfoByTimeTestByOneDay() {

        List<PostInfo> postInfo = Arrays.asList(
                new PostInfo(1, "1", "1", ZonedDateTime.now(), 1, 1, 1, 1),
                new PostInfo(1, "1", "1", ZonedDateTime.now(), 2, 2, 2, 2),
                new PostInfo(1, "1", "1", ZonedDateTime.now(), 3, 3, 3, 3)
        );

        Mockito.when(postInfoRepository.findPostInfoByPeriod(
                Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any()
        ))
                .thenReturn(postInfo);
        service = new StatServiceImpl(groupInfoRep, postInfoRepository);
        PostInfoByTime info = service.getPostInfoByTime("1", "1", LocalDate.now(), LocalDate.now());

        Assertions.assertEquals(postInfo.size(), info.getVariabilityNumberViews().getSize());
        Assertions.assertEquals(postInfo.size(), info.getVariabilityNumberViews().getData().size());
        Assertions.assertEquals(postInfo.size(), info.getVariabilityNumberShares().getSize());
        Assertions.assertEquals(postInfo.size(), info.getVariabilityNumberShares().getData().size());
        Assertions.assertEquals(postInfo.size(), info.getVariabilityNumberLikes().getSize());
        Assertions.assertEquals(postInfo.size(), info.getVariabilityNumberLikes().getData().size());
        Assertions.assertEquals(postInfo.size(), info.getVariabilityNumberComments().getSize());
        Assertions.assertEquals(postInfo.size(), info.getVariabilityNumberComments().getData().size());

        for(int i = 0; i < postInfo.size(); i++) {
            Assertions.assertEquals(postInfo.get(i).getViews(), info.getVariabilityNumberViews().getData().get(i));
            Assertions.assertEquals(postInfo.get(i).getShare(), info.getVariabilityNumberShares().getData().get(i));
            Assertions.assertEquals(postInfo.get(i).getLikes(), info.getVariabilityNumberLikes().getData().get(i));
            Assertions.assertEquals(postInfo.get(i).getComments(), info.getVariabilityNumberComments().getData().get(i));
        }
    }

    @Test
    public void getPostInfoByTimeTestByFewDay() {

        List<PostInfo> postInfo = Arrays.asList(
                new PostInfo(1, "1", "1", ZonedDateTime.now().minusDays(2), 1, 2, 3, 4),
                new PostInfo(1, "1", "1", ZonedDateTime.now().minusDays(2), 2, 2, 2, 2),
                new PostInfo(1, "1", "1", ZonedDateTime.now().minusDays(2), 3, 3, 3, 3),
                new PostInfo(1, "1", "1", ZonedDateTime.now().minusDays(1), 1, 2, 3, 4),
                new PostInfo(1, "1", "1", ZonedDateTime.now().minusDays(1), 2, 2, 2, 2),
                new PostInfo(1, "1", "1", ZonedDateTime.now().minusDays(1), 3, 3, 3, 3),
                new PostInfo(1, "1", "1", ZonedDateTime.now(), 1, 2, 3, 4),
                new PostInfo(1, "1", "1", ZonedDateTime.now(), 2, 2, 2, 2),
                new PostInfo(1, "1", "1", ZonedDateTime.now(), 3, 3, 3, 3)
        );

        Mockito.when(postInfoRepository.findPostInfoByPeriod(
                Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any()
        ))
                .thenReturn(postInfo);
        service = new StatServiceImpl(groupInfoRep, postInfoRepository);
        PostInfoByTime info = service.getPostInfoByTime("1", "1", LocalDate.now().minusDays(2), LocalDate.now());

        Assertions.assertEquals(3, info.getVariabilityNumberViews().getSize());
        Assertions.assertEquals(3, info.getVariabilityNumberViews().getData().size());
        Assertions.assertEquals(3, info.getVariabilityNumberShares().getSize());
        Assertions.assertEquals(3, info.getVariabilityNumberShares().getData().size());
        Assertions.assertEquals(3, info.getVariabilityNumberLikes().getSize());
        Assertions.assertEquals(3, info.getVariabilityNumberLikes().getData().size());
        Assertions.assertEquals(3, info.getVariabilityNumberComments().getSize());
        Assertions.assertEquals(3, info.getVariabilityNumberComments().getData().size());

        for(int i = 0; i < 3; i++) {
            Assertions.assertEquals(6, info.getVariabilityNumberViews().getData().get(i));
            Assertions.assertEquals(7, info.getVariabilityNumberShares().getData().get(i));
            Assertions.assertEquals(8, info.getVariabilityNumberLikes().getData().get(i));
            Assertions.assertEquals(9, info.getVariabilityNumberComments().getData().get(i));
        }

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


    @Test
    public void getPostSummaryFoundData() {
        List<PostInfo> value = Arrays.asList(
                new PostInfo(1, "1", "2", ZonedDateTime.now(ZoneOffset.UTC),
                        20, 5, 12, 3),
                new PostInfo(2, "1", "2", ZonedDateTime.now(ZoneOffset.UTC),
                        12, 3, 4, 5),
                new PostInfo(1, "1", "2", ZonedDateTime.now(ZoneOffset.UTC),
                        50, 20, 30, 25));


        Mockito.when(postInfoRepository.findPostInfoByGroupIdAndPostId(
            Mockito.eq("1"), Mockito.eq("2")
    )).thenReturn(value);

        service = new StatServiceImpl(groupInfoRep, postInfoRepository);

        PostSummary res = service.getPostSummary("1", "2");

        Assertions.assertEquals( "1", res.getGroupId());
        Assertions.assertEquals( "2", res.getPostId());
        Assertions.assertEquals( 82, res.getNumberViews());
        Assertions.assertEquals( 28, res.getNumberReposts());
        Assertions.assertEquals( 46, res.getNumberLikes());
        Assertions.assertEquals( 33, res.getNumberComments());
        Assertions.assertEquals(1.3049 , res.getEngagementRate(), 0.0001);

    }

    @Test
    public void getPostSummaryNotFoundData() {
        Mockito.when(postInfoRepository.findPostInfoByGroupIdAndPostId(
                Mockito.anyString(), Mockito.anyString()
        )).thenReturn(Collections.emptyList());

        service = new StatServiceImpl(groupInfoRep, postInfoRepository);
        Assertions.assertThrows( HttpNotFoundException.class, ()-> service.getPostSummary("1", "2"));
    }

    @Test
    public void getPostInfoByTimeTestNotFound() {

        Mockito.when(postInfoRepository.findPostInfoByGroupIdAndPostId(
                Mockito.anyString(), Mockito.anyString()
        )).thenReturn(Collections.emptyList());

        service = new StatServiceImpl(groupInfoRep, postInfoRepository);
        Assertions.assertThrows(HttpNotFoundException.class,
                () ->service.getPostInfoByTime("1", "2", LocalDate.now(), LocalDate.now()));
    }


    @Test
    public void updateInformationOfGroupTestDbDontContainInfo() {
        Mockito.when(groupInfoRep.getOldestTimeOfRecord(
                Mockito.anyString()
        ))
                .thenReturn(Optional.empty());
        ArgumentCaptor<GroupInfo> args = ArgumentCaptor.forClass(GroupInfo.class);
        service = new StatServiceImpl(groupInfoRep, postInfoRepository);

        List<InformationOfGroup> data = Arrays.asList(
                new InformationOfGroup("1", 100, 150, LocalDateTime.now()),
                new InformationOfGroup("2", 95, 150, LocalDateTime.now()),
                new InformationOfGroup("3", 450, 500, LocalDateTime.now())
        );


        service.updateInformationOfGroup(data);
        Mockito.verify(groupInfoRep, times(3)).save(args.capture());
        List<GroupInfo> res = args.getAllValues();

        Assertions.assertEquals(3, res.size());
        Assertions.assertEquals(100, res.get(0).getOnline());
        Assertions.assertEquals(150, res.get(0).getSubscribers());
        Assertions.assertEquals(95, res.get(1).getOnline());
        Assertions.assertEquals(150, res.get(1).getSubscribers());
        Assertions.assertEquals(450, res.get(2).getOnline());
        Assertions.assertEquals(500, res.get(2).getSubscribers());
    }

    @Test
    public void updateInformationOfGroupTestDbContainInfo() {

        Mockito.when(groupInfoRep.getNumberSubscribers(
                Mockito.eq("1")
        )).thenReturn(Optional.of(500L));
        Mockito.when(groupInfoRep.getNumberSubscribers(
                Mockito.eq("2")
        )).thenReturn(Optional.of(100L));
        Mockito.when(groupInfoRep.getNumberSubscribers(
                Mockito.eq("3")
        )).thenReturn(Optional.of(150L));

        Mockito.when(groupInfoRep.getOldestTimeOfRecord(
                Mockito.anyString()
        ))
                .thenReturn(Optional.of(new OldestTimeRecord(ZonedDateTime.now().minusMinutes(15))));
        ArgumentCaptor<GroupInfo> args = ArgumentCaptor.forClass(GroupInfo.class);
        service = new StatServiceImpl(groupInfoRep, postInfoRepository);

        List<InformationOfGroup> data = Arrays.asList(
                new InformationOfGroup("1", 100, 560, LocalDateTime.now()),
                new InformationOfGroup("2", 100, 100, LocalDateTime.now()),
                new InformationOfGroup("3", 100, 200, LocalDateTime.now())
        );


        service.updateInformationOfGroup(data);
        Mockito.verify(groupInfoRep, times(3)).save(args.capture());
        List<GroupInfo> res = args.getAllValues();

        Assertions.assertEquals(3, res.size());
        Assertions.assertEquals(100, res.get(0).getOnline());
        Assertions.assertEquals(60, res.get(0).getSubscribers());
        Assertions.assertEquals(100, res.get(1).getOnline());
        Assertions.assertEquals(0, res.get(1).getSubscribers());
        Assertions.assertEquals(100, res.get(2).getOnline());
        Assertions.assertEquals(50, res.get(2).getSubscribers());
    }

    @Test
    public void updateInformationOfGroupTestSetOldestRecord() {
        Mockito.when(groupInfoRep.getOldestTimeOfRecord(
                Mockito.any()
        ))
                .thenReturn(Optional.of(new OldestTimeRecord(ZonedDateTime.now().minusMinutes(15))));

        service = new StatServiceImpl(groupInfoRep, postInfoRepository);

        Assertions.assertThrows(HttpIllegalBodyRequest.class,
                () -> service.updateInformationOfGroup(Arrays.asList(
                        new InformationOfGroup("1", 100, 100, LocalDateTime.now()),
                        new InformationOfGroup("1", 100, 100, LocalDateTime.now().minusMinutes(30))
                )));
    }

}