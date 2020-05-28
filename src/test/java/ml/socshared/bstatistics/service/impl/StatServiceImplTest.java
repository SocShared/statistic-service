//package ml.socshared.bstatistics.service.impl;
//
//
//import ml.socshared.bstatistics.config.Constants;
//import ml.socshared.bstatistics.domain.db.*;
//import ml.socshared.bstatistics.domain.object.*;
//import ml.socshared.bstatistics.exception.HttpIllegalBodyRequest;
//import ml.socshared.bstatistics.exception.HttpNotFoundException;
//import ml.socshared.bstatistics.repository.GroupInfoRepository;
//import ml.socshared.bstatistics.repository.GroupRepository;
//import ml.socshared.bstatistics.repository.PostInfoRepository;
//import ml.socshared.bstatistics.repository.PostRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mockito;
//
//import java.time.*;
//import java.util.*;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//
//public class StatServiceImplTest {
//
//    StatServiceImpl service;
//    GroupInfoRepository groupInfoRep = Mockito.mock(GroupInfoRepository.class);
//    PostInfoRepository postInfoRep = Mockito.mock(PostInfoRepository.class);
//    GroupRepository groupRep = Mockito.mock(GroupRepository.class);
//    PostRepository postRep = Mockito.mock(PostRepository.class);
//
//    List<GroupInfo> gl;
//    List<Double> onlineValueOfDay;
//    List<Double> onlineValueOfTime19or20;
//    List<Double> onlineValueOfTime21;
//
//    public StatServiceImpl makeService() {
//        return new StatServiceImpl(groupInfoRep, groupRep, postInfoRep, postRep);
//    }
//
//    @BeforeEach
//    public void cookData() {
//        gl = new ArrayList<>();
//        gl.add(new GroupInfo().setOnline(10)
//                              .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 19, 0, 0,0,0, ZoneOffset.UTC)));
//        gl.add(new GroupInfo().setOnline(10)
//                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 19, 1, 0, 0, 0, ZoneOffset.UTC)));
//        gl.add(new GroupInfo().setOnline(10)
//                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 19, 2, 0, 0, 0, ZoneOffset.UTC)));
//        gl.add(new GroupInfo().setOnline(10)
//                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 20, 0, 0, 0, 0, ZoneOffset.UTC)));
//        gl.add(new GroupInfo().setOnline(10)
//                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 20, 1, 0, 0, 0, ZoneOffset.UTC)));
//        gl.add(new GroupInfo().setOnline(10)
//                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 0, 0, 0, 0, ZoneOffset.UTC)));
//        gl.add(new GroupInfo().setOnline(10)
//                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 1, 0, 0, 0, ZoneOffset.UTC)));
//        gl.add(new GroupInfo().setOnline(10)
//                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 2, 0, 0, 0, ZoneOffset.UTC)));
//        gl.add(new GroupInfo().setOnline(10)
//                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 3, 0, 0, 0, ZoneOffset.UTC)));
//        gl.add(new GroupInfo().setOnline(10)
//                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 4, 0, 0, 0, ZoneOffset.UTC)));
//        gl.add(new GroupInfo().setOnline(10)
//                .setTimeAddedRecord(ZonedDateTime.of(2020, 5, 21, 5, 0, 0, 0, ZoneOffset.UTC)));
//
//        onlineValueOfDay = Arrays.asList(10.0, 10.0, 10.0);
//
//
//    }
//
//
//    @Test
//    public void getOnlineByTimeTest() {
//        ZonedDateTime begin1 = gl.get(0).getTimeAddedRecord();
//        ZonedDateTime end1 = gl.get(gl.size()-1).getTimeAddedRecord();
//        List<GroupInfo> returnOneDay = Arrays.asList(gl.get(0), gl.get(1), gl.get(2));
//        Mockito.when(groupInfoRep.findBetweenDates(Mockito.any(), Mockito.eq(LocalDate.from(begin1)),
//                Mockito.eq(LocalDate.from(begin1)))).thenReturn(returnOneDay);
//        Mockito.when(groupInfoRep.findBetweenDates(Mockito.any(), Mockito.eq(LocalDate.from(begin1)),
//                Mockito.eq(LocalDate.from(end1)))).thenReturn(gl);
//
//        service = makeService();
//        TimeSeries<Integer> res =  service.getOnlineByTime("111", LocalDate.from(begin1), LocalDate.from(begin1));
//
//        Assertions.assertEquals(res.getData(), Arrays.asList(10, 10, 10));
//        Assertions.assertEquals(res.getStep(), Duration.ofDays(1).dividedBy(Constants.NEWS_NUM_CHECK_PER_DAY));
//
//        res =  service.getOnlineByTime("111", LocalDate.from(begin1), LocalDate.from(end1));
//
//        Assertions.assertEquals(res.getData(), Arrays.asList(30, 20, 60));
//        Assertions.assertEquals(res.getStep(), Duration.ofDays(1));
//    }
//
//    @Test
//    public void getOnlineByTimeTestNotFoundGroup() {
//        Mockito.when(groupInfoRep.findBetweenDates(
//                Mockito.anyString(), Mockito.any(), Mockito.any()
//        ))
//                .thenReturn(Collections.emptyList());
//        service = new StatServiceImpl(groupInfoRep, groupRep, postInfoRep, postRep);
//
//        Assertions.assertThrows(HttpNotFoundException.class,
//                ()-> service.getOnlineByTime("1", LocalDate.now(), LocalDate.now()));
//    }
//
//    @Test
//    public void getPostInfoByTimeTestByOneDay() {
//        Post post = new Post(new PostId("1", "1"), 100, 100, 100, 100);
//        List<PostInfo> postInfo = Arrays.asList(
//                new PostInfo(1, post, ZonedDateTime.now(), 1, 1, 1, 1),
//                new PostInfo(1, post, ZonedDateTime.now(), 2, 2, 2, 2),
//                new PostInfo(1, post, ZonedDateTime.now(), 3, 3, 3, 3)
//        );
//
//        Mockito.when(postInfoRep.findPostInfoByPeriod(
//                Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any()
//        ))
//                .thenReturn(postInfo);
//
//        service = makeService();
//
//        PostInfoByTime info = service.getPostInfoByTime("1", "1", LocalDate.now(), LocalDate.now());
//
//        Assertions.assertEquals(postInfo.size(), info.getVariabilityNumberViews().getSize());
//        Assertions.assertEquals(postInfo.size(), info.getVariabilityNumberViews().getData().size());
//        Assertions.assertEquals(postInfo.size(), info.getVariabilityNumberShares().getSize());
//        Assertions.assertEquals(postInfo.size(), info.getVariabilityNumberShares().getData().size());
//        Assertions.assertEquals(postInfo.size(), info.getVariabilityNumberLikes().getSize());
//        Assertions.assertEquals(postInfo.size(), info.getVariabilityNumberLikes().getData().size());
//        Assertions.assertEquals(postInfo.size(), info.getVariabilityNumberComments().getSize());
//        Assertions.assertEquals(postInfo.size(), info.getVariabilityNumberComments().getData().size());
//
//        for(int i = 0; i < postInfo.size(); i++) {
//            Assertions.assertEquals(postInfo.get(i).getViews(), info.getVariabilityNumberViews().getData().get(i));
//            Assertions.assertEquals(postInfo.get(i).getShare(), info.getVariabilityNumberShares().getData().get(i));
//            Assertions.assertEquals(postInfo.get(i).getLikes(), info.getVariabilityNumberLikes().getData().get(i));
//            Assertions.assertEquals(postInfo.get(i).getComments(), info.getVariabilityNumberComments().getData().get(i));
//        }
//    }
//
//    @Test
//    public void getPostInfoByTimeTestByFewDay() {
//        Post post = new Post(new PostId("1", "1"), 100, 100, 100, 100);
//        List<PostInfo> postInfo = Arrays.asList(
//                new PostInfo(1, post, ZonedDateTime.now().minusDays(2), 1, 2, 3, 4),
//                new PostInfo(1, post, ZonedDateTime.now().minusDays(2), 2, 2, 2, 2),
//                new PostInfo(1, post, ZonedDateTime.now().minusDays(2), 3, 3, 3, 3),
//                new PostInfo(1, post, ZonedDateTime.now().minusDays(1), 1, 2, 3, 4),
//                new PostInfo(1, post, ZonedDateTime.now().minusDays(1), 2, 2, 2, 2),
//                new PostInfo(1, post, ZonedDateTime.now().minusDays(1), 3, 3, 3, 3),
//                new PostInfo(1, post, ZonedDateTime.now(), 1, 2, 3, 4),
//                new PostInfo(1, post, ZonedDateTime.now(), 2, 2, 2, 2),
//                new PostInfo(1, post, ZonedDateTime.now(), 3, 3, 3, 3)
//        );
//
//        Mockito.when(postInfoRep.findPostInfoByPeriod(
//                Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any()
//        ))
//                .thenReturn(postInfo);
//
//        service = makeService();
//
//        PostInfoByTime info = service.getPostInfoByTime("1", "1", LocalDate.now().minusDays(2), LocalDate.now());
//
//        Assertions.assertEquals(3, info.getVariabilityNumberViews().getSize());
//        Assertions.assertEquals(3, info.getVariabilityNumberViews().getData().size());
//        Assertions.assertEquals(3, info.getVariabilityNumberShares().getSize());
//        Assertions.assertEquals(3, info.getVariabilityNumberShares().getData().size());
//        Assertions.assertEquals(3, info.getVariabilityNumberLikes().getSize());
//        Assertions.assertEquals(3, info.getVariabilityNumberLikes().getData().size());
//        Assertions.assertEquals(3, info.getVariabilityNumberComments().getSize());
//        Assertions.assertEquals(3, info.getVariabilityNumberComments().getData().size());
//
//        for(int i = 0; i < 3; i++) {
//            Assertions.assertEquals(6, info.getVariabilityNumberViews().getData().get(i));
//            Assertions.assertEquals(7, info.getVariabilityNumberShares().getData().get(i));
//            Assertions.assertEquals(8, info.getVariabilityNumberLikes().getData().get(i));
//            Assertions.assertEquals(9, info.getVariabilityNumberComments().getData().get(i));
//        }
//
//    }
//
////    @Test
////    public void updateInformationOfPostTestWhenOldInformationIsMissing() {
////
////        Mockito.when(postInfoRep.findPostInfoByGroupIdAndPostId(
////                Mockito.anyString(), Mockito.anyString()
////        ))
////                .thenReturn(Collections.emptyList());
////        ArgumentCaptor<PostInfo> args = ArgumentCaptor.forClass(PostInfo.class);
////
////        service = makeService();
////
////        InformationOfPost info = new InformationOfPost();
////        info.setGroupId("111");
////        info.setPostId("222");
////        info.setComments(100);
////        info.setLikes(101);
////        info.setShares(102);
////        info.setViews(103);
////        info.setTime(Util.timeUtc());
////
////        service.updateInformationOfPost(Collections.singletonList(info));
////        Mockito.verify(postInfoRep, Mockito.times(1)).save(args.capture());
////        PostInfo result = args.getValue();
////        Assertions.assertEquals(info.getGroupId(), result.getPost().getId().getGroupId());
////        Assertions.assertEquals(info.getPostId(), result.getPost().getId().getPostId());
////        Assertions.assertEquals(info.getComments(), result.getComments());
////        Assertions.assertEquals(info.getLikes(), result.getLikes());
////        Assertions.assertEquals(info.getShares(), result.getShare());
////        Assertions.assertEquals(info.getViews(), result.getViews());
////        Assertions.assertEquals(info.getTime(), result.getDateAddedRecord());
////    }
//
////    @Test
////    public void  updateInformationOfPostTestWhenDbHaveData() {
////        final String groupId = "111";
////        final String postId  = "222";
////        final Post post = new Post(new PostId(groupId, postId), 12, 10, 8, 6);
////        final ZonedDateTime time1 = ZonedDateTime.now(ZoneOffset.UTC);
////        final ZonedDateTime time2 = ZonedDateTime.now(ZoneOffset.UTC).plusHours(2);
////        final ZonedDateTime time3 = ZonedDateTime.now(ZoneOffset.UTC).plusHours(4);
////        PostInfo info1 = new PostInfo();
////        PostInfo info2 = new PostInfo();
////        info1.setPost(post);
////        info1.setViews(4);
////        info1.setComments(3);
////        info1.setShare(2);
////        info1.setLikes(1);
////        info1.setDateAddedRecord(time1);
////
////        info2.setPost(post);
////        info2.setViews(8);
////        info2.setComments(7);
////        info2.setShare(6);
////        info2.setLikes(5);
////        info2.setDateAddedRecord(time2);
////
////
////        Mockito.when(postInfoRep.findPostInfoByGroupIdAndPostId(
////                Mockito.anyString(), Mockito.anyString()
////        ))
////                .thenReturn(Arrays.asList(info1, info2));
////        ArgumentCaptor<PostInfo> args = ArgumentCaptor.forClass(PostInfo.class);
////
////        Mockito.when(postRep.findById(
////                Mockito.any()
////        )).thenReturn(Optional.of(post));
////        service = makeService();
////
////        InformationOfPost postInfo = new InformationOfPost();
////        postInfo.setGroupId(groupId);
////        postInfo.setPostId(postId);
////        postInfo.setLikes(10);
////        postInfo.setShares(11);
////        postInfo.setComments(13);
////        postInfo.setViews(14);
////        postInfo.setTime(time3);
////
////        service.updateInformationOfPost(Collections.singletonList(postInfo));
////
////        Mockito.verify(postInfoRep, Mockito.times(1)).save(args.capture());
////        PostInfo result = args.getValue();
////        Assertions.assertEquals(4, result.getLikes());
////        Assertions.assertEquals(3, result.getShare());
////        Assertions.assertEquals(3, result.getComments());
////        Assertions.assertEquals(2, result.getViews());
////        Assertions.assertEquals(time3, result.getDateAddedRecord());
////    }
//
//
//    @Test
//    public void getPostSummaryFoundData() {
//        Post post = new Post(new PostId("1", "2"), 82, 33, 28, 46);
//
//        Mockito.when(postRep.findById(
//            Mockito.eq(new PostId("1", "2"))
//    )).thenReturn(Optional.of(post));
//
//        service = makeService();
//
//        PostSummary res = service.getPostSummary("1", "2");
//
//        Assertions.assertEquals( "1", res.getGroupId());
//        Assertions.assertEquals( "2", res.getPostId());
//        Assertions.assertEquals( 82, res.getNumberViews());
//        Assertions.assertEquals( 28, res.getNumberReposts());
//        Assertions.assertEquals( 46, res.getNumberLikes());
//        Assertions.assertEquals( 33, res.getNumberComments());
//        Assertions.assertEquals(1.3049 , res.getEngagementRate(), 0.0001);
//
//    }
//
//    @Test
//    public void getPostSummaryNotFoundData() {
//        Mockito.when(postInfoRep.findPostInfoByGroupIdAndPostId(
//                Mockito.anyString(), Mockito.anyString()
//        )).thenReturn(Collections.emptyList());
//
//        service = makeService();
//
//        Assertions.assertThrows( HttpNotFoundException.class, ()-> service.getPostSummary("1", "2"));
//    }
//
//    @Test
//    public void getPostInfoByTimeTestNotFound() {
//
//        Mockito.when(postInfoRep.findPostInfoByGroupIdAndPostId(
//                Mockito.anyString(), Mockito.anyString()
//        )).thenReturn(Collections.emptyList());
//
//        service = makeService();
//
//        Assertions.assertThrows(HttpNotFoundException.class,
//                () ->service.getPostInfoByTime("1", "2", LocalDate.now(), LocalDate.now()));
//    }
//
//
//    @Test
//    public void updateInformationOfGroupTestDbDontContainInfo() {
//        Mockito.when(groupInfoRep.getOldestTimeOfRecord(
//                Mockito.anyString()
//        ))
//                .thenReturn(Optional.empty());
//        ArgumentCaptor<GroupInfo> args = ArgumentCaptor.forClass(GroupInfo.class);
//
//        service = makeService();
//
//        List<InformationOfGroup> data = Arrays.asList(
//                new InformationOfGroup("1", 100, 150, LocalDateTime.now()),
//                new InformationOfGroup("2", 95, 150, LocalDateTime.now()),
//                new InformationOfGroup("3", 450, 500, LocalDateTime.now())
//        );
//
//
//        service.updateInformationOfGroup(data);
//        Mockito.verify(groupInfoRep, times(3)).save(args.capture());
//        List<GroupInfo> res = args.getAllValues();
//
//        Assertions.assertEquals(3, res.size());
//        Assertions.assertEquals(100, res.get(0).getOnline());
//        Assertions.assertEquals(150, res.get(0).getSubscribers());
//        Assertions.assertEquals(95, res.get(1).getOnline());
//        Assertions.assertEquals(150, res.get(1).getSubscribers());
//        Assertions.assertEquals(450, res.get(2).getOnline());
//        Assertions.assertEquals(500, res.get(2).getSubscribers());
//    }
//
//    @Test
//    public void updateInformationOfGroupTestDbContainInfo() {
//
//        Mockito.when(
//                groupRep.findById(Mockito.eq("1"))
//        )
//                .thenReturn(Optional.of(new Group("1", 500)));
//
//        Mockito.when(
//                groupRep.findById(Mockito.eq("2"))
//        )
//                .thenReturn(Optional.of(new Group("2", 100)));
//
//        Mockito.when(
//                groupRep.findById(Mockito.eq("3"))
//        )
//                .thenReturn(Optional.of(new Group("3", 150)));
//
//        Mockito.when(groupInfoRep.getOldestTimeOfRecord(
//                Mockito.anyString()
//        ))
//                .thenReturn(Optional.of(new OldestTimeRecord(ZonedDateTime.now().minusMinutes(15))));
//        ArgumentCaptor<GroupInfo> args = ArgumentCaptor.forClass(GroupInfo.class);
//
//        service = makeService();
//
//        List<InformationOfGroup> data = Arrays.asList(
//                new InformationOfGroup("1", 100, 560, LocalDateTime.now()),
//                new InformationOfGroup("2", 100, 100, LocalDateTime.now()),
//                new InformationOfGroup("3", 100, 200, LocalDateTime.now())
//        );
//
//
//        service.updateInformationOfGroup(data);
//
//        Mockito.verify(groupInfoRep, times(3)).save(args.capture());
//        List<GroupInfo> res = args.getAllValues();
//
//        Assertions.assertEquals(3, res.size());
//        Assertions.assertEquals(100, res.get(0).getOnline());
//        Assertions.assertEquals(60, res.get(0).getSubscribers());
//        Assertions.assertEquals(100, res.get(1).getOnline());
//        Assertions.assertEquals(0, res.get(1).getSubscribers());
//        Assertions.assertEquals(100, res.get(2).getOnline());
//        Assertions.assertEquals(50, res.get(2).getSubscribers());
//    }
//
//    @Test
//    public void updateInformationOfGroupTestSetOldestRecord() {
//        Mockito.when(groupInfoRep.getOldestTimeOfRecord(
//                Mockito.any()
//        ))
//                .thenReturn(Optional.of(new OldestTimeRecord(ZonedDateTime.now().minusMinutes(15))));
//
//        service = makeService();
//
//        Assertions.assertThrows(HttpIllegalBodyRequest.class,
//                () -> service.updateInformationOfGroup(Arrays.asList(
//                        new InformationOfGroup("1", 100, 100, LocalDateTime.now()),
//                        new InformationOfGroup("1", 100, 100, LocalDateTime.now().minusMinutes(30))
//                )));
//    }
//
//}