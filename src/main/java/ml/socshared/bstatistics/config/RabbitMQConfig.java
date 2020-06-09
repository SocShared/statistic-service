package ml.socshared.bstatistics.config;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.bstatistics.exception.AbstractRestHandleableException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    public final static String QUEUE_BSTAT_REQUEST_NAME = "socshared-bstat-request-queue";
    public final static String QUEUE_BSTAT_RESPONSE_NAME = "socshared-bstat-response-queue";
    public final static String BSTAT_REQUEST_EXCHANGE_NAME = "socshared-bstat-request";
    public final static String BSTAT_RESPONSE_EXCHANGE_NAME = "socshared-bstat-response";
    public final static String ROUTING_KEY = "12345";



    @Bean
    public TopicExchange appExchangeBStatRequest() {
        return new TopicExchange(BSTAT_REQUEST_EXCHANGE_NAME);
    }

    @Bean
    public TopicExchange appExchangeBStatResponse() {
        return new TopicExchange(BSTAT_RESPONSE_EXCHANGE_NAME);
    }


    @Bean
    public Queue appQueueBStatRequest() { return new Queue(QUEUE_BSTAT_REQUEST_NAME); }

    @Bean
    public Queue appQueueBStatResponse() { return new Queue(QUEUE_BSTAT_RESPONSE_NAME); }


    @Bean
    public Binding declareBindingBStatRequest() {
        return BindingBuilder.bind(appQueueBStatRequest()).to(appExchangeBStatRequest()).with(ROUTING_KEY);
    }

    @Bean
    public Binding declareBindingBStatResponse() {
        return BindingBuilder.bind(appQueueBStatResponse()).to(appExchangeBStatResponse()).with(ROUTING_KEY);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
            SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setErrorHandler(errorHandler());
        return factory;
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new ConditionalRejectingErrorHandler(customExceptionStrategy());
    }

    @Bean
    FatalExceptionStrategy customExceptionStrategy() {
        return new CustomFatalExceptionStrategy();
    }

    @Slf4j
    public static class CustomFatalExceptionStrategy
            extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {

        @Override
        public boolean isFatal(Throwable t) {
            if (t instanceof IOException || t instanceof AbstractRestHandleableException || t instanceof FeignException) {
                log.info(t.getMessage());
                return true;
            }
            return super.isFatal(t);
        }
    }
}
