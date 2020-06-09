package ml.socshared.bstatistics.domain.rabbitmq.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ml.socshared.bstatistics.domain.rabbitmq.RabbitMqType;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RabbitMqSocialRequest {
    SocialNetwork socialNetwork;
    RabbitMqType type;
    UUID systemUserId;
    String groupId;
    String postId;
}
