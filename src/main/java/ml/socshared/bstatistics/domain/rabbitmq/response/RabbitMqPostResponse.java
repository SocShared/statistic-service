package ml.socshared.bstatistics.domain.rabbitmq.response;

import lombok.Data;
import ml.socshared.bstatistics.domain.rabbitmq.RabbitMqType;
import ml.socshared.bstatistics.domain.storage.SocialNetwork;

import java.util.UUID;

@Data
public class RabbitMqPostResponse extends RabbitMqResponse {

    public RabbitMqPostResponse() {
        this.setType(RabbitMqType.POST);
    }

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

}
