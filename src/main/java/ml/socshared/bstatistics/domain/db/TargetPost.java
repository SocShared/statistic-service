package ml.socshared.bstatistics.domain.db;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "target_post")
public class TargetPost {
    @Id
    @Column(name = "id")
    @GeneratedValue
    Integer id;
    @Column(name="group_id")
    String groupId;
    @Column(name="post_id")
    String postId;
    @Column(name="date_adding_record")
    LocalDate dateAddingRecord = LocalDate.now();
}
