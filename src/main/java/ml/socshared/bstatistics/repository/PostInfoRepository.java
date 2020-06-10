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

    @Query("SELECT pi FROM PostInfo pi WHERE (pi.post.group.socialId = :groupId) AND (pi.post.socId = :postId) " +
            "AND (pi.post.group.socialNetwork = :soc)")
    List<PostInfo> findPostInfoByGroupIdAndPostId(String groupId, String postId, SocialNetwork soc);

    @Query(" Select info FROM PostInfo info " +
            " WHERE (info.post.group.socialId = :groupId and info.post.socId = :postId and info.post.group.socialNetwork = :soc) and " +
            " (info.dateAddedRecord >= :begin and info.dateAddedRecord <= :end) ")
    List<PostInfo> findPostInfoByPeriod(String groupId,
                                        String postId,
                                        SocialNetwork soc,
                                        LocalDateTime begin,
                                        LocalDateTime end);

    @Query("SELECT " +
            " new ml.socshared.bstatistics.domain.object.YoungestTimeRecord(MAX(pi.dateAddedRecord)) " +
            " FROM  PostInfo pi " +
            " GROUP BY pi.post.socId, pi.post.group.socialId, pi.post.group.socialNetwork " +
            " HAVING pi.post.group.socialId = :groupId and pi.post.socId = :postId and pi.post.group.socialNetwork = :soc")
    Optional<YoungestTimeRecord> getTimeOfYoungestRecord(String groupId, String postId, SocialNetwork soc);
}
