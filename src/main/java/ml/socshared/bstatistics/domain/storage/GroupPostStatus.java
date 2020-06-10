package ml.socshared.bstatistics.domain.storage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class GroupPostStatus {

    private UUID groupId;
    private PostStatus postStatus;
    private String statusText;
    private SocialNetwork socialNetwork;
    private String postFacebookId;
    private String postVkId;
    private String groupFacebookId;
    private String groupVkId;

    public enum PostStatus {
        @JsonProperty("published")
        PUBLISHED,
        @JsonProperty("awaiting")
        AWAITING,
        @JsonProperty("not_successful")
        NOT_SUCCESSFUL,
        @JsonProperty("processing")
        PROCESSING
    }

}
