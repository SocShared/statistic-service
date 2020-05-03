package ml.socshared.bstatistics.repository;

import ml.socshared.bstatistics.domain.db.TargetPost;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface TargetPostRepository extends CrudRepository<TargetPost, Integer> {

    @Query("SELECT tp FROM TargetPost tp  WHERE tp.dateAddingRecord >= :dateAddingAfter")
    List<TargetPost> findRecordAddedAfter(@Param("dateAddingAfter") LocalDate dateAdding);

}
