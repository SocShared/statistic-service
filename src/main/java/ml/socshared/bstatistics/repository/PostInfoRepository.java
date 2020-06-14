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
import java.util.UUID;


@Repository
public interface PostInfoRepository extends CrudRepository<PostInfo, Integer> {

    @Query("SELECT pi FROM PostInfo pi WHERE (pi.post.group.systemGroupId = :groupId) AND (pi.post.socId = :postId) " +
            "AND (pi.post.group.socialNetwork = :soc)")
    List<PostInfo> findPostInfoByGroupIdAndPostId(UUID groupId, String postId, SocialNetwork soc);

    @Query(" Select info FROM PostInfo info " +
            " WHERE (info.post.group.systemUserId = :systemUserId) AND (info.post.group.systemGroupId = :groupId and info.post.systemPostId = :postId and info.post.group.socialNetwork = :soc) and " +
            " (info.dateAddedRecord >= :begin and info.dateAddedRecord <= :end) ")
    List<PostInfo> findPostInfoByPeriod(UUID systemUserId,
                                        UUID groupId,
                                        UUID postId,
                                        SocialNetwork soc,
                                        LocalDateTime begin,
                                        LocalDateTime end);

    @Query("SELECT " +
            " new ml.socshared.bstatistics.domain.object.YoungestTimeRecord(MAX(pi.dateAddedRecord)) " +
            " FROM  PostInfo pi " +
            " GROUP BY pi.post.group.systemUserId, pi.post.systemPostId, pi.post.group.systemGroupId, pi.post.group.socialNetwork " +
            " HAVING pi.post.group.systemUserId = :systemUserId and pi.post.group.systemGroupId = :groupId and pi.post.systemPostId = :postId and pi.post.group.socialNetwork = :soc")
    Optional<YoungestTimeRecord> getTimeOfYoungestRecord(UUID systemUserId, UUID groupId, UUID postId, SocialNetwork soc);
}
