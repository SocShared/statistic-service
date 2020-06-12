package ml.socshared.bstatistics.domain.object;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class SentryPostId {
    UUID groupId;
    UUID postId;
}
