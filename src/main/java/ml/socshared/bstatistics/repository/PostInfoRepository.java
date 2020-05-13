package ml.socshared.bstatistics.repository;

import ml.socshared.bstatistics.domain.db.PostInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface PostInfoRepository extends CrudRepository<PostInfo, Integer> {
    List<PostInfo> findPostInfoByGroupIdAndPostId(String groupId, String postId);

    @Query("Select info FROM PostInfo info WHERE (info.groupId = :groupId and info.postId = :postId) and (info.dateAddedRecord >= :begin and info.dateAddedRecord <= :end)")
    List<PostInfo> findPostInfoByPeriod(@Param("groupId") String groupId,
                                        @Param("postId")  String postId,
                                        @Param("begin")   ZonedDateTime begin,
                                        @Param("end")     ZonedDateTime end);

    @Query("Select info FROM PostInfo info WHERE (info.groupId = :groupId)")
    List<PostInfo> findPostInfoByPeriod2(@Param("groupId") String groupId);
}
