package ml.socshared.bstatistics.repository;

import ml.socshared.bstatistics.domain.db.Post;
import ml.socshared.bstatistics.domain.db.PostId;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PostRepository extends CrudRepository<Post, PostId> {
}
