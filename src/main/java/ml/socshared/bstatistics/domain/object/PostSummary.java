package ml.socshared.bstatistics.domain.object;

import lombok.Data;

@Data
public class PostSummary{
    String groupId;
    String postId;
    Integer numberComments;
    Integer numberReposts;
    Integer numberLikes;
    Integer numberViews;
    Double engagementRate;

    public PostSummary() {
        groupId = "";
        postId = "";
        numberComments = 0;
        numberReposts = 0;
        numberLikes = 0;
        numberViews = 0;
        engagementRate = 0.0;
    }
}
