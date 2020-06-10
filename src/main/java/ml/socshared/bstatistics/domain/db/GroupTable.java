package ml.socshared.bstatistics.domain.db;

import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import lombok.Data;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "group_table",
        uniqueConstraints={
            @UniqueConstraint(columnNames = {"social_id", "social_network"})
})
public class GroupTable {
    @Id
    @Column(name = "system_group_id")
    UUID systemGroupId;
    @Column(name = "system_user_id")
    UUID systemUserId;
    @Column(name="social_id")
    String socialId;
    @Column(name="social_network")
    SocialNetwork socialNetwork;
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    Set<GroupInfo> info;
    @OneToMany(mappedBy = "group")
    Set<Post> posts;
}
