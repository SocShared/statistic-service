package ml.socshared.bstatistics.domain.rabbitmq.response;

import lombok.Data;
import ml.socshared.bstatistics.domain.rabbitmq.RabbitMqType;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RabbitMqResponseAll {
    RabbitMqType type;
    UUID systemUserId;
    String groupId;
    String postId;
    SocialNetwork socialNetwork;
    private String userId; //Id пользователя, который сделал пост
    private String message;
    private String adapterId = "vk";
    private Integer commentsCount;
    private Integer likesCount = 0;
    private Integer repostsCount = 0;
    private Integer viewsCount = 0;
    private Long dateTime;

    Integer membersCount;
    Integer membersOnline;
}
