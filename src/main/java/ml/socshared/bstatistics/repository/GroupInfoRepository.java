package ml.socshared.bstatistics.repository;

import ml.socshared.bstatistics.domain.db.GroupInfo;
import ml.socshared.bstatistics.domain.object.YoungestTimeRecord;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupInfoRepository extends CrudRepository<GroupInfo, Integer>{

    @Query("SELECT gi FROM GroupInfo gi WHERE (gi.group.systemUserId = :systemUserId) AND (gi.group.systemGroupId = :groupId) AND  (gi.group.socialNetwork = :soc) AND " +
            " (gi.timeAddedRecord >= :begin) AND (gi.timeAddedRecord <= :end) ")
    List<GroupInfo> findBySocialIdBetweenDates(UUID systemUserId,UUID groupId, SocialNetwork soc, LocalDateTime begin,
                                               LocalDateTime end);

    @Query("SELECT " +
                " new ml.socshared.bstatistics.domain.object.YoungestTimeRecord(MAX(go.timeAddedRecord))" +
            " FROM GroupInfo go GROUP BY go.group.systemGroupId, go.group.socialNetwork " +
            " HAVING go.group.systemGroupId = :groupId AND go.group.socialNetwork = :soc ")
    Optional<YoungestTimeRecord> getYoungestTimeOfRecordBySocialId(UUID groupId, SocialNetwork soc);

//    @Query("SELECT " +
//            " new java.lang.Long(SUM(go.subscribers))" +
//            " FROM GroupInfo go GROUP BY go.group.socialId, go.group.socialNetwork " +
//            "HAVING go.group.socialId = :groupId AND go.group.socialNetwork = :soc")
//    Optional<Long> getNumberSubscribersBySocialId(String groupId, SocialNetwork soc);

}
