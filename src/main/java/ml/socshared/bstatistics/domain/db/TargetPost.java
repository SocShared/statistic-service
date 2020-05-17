package ml.socshared.bstatistics.domain.db;

import lombok.Data;
import ml.socshared.bstatistics.service.impl.Util;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "target_post")
public class TargetPost {
    @Id
    @Column(name = "id")
    @GeneratedValue
    Integer id;
    @Column(name="group_id", nullable = false)
    String groupId;
    @Column(name="post_id", nullable = false)
    String postId;
    @Column(name="date_adding_record", nullable = false)
    ZonedDateTime dateAddingRecord = Util.timeUtc();
}
