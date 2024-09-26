package priv.lee90.learn.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import priv.lee90.learn.service.RequestPongService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 请求pong服务的定时器
 * @author lee90
 */
@Component
public class RequestPongTimer {

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RequestPongTimer.class);

    private ScheduledExecutorService executor = null;
    @Value("${lockFilePath:ping.lock}")
    private String lockFilePath;
    @Value("${stateFilePath:pingLockState.txt}")
    private String stateFilePath;
    @Value("${apiPath:http://localhost:8080/hello}")
    private String apiPath;
    @Autowired
    private RequestPongService requestPongService;

    public RequestPongTimer(@Value("${lockFilePath:ping.lock}")String lockFilePath,
                           @Value("${stateFilePath:pingLockState.txt}") String stateFilePath,
                           @Value("${apiPath:http://localhost:8080/hello}") String apiPath) {
        this.lockFilePath = lockFilePath;
        this.stateFilePath = stateFilePath;
        this.apiPath = apiPath;
    }

    @PostConstruct
    public void init() {
        executor = new ScheduledThreadPoolExecutor(1);
    }

    @PreDestroy
    public void destroy() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }

    /**
     * 定时请求pong服务
     */
    public void helloPong() {
        executor.scheduleWithFixedDelay(
            () -> {
                try {
                    requestPongService.requestPong();
                } catch (Exception e) {
                    logger.error("Error occurred while requesting pong service", e);
                }

            },
            0,
            1000,
            TimeUnit.MILLISECONDS);
    }

}
