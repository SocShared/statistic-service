package ml.socshared.bstatistics.repository;

import ml.socshared.bstatistics.domain.db.PostInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostInfoRepository extends CrudRepository<PostInfo, Integer> {
    List<PostInfo> findPostInfoByGroupIdAndPostId(String groupId, String postId);
}
