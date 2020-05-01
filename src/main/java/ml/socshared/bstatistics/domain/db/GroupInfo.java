package ml.socshared.bstatistics.domain.db;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "group info")
@Data
public class GroupInfo {
    Integer id;
    String group_id;
    Date timestamp;
    Integer online;
    Integer views;
    Integer share;
    Integer likes;
    Integer comments;
    Integer subscribers;
}
