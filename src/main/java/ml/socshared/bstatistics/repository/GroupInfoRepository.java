package ml.socshared.bstatistics.repository;

import ml.socshared.bstatistics.domain.db.GroupInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface GroupInfoRepository extends CrudRepository<GroupInfo, Integer>{

    @Query("SELECT gi FROM GroupInfo gi WHERE (gi.groupId = :groupId) AND " +
            "(gi.dateAddedRecord >= :begin) AND (gi.dateAddedRecord <= :end)")
    List<GroupInfo> findBetweenDates(@Param("groupId")String groupId, @Param("begin") LocalDate begin,
                                     @Param("end") LocalDate end);
}
