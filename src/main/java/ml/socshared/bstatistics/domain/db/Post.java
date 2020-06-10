package ml.socshared.bstatistics.domain.db;

import lombok.Data;
import ml.socshared.bstatistics.service.impl.Util;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "post")
public class Post {
    @Id
    @Column(name = "system_post_id")
    UUID systemPostId;
    @ManyToOne(cascade = CascadeType.ALL)
    GroupTable group;
    @Column(name = "post_id")
    String socId;
    @OneToMany(mappedBy = "post")
    List<PostInfo> info;
    @Column(name="date_adding_record", nullable = false)
    ZonedDateTime dateAddingRecord = Util.timeUtc();
}
