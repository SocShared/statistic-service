package ml.socshared.bstatistics.repository;

//import org.junit.Test;


//
//@DataJpaTest
//@ActiveProfiles("test")
//@Slf4j
//public class TargetPostRepositoryTest  {
//
//    @Resource
//    private TargetPostRepository repository;
//    DateTimeFormatter dform;
//    TargetPost tp1;
//    TargetPost tp2;
//    TargetPost tp3;
//
//    @BeforeEach
//    public void startUp() throws ParseException {
//        tp1 = new TargetPost();
//        tp1.setGroupId("1");
//        tp1.setPostId("p1");
//        tp1.setDateAddingRecord(ZonedDateTime.of(2020, 6, 1,0, 0, 0, 0, ZoneOffset.UTC));
//        repository.save(tp1);
//        tp2 = new TargetPost();
//        tp2.setGroupId("2");
//        tp2.setPostId("p2");
//        tp2.setDateAddingRecord(ZonedDateTime.of(2020, 5, 1,0, 0, 0, 0, ZoneOffset.UTC));
//        repository.save(tp2);
//        tp3 = new TargetPost();
//        tp3.setGroupId("3");
//        tp3.setPostId("p3");
//        tp3.setDateAddingRecord(ZonedDateTime.of(2020, 4, 1,0, 0, 0, 0, ZoneOffset.UTC));
//        repository.save(tp3);
//    }
//
//    @Test
//    public void testRequestFindRecordAddedAfter1() throws ParseException {
//        List<TargetPost> res = repository.findRecordAddedAfter(ZonedDateTime.of(2020, 5, 1,0, 0, 0, 0, ZoneOffset.UTC));
//        Assert.assertEquals(2, res.size());
//        Assert.assertTrue(inTargetPost(res, tp1));
//        Assert.assertTrue(inTargetPost(res, tp2));
//    }
//
//    public static boolean inTargetPost(List<TargetPost> list, TargetPost tp) {
//        for(TargetPost el : list) {
//            if(el.equals(tp)) {
//                return true;
//            }
//        }
//        return false;
//    }
//}