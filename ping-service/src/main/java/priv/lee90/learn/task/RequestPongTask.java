package priv.lee90.learn.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import priv.lee90.learn.exception.RateLimitException;
import priv.lee90.learn.util.FileLockRateLimiter;
import reactor.core.publisher.Mono;

/**
 * 请求Pong任务
 * @author lee90
 */
public class RequestPongTask implements Runnable{

    private final Logger logger = LoggerFactory.getLogger(RequestPongTask.class);
    private final WebClient webClient = WebClient.create("http://localhost:8082/hello");

    private String lockFilePath;

    private String stateFilePath;

    public RequestPongTask(String lockFilePath, String stateFilePath) {
        this.lockFilePath = lockFilePath;
        this.stateFilePath = stateFilePath;
    }

    @Override
    public void run() {
        FileLockRateLimiter rateLimiter = new FileLockRateLimiter(lockFilePath, stateFilePath, 2);
        if (rateLimiter.tryAcquire()) {
            request();
        } else {
            logger.info("Request not send as being \"rate limited\"");
        }
    }

    /**
     * 发送请求
     */
    private void request() {
        WebClient.ResponseSpec responseSpec = webClient.get().retrieve();

        try {
            Mono<String> responseBody = responseSpec
                    .onStatus(e -> e.is4xxClientError(), clientResponse -> {
                        int rawStatusCode = clientResponse.rawStatusCode();
//                        logger.info("Received 4xx client error. Status code : {}", rawStatusCode);
                        if (HttpStatus.TOO_MANY_REQUESTS.value() == rawStatusCode) {
//                            logger.info("Request sent & Pong throttled it.");
                            return Mono.error(new RateLimitException("Too many requests. Please Try again later."));
                        }

                        return Mono.error(new RuntimeException("4xx client error"));
                    })
                    .bodyToMono(String.class);
            String result = responseBody.block();
            logger.info("Request sent & Pong Respond \"{}\"", result);
        } catch (Exception e) {
            if (e instanceof RateLimitException) {
                logger.info("Request sent & Pong throttled it.");
            } else {
                logger.error("Request error, The reason is : {}", e.getLocalizedMessage());
            }
        }
    }
}
