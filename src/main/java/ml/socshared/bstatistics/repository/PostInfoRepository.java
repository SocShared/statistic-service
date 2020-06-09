package ml.socshared.bstatistics.repository;

import ml.socshared.bstatistics.domain.db.PostInfo;
import ml.socshared.bstatistics.domain.object.YoungestTimeRecord;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface PostInfoRepository extends CrudRepository<PostInfo, Integer> {

    @Query("SELECT pi FROM PostInfo pi WHERE (pi.groupId = :groupId) AND (pi.postId = :postId) AND (pi.socialNetwork = :soc)")
    List<PostInfo> findPostInfoByGroupIdAndPostId(String groupId, String postId, SocialNetwork soc);

    @Query(" Select info FROM PostInfo info " +
            " WHERE (info.groupId = :groupId and info.postId = :postId and info.socialNetwork = :soc) and " +
            " (info.dateAddedRecord >= :begin and info.dateAddedRecord <= :end) ")
    List<PostInfo> findPostInfoByPeriod(String groupId,
                                        String postId,
                                        SocialNetwork soc,
                                        LocalDateTime begin,
                                        LocalDateTime end);

    @Query("SELECT " +
            " new ml.socshared.bstatistics.domain.object.YoungestTimeRecord(MAX(pi.dateAddedRecord)) " +
            " FROM  PostInfo pi " +
            " GROUP BY pi.postId, pi.groupId, pi.socialNetwork " +
            " HAVING pi.groupId = :groupId and pi.postId = :postId and pi.socialNetwork = :soc")
    Optional<YoungestTimeRecord> getTimeOfYoungestRecord(String groupId, String postId, SocialNetwork soc);
}
