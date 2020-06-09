package ml.socshared.bstatistics.domain.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class GroupInfo {
    @Id
    @GeneratedValue
    Integer id;
    @ManyToOne(fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id", nullable = false)
    Group group;
    @Column(nullable = false)
    LocalDateTime timeAddedRecord = LocalDateTime.now();
    @Column(nullable =  false)
    Integer online;
    @Column(nullable = false)
    Integer subscribers;
}
