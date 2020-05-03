package ml.socshared.bstatistics.domain.db;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "group_info")
@Data
public class GroupInfo {
    @Id
    @GeneratedValue
    @Column(name="id")
    Integer id;
    @Column(name="group_id")
    String groupId;
    @Column(name="date_added_record")
    LocalDate dateAddedRecord = LocalDate.now();
    Integer online;
    Integer views;
    Integer share;
    Integer likes;
    Integer comments;
    Integer subscribers;
}
