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

@Repository
public interface GroupInfoRepository extends CrudRepository<GroupInfo, Integer>{

    @Query("SELECT gi FROM GroupInfo gi WHERE (gi.group.socialId = :groupId) AND  (gi.group.socialNetwork = :soc) AND " +
            " (gi.timeAddedRecord >= :begin) AND (gi.timeAddedRecord <= :end) ")
    List<GroupInfo> findBySocialIdBetweenDates(@Param("groupId")String groupId, SocialNetwork soc, @Param("begin") LocalDateTime begin,
                                     @Param("end") LocalDateTime end);

    @Query("SELECT " +
                " new ml.socshared.bstatistics.domain.object.YoungestTimeRecord(MAX(go.timeAddedRecord))" +
            " FROM GroupInfo go GROUP BY go.group.socialId, go.group.socialNetwork " +
            " HAVING go.group.socialId = :groupId AND go.group.socialId = :soc ")
    Optional<YoungestTimeRecord> getYoungestTimeOfRecordBySocialId(String groupId, SocialNetwork soc);

//    @Query("SELECT " +
//            " new java.lang.Long(SUM(go.subscribers))" +
//            " FROM GroupInfo go GROUP BY go.group.socialId, go.group.socialNetwork " +
//            "HAVING go.group.socialId = :groupId AND go.group.socialNetwork = :soc")
//    Optional<Long> getNumberSubscribersBySocialId(String groupId, SocialNetwork soc);

}
