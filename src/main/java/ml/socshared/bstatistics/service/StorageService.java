package ml.socshared.bstatistics.service;

import ml.socshared.bstatistics.domain.db.Post;

import java.time.LocalDateTime;
import java.util.List;

public interface StorageService {
    List<Post> getPostNotOlderThat(LocalDateTime time);
}
