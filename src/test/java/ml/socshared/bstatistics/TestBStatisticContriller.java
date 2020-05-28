package ml.socshared.bstatistics;

import ml.socshared.bstatistics.api.v1.BStatisticApi;
import ml.socshared.bstatistics.controller.BStatisticController;
import ml.socshared.bstatistics.domain.db.Post;
import ml.socshared.bstatistics.domain.db.PostId;
import ml.socshared.bstatistics.domain.db.PostInfo;
import ml.socshared.bstatistics.repository.GroupInfoRepository;
import ml.socshared.bstatistics.repository.PostInfoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//public class TestBStatisticContriller {
//
//    @Autowired
//    MockMvc mockMvc;
//    @Autowired
//    BStatisticApi controller;
//    @Autowired
//    PostInfoRepository postRep;
//    @Autowired
//    GroupInfoRepository groupRep;
//
//    final String groupId = "1";
//    final String postId = "1";
//
//    @BeforeEach
//    void loadData() throws Exception {
//        Post post = new Post(new PostId(groupId, postId), 800, 8, 8, 600);
//        PostInfo info1 = new PostInfo(1, post, ZonedDateTime.now(ZoneOffset.UTC).minusDays(1).minusMinutes(30), 50, 0, 0, 0);
//        PostInfo info2 = new PostInfo(2, post, ZonedDateTime.now(ZoneOffset.UTC).minusDays(1).minusMinutes(20), 200, 2, 150, 2);
//        PostInfo info3 = new PostInfo(3, post, ZonedDateTime.now(ZoneOffset.UTC).minusDays(1).minusMinutes(10), 150, 2, 150, 2);
//        PostInfo info4 = new PostInfo(1, post, ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(30), 50, 0, 0, 0);
//        PostInfo info5 = new PostInfo(2, post, ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(20), 200, 2, 150, 2);
//        PostInfo info6 = new PostInfo(3, post, ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(10), 150, 2, 150, 2);
//
//        postRep.save(info1);
//        postRep.save(info2);
//        postRep.save(info3);
//        postRep.save(info4);
//        postRep.save(info5);
//        postRep.save(info6);
//
//    }
//
//
//
//
//    @Test
//    void testLoadContext() throws Exception {
//        Assertions.assertNotNull(controller);
//    }
//
//    @Test
//    void getTimeSeriesOfPostForOneDay() throws Exception {
//       /* mockMvc.perform(
//                get("/api/v1/groups/{groupId}/posts/{postId}/time_series", groupId, postId)
//                    .param("begin", String.valueOf(LocalDateTime.now().minusDays(1).toEpochSecond(ZoneOffset.UTC)))
//                    .param("end", String.valueOf(LocalDateTime.now().minusDays(1).toEpochSecond(ZoneOffset.UTC)))
//                )
//                .andDo(print())
//                .andExpect(status().isOk());*/
//        mockMvc.perform(
//                get("/api/v1/all", groupId, postId))
//                .andDo(print());
//    }
//}
