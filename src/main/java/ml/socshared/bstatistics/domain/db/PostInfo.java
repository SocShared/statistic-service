package ml.socshared.bstatistics.domain.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ml.socshared.bstatistics.exception.HttpIllegalBodyRequest;
import ml.socshared.bstatistics.service.impl.Util;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name = "post_info")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PostInfo {
    @Id
    @GeneratedValue
    @Column(name="id")
    Integer id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "id_group_id"),
            @JoinColumn(name = "id_post_id")
    })
    Post post;
    @Column(name="date_added_record", nullable = false)
    ZonedDateTime dateAddedRecord;
    Integer views;
    Integer share;
    Integer likes;
    Integer comments;

   public  boolean checkNotNull() {
        return !(views == null || share == null || likes == null || comments == null);
    }
}
