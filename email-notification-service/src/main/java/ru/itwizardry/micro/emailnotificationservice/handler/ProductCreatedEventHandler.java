package ru.itwizardry.micro.emailnotificationservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ru.itwizardry.micro.core.ProductCreatedEvent;
import ru.itwizardry.micro.emailnotificationservice.exception.NonRetryableException;
import ru.itwizardry.micro.emailnotificationservice.exception.RetryableException;

@Component
@KafkaListener(topics = "product-created-events-topic")
public class ProductCreatedEventHandler {

    private RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ProductCreatedEventHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @KafkaHandler
    public void handle(ProductCreatedEvent productCreatedEvent) {
        logger.info("Received event: {}, productId: {}", productCreatedEvent.getTitle(), productCreatedEvent.getProductId());

        String url = "http://localhost:8090/response/200";
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            if (response.getStatusCode().value() == HttpStatus.OK.value()) {
                logger.info("Received response: {}", response.getBody());
            }
        } catch (ResourceAccessException e) {
            logger.error(e.getMessage());
            throw new RetryableException(e);
        } catch (HttpServerErrorException e) {
            logger.error(e.getMessage());
            throw new NonRetryableException(e);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new NonRetryableException(e);
        }

    }
}
