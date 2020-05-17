package ml.socshared.bstatistics.repository;

import ml.socshared.bstatistics.domain.db.Group;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<Group, String> {
}
