package ml.socshared.bstatistics.repository;

import ml.socshared.bstatistics.AbstractIntegrationTest;
import ml.socshared.bstatistics.domain.db.PostInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.Assert.*;


@DataJpaTest
@ActiveProfiles("test")
public class PostInfoRepositoryTest{

    @Resource
    PostInfoRepository repository;
    final String groupId1 = "1112";
    final String groupId2 = "147";
    final String postId = "897";

    @BeforeEach
    public void StartUp() {

        PostInfo info = new PostInfo();
        info.setDateAddedRecord(ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));
        info.setGroupId(groupId1);
        info.setPostId(postId);
        info.setViews(1);
        info.setComments(2);
        info.setShare(3);
        info.setLikes(4);
        repository.save(info);
        info = new PostInfo();
        info.setGroupId(groupId1);
        info.setPostId(postId);
        info.setDateAddedRecord(ZonedDateTime.of(2020, 1, 2, 0, 0, 0, 0, ZoneOffset.UTC));
        info.setViews(5);
        info.setComments(6);
        info.setShare(7);
        info.setLikes(8);
        repository.save(info);
        info = new PostInfo();
        info.setGroupId(groupId2);
        info.setPostId(postId);
        info.setDateAddedRecord(ZonedDateTime.of(2020, 1, 3, 0, 0, 0, 0, ZoneOffset.UTC));
        info.setViews(9);
        info.setComments(10);
        info.setShare(11);
        info.setLikes(12);
        repository.save(info);

    }


    @Test
    public void findPostInfoByPeriod() {
        List<PostInfo> res = repository.findPostInfoByPeriod(groupId1, postId, ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                ZonedDateTime.of(2020, 1, 5, 0, 0, 0, 0, ZoneOffset.UTC));
//        List<PostInfo> res = repository.findPostInfoByPeriod2(groupId1);
        Assertions.assertEquals(2, res.size());
    }
}