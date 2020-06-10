package ml.socshared.bstatistics.repository;

import ml.socshared.bstatistics.domain.db.GroupTable;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends CrudRepository<GroupTable, UUID> {

    @Query("SELECT g FROM GroupTable g WHERE g.socialId = :socGroupId AND g.socialNetwork = :soc")
    Optional<GroupTable> findBySocial(String socGroupId, SocialNetwork soc);
}
