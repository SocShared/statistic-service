package ml.socshared.bstatistics.repository;

import ml.socshared.bstatistics.domain.db.Post;
import ml.socshared.bstatistics.domain.db.PostId;
import ml.socshared.bstatistics.domain.db.PostInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

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
//        PostInfo info = new PostInfo();
//        Post post = new Post(new PostId(groupId1, postId), 1, 1, 1, 1);
//        post =  postRep.save(post);
//        info.setDateAddedRecord(ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));
//        info.setPost(post);
//        info.setViews(1);
//        info.setComments(2);
//        info.setShare(3);
//        info.setLikes(4);
//        infoRep.save(info);
//        info = new PostInfo();
//       info.setPost(post);
//        info.setDateAddedRecord(ZonedDateTime.of(2020, 1, 2, 0, 0, 0, 0, ZoneOffset.UTC));
//        info.setViews(5);
//        info.setComments(6);
//        info.setShare(7);
//        info.setLikes(8);
//        infoRep.save(info);
//        info = new PostInfo();
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
//        List<PostInfo> res = infoRep.findPostInfoByPeriod(groupId1, postId, ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
//                ZonedDateTime.of(2020, 1, 5, 0, 0, 0, 0, ZoneOffset.UTC));
////        List<PostInfo> res = repository.findPostInfoByPeriod2(groupId1);
//        Assertions.assertEquals(2, res.size());
//    }
//}