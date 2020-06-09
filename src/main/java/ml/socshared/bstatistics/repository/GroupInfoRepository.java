package ml.socshared.bstatistics.repository;

import ml.socshared.bstatistics.domain.db.GroupInfo;
import ml.socshared.bstatistics.domain.object.YoungestTimeRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupInfoRepository extends CrudRepository<GroupInfo, Integer>{

    @Query("SELECT gi FROM GroupInfo gi WHERE (gi.group.groupId = :groupId) AND " +
            "(gi.timeAddedRecord >= :begin) AND (gi.timeAddedRecord <= :end)")
    List<GroupInfo> findBetweenDates(@Param("groupId")String groupId, @Param("begin") LocalDate begin,
                                     @Param("end") LocalDate end);

    @Query("SELECT " +
                " new ml.socshared.bstatistics.domain.object.YoungestTimeRecord(MAX(go.timeAddedRecord))" +
            " FROM GroupInfo go GROUP BY go.group HAVING go.group.groupId = :groupId")
    Optional<YoungestTimeRecord> getOldestTimeOfRecord(String groupId);

    @Query("SELECT " +
            " new java.lang.Long(SUM(go.subscribers))" +
            " FROM GroupInfo go GROUP BY go.group HAVING go.group.groupId = :groupId")
    Optional<Long> getNumberSubscribers(String groupId);
}
