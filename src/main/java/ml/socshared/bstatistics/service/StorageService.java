package ml.socshared.bstatistics.service;

import ml.socshared.bstatistics.domain.db.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface StorageService {
    void storageLoadPostNotOlderThat(LocalDateTime time);
    Page<Post> getPostNotOlderThat(LocalDateTime time, Pageable pageable);
}
