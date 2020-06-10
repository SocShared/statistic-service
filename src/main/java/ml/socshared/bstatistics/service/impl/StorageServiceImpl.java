package ml.socshared.bstatistics.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.bstatistics.client.StorageClient;
import ml.socshared.bstatistics.domain.db.GroupTable;
import ml.socshared.bstatistics.domain.db.Post;
import ml.socshared.bstatistics.domain.storage.GroupPostStatus;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;
import ml.socshared.bstatistics.domain.storage.request.PublicationRequest;
import ml.socshared.bstatistics.domain.storage.response.GroupResponse;
import ml.socshared.bstatistics.domain.storage.response.PublicationResponse;
import ml.socshared.bstatistics.repository.GroupRepository;
import ml.socshared.bstatistics.repository.PostRepository;
import ml.socshared.bstatistics.security.model.TokenObject;
import ml.socshared.bstatistics.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.http.converter.ObjectToStringHttpMessageConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageClient storageClient;
    private final PostRepository targetRep;
    private final GroupRepository groupRep;
    private int PAGE_SIZE_OF_REQUEST = 100;

    @Value("#{tokenGetter.tokenStorageService}")
    TokenObject storageToken;

    private String authStorageToken() {
        return "Bearer " + storageToken.getToken();
    }

    @Override
    public void storageLoadPostNotOlderThat(LocalDateTime time) {

        Page<PublicationResponse> posts = null;
        int i = 0;
        do {
            posts = storageClient.findPostAfterDate(time.toInstant(ZoneOffset.UTC).toEpochMilli(), i, PAGE_SIZE_OF_REQUEST,
                    authStorageToken());
            for(PublicationResponse p : posts) {
                for(GroupPostStatus status : p.getPostStatus()) {
                    Optional<GroupTable> groupOptional = groupRep.findById(status.getGroupId());
                    GroupTable group = null;
                    if(groupOptional.isEmpty()) {
                        GroupResponse gresponse = storageClient.findGroupById(status.getGroupId(), authStorageToken());
                        group = new GroupTable();
                        group.setSystemUserId(p.getUserId());
                        group.setSocialNetwork(gresponse.getSocialNetwork());
                        group.setSystemGroupId(gresponse.getGroupId());
                        if(gresponse.getSocialNetwork() == SocialNetwork.VK) {
                            group.setSocialId(gresponse.getVkId());
                        } else {
                            group.setSocialId(gresponse.getFacebookId());
                        }
                        group = groupRep.save(group);
                    } else {
                        group = groupOptional.get();
                    }
                    Post post = new Post();
                    post.setGroup(group);
                    if(group.getSocialNetwork() == SocialNetwork.VK) {
                        post.setSocId(status.getPostVkId());
                    } else {
                        post.setSocId(status.getGroupFacebookId());
                    }
                    post.setSystemPostId(p.getPublicationId());
                    targetRep.save(post);
                }

            }
            i++;
        } while(i < posts.getTotalPages());
    }

    public Page<Post> getPostNotOlderThat(LocalDateTime time, Pageable pageable) {
        return targetRep.findRecordAddedAfter(time, pageable);
    }
}

