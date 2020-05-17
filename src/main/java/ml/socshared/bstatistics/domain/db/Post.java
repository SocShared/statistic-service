package ml.socshared.bstatistics.domain.db;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Data
public class Post {
    @EmbeddedId
    PostId id;
    Integer views;
    Integer comments;
    Integer shares;
    Integer likes;
}
