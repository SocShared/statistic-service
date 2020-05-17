package ml.socshared.bstatistics.domain.db;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostId implements Serializable {
    @Column(name = "group_id")
    String groupId;
    @Column(name = "post_id")
    String postId;
}
