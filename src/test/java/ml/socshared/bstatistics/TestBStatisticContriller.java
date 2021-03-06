package ml.socshared.bstatistics;

import ml.socshared.bstatistics.domain.db.Post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
//        POST_INFO info1 = new POST_INFO(1, post, ZonedDateTime.now(ZoneOffset.UTC).minusDays(1).minusMinutes(30), 50, 0, 0, 0);
//        POST_INFO info2 = new POST_INFO(2, post, ZonedDateTime.now(ZoneOffset.UTC).minusDays(1).minusMinutes(20), 200, 2, 150, 2);
//        POST_INFO info3 = new POST_INFO(3, post, ZonedDateTime.now(ZoneOffset.UTC).minusDays(1).minusMinutes(10), 150, 2, 150, 2);
//        POST_INFO info4 = new POST_INFO(1, post, ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(30), 50, 0, 0, 0);
//        POST_INFO info5 = new POST_INFO(2, post, ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(20), 200, 2, 150, 2);
//        POST_INFO info6 = new POST_INFO(3, post, ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(10), 150, 2, 150, 2);
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
