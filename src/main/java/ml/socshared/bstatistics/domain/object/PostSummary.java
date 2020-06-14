package ml.socshared.bstatistics.domain.object;

import lombok.Data;

import java.util.UUID;

@Data
public class PostSummary{
    UUID systemGroupId;
    UUID systemPostId;
    Integer numberComments;
    Integer numberReposts;
    Integer numberLikes;
    Integer numberViews;
    Double engagementRate;
}
