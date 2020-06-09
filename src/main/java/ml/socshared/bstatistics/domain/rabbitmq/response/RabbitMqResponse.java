package ml.socshared.bstatistics.domain.rabbitmq.response;

import lombok.Data;
import ml.socshared.bstatistics.domain.rabbitmq.RabbitMqType;

import java.time.LocalDateTime;


@Data
public class RabbitMqResponse {
    RabbitMqType type;
    LocalDateTime dateTime;
}
