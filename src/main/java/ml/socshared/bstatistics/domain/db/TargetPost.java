package ml.socshared.bstatistics.domain.db;

import lombok.Data;

import java.util.Date;

@Data
public class TargetPost {
    String groupId;
    String postId;
    Date dateAddRecord;
}
