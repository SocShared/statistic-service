package ml.socshared.bstatistics.client;

import io.swagger.annotations.Authorization;
import ml.socshared.bstatistics.domain.RestResponsePage;
import ml.socshared.bstatistics.domain.storage.response.GroupResponse;
import ml.socshared.bstatistics.domain.storage.response.PublicationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;


@FeignClient(name = "StorageApi", url = "${feign.url.storage}")
public interface StorageClient {

    @GetMapping(value = "api/v1/private/publications/status/published", produces = MediaType.APPLICATION_JSON_VALUE)
    RestResponsePage<PublicationResponse> findPostAfterDate(@RequestParam Long after, @RequestParam Integer page,
                                                    @RequestParam Integer size,
                                                    @RequestHeader("Authorization") String token);

    @GetMapping(value = "api/v1/private/groups/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GroupResponse findGroupById(@PathVariable UUID groupId, @RequestHeader("Authorization") String token);
}
