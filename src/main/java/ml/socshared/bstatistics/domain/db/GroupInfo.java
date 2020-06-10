package ml.socshared.bstatistics.domain.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "group_online")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GroupInfo {
    @Id
    @GeneratedValue
    Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    GroupTable group;
    @Column(name = "time_add_record", nullable = false)
    LocalDateTime timeAddedRecord = LocalDateTime.now();
    @Column(nullable =  false)
    Integer online;
    @Column(nullable = false)
    Integer subscribers;
}
