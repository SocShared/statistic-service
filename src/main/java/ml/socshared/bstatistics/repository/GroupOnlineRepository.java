package ml.socshared.bstatistics.repository;

import ml.socshared.bstatistics.domain.db.GroupOnline;
import ml.socshared.bstatistics.domain.db.PostInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface GroupOnlineRepository extends CrudRepository<GroupOnline, Integer>{

    @Query("SELECT gi FROM GroupOnline gi WHERE (gi.groupId = :groupId) AND " +
            "(gi.timeAddedRecord >= :begin) AND (gi.timeAddedRecord <= :end)")
    List<GroupOnline> findBetweenDates(@Param("groupId")String groupId, @Param("begin") LocalDate begin,
                                    @Param("end") LocalDate end);
}
