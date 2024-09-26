package priv.lee90.learn.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import priv.lee90.learn.util.FileLockRateLimiter;
import reactor.core.publisher.Mono;


@Service
public class RequestPongService {

    private final Logger logger = LoggerFactory.getLogger(RequestPongService.class);
    private final WebClient webClient;
    @Value("${lockFilePath:ping.lock}")
    private final String lockFilePath;
    @Value("${stateFilePath:pingLockState.txt}")
    private final String stateFilePath;

    public RequestPongService(WebClient.Builder webClientBuilder,
                              @Value("${lockFilePath:ping.lock}")String lockFilePath,
                              @Value("${stateFilePath:pingLockState.txt}") String stateFilePath,
                              @Value("${apiPath:http://localhost:8080/hello}") String apiPath) {
        this.webClient = webClientBuilder.baseUrl(apiPath).build();
        this.lockFilePath = lockFilePath;
        this.stateFilePath = stateFilePath;
    }

    public void requestPong() {
        FileLockRateLimiter rateLimiter = new FileLockRateLimiter(lockFilePath, stateFilePath, 2);
        if (!rateLimiter.tryAcquire()) {
            logger.info("Request not send as being \"rate limited\"");
            return;
        }

        WebClient.ResponseSpec responseSpec = webClient.get().retrieve();
        Mono<ResponseEntity<String>> responseBody = responseSpec
                .onStatus(HttpStatus::is4xxClientError, response -> {
                    if (response.statusCode().equals(HttpStatus.TOO_MANY_REQUESTS)) {
                        logger.info("Request not send as being \"rate limited\"");
                    }
                    return Mono.error(new RuntimeException(response.statusCode().value() + " : " + response.statusCode().getReasonPhrase()));
                })
                .toEntity(String.class)
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        HttpStatus statusCode = responseBody.block().getStatusCode();

        if (HttpStatus.OK.equals(statusCode)) {
            logger.info("Request sent & Pong Respond");
        }

    }

}
