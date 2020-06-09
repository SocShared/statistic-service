package ml.socshared.bstatistics.client;

import ml.socshared.bstatistics.domain.RestResponsePage;
import ml.socshared.bstatistics.domain.storage.response.PublicationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "StorageApi", url = "${feign.url.storage}")
public interface StorageClient {

    @GetMapping(value = "/private/publications/status/published", produces = MediaType.APPLICATION_JSON_VALUE)
    RestResponsePage<PublicationResponse> findAfter(@RequestParam Long after, @RequestParam Integer page,
                                                           @RequestParam Integer size);
}
