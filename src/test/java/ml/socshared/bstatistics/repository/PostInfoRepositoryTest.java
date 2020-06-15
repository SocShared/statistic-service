package ml.socshared.bstatistics.repository;

import ml.socshared.bstatistics.domain.db.Post;

//
//@DataJpaTest
//@ActiveProfiles("test")
//public class PostInfoRepositoryTest{
//
//    @Resource
//    PostInfoRepository infoRep;
//    @Resource
//    PostRepository postRep;
//    final String groupId1 = "1112";
//    final String groupId2 = "147";
//    final String postId = "897";
//
//    @BeforeEach
//    public void StartUp() {
//
//        POST_INFO info = new POST_INFO();
//        Post post = new Post(new PostId(groupId1, postId), 1, 1, 1, 1);
//        post =  postRep.save(post);
//        info.setDateAddedRecord(ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));
//        info.setPost(post);
//        info.setViews(1);
//        info.setComments(2);
//        info.setShare(3);
//        info.setLikes(4);
//        infoRep.save(info);
//        info = new POST_INFO();
//       info.setPost(post);
//        info.setDateAddedRecord(ZonedDateTime.of(2020, 1, 2, 0, 0, 0, 0, ZoneOffset.UTC));
//        info.setViews(5);
//        info.setComments(6);
//        info.setShare(7);
//        info.setLikes(8);
//        infoRep.save(info);
//        info = new POST_INFO();
//        post = new Post(new PostId(groupId2, postId), 2, 2, 2, 2);
//        post = postRep.save(post);
//        info.setPost(post);
//        info.setDateAddedRecord(ZonedDateTime.of(2020, 1, 3, 0, 0, 0, 0, ZoneOffset.UTC));
//        info.setViews(9);
//        info.setComments(10);
//        info.setShare(11);
//        info.setLikes(12);
//        infoRep.save(info);
//
//    }
//
//
//    @Test
//    public void findPostInfoByPeriod() {
//        List<POST_INFO> res = infoRep.findPostInfoByPeriod(groupId1, postId, ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
//                ZonedDateTime.of(2020, 1, 5, 0, 0, 0, 0, ZoneOffset.UTC));
////        List<POST_INFO> res = repository.findPostInfoByPeriod2(groupId1);
//        Assertions.assertEquals(2, res.size());
//    }
//}