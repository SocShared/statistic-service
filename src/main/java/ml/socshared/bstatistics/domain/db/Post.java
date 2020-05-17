package ml.socshared.bstatistics.domain.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post")
public class Post {
    @EmbeddedId
    PostId id;
    Integer views;
    Integer comments;
    Integer shares;
    Integer likes;
}
