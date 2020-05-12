package ml.socshared.bstatistics.domain.object;

import lombok.Data;

@Data
public class PostInfoResponse {
    String groupId;
    String postId;
    Integer numberComments;
    Integer numberReposts;
    Integer numberLikes;
    Double engagementRate;
}
