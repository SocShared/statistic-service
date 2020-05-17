package ml.socshared.bstatistics.domain.db;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="group_")
public class Group {
    @Id
    @Column(name = "group_id")
    String groupId;
    Integer subscribers;
}
