package priv.lee90.learn.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import priv.lee90.learn.util.LeakyBucketRateLimiter;
import reactor.core.publisher.Mono;

/**
 * @author Ethan Lee
 */
@RestController
public class HelloController {

    /**
     * 每秒钟最多处理一个请求的限流器
     */
    private final LeakyBucketRateLimiter rateLimiter = new LeakyBucketRateLimiter(1, 1000);

    @GetMapping("/hello")
    public Mono<String> hello() {
        if (rateLimiter.isAllowed()) {
            return Mono.just("World");
        }

        return Mono.error(new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests. Please Try again later."));
    }

}
