package ml.socshared.bstatistics.domain.db;

import lombok.Data;
import lombok.experimental.Accessors;
import ml.socshared.bstatistics.service.impl.Util;
import org.hibernate.annotations.Generated;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Entity
@Table(name = "group_online")
@Data
@Accessors(chain = true)
public class GroupOnline {
    @Id
    @GeneratedValue
    Integer id;
    @Column(name="group_id", nullable = false)
    String groupId;
    @Column(nullable = false)
    ZonedDateTime timeAddedRecord = Util.timeUtc();
    @Column(nullable =  false)
    Integer online;
    @Column(nullable = false)
    Integer subscribers;
}
