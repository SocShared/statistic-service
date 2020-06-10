package ml.socshared.bstatistics.repository;

import ml.socshared.bstatistics.domain.db.Post;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends CrudRepository<Post, UUID> {

    @Query("SELECT tp FROM Post tp  WHERE tp.dateAddingRecord >= :dateAddingAfter")
    Page<Post> findRecordAddedAfter(@Param("dateAddingAfter") ZonedDateTime dateAdding, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.group.socialId = :socGroupId AND p.group.socialNetwork = :soc " +
            " AND p.socId = :socPostId ")
    Optional<Post> findBySocial(String socGroupId, String socPostId, SocialNetwork soc);

}
