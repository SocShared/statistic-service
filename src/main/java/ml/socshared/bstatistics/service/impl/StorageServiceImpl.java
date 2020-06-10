package ml.socshared.bstatistics.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.bstatistics.client.StorageClient;
import ml.socshared.bstatistics.domain.db.Post;
import ml.socshared.bstatistics.repository.PostRepository;
import ml.socshared.bstatistics.service.StorageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageClient storageClient;
    private final PostRepository targetRep;

    @Override
    public List<Post> getPostNotOlderThat(LocalDateTime time) {
        return null;
    }
}
