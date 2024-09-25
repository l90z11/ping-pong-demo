package priv.lee90.learn.service;

import org.springframework.stereotype.Component;
import priv.lee90.learn.task.RequestPongTask;

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

    private ScheduledExecutorService executor = null;

    @PostConstruct
    public void init() {
        executor = new ScheduledThreadPoolExecutor(2);
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
        String lockFilePath = "ping.lock";
        String stateFilePath = "pingLockState.txt";
        executor.scheduleWithFixedDelay(
                new RequestPongTask(lockFilePath, stateFilePath),
                0,
                1000,
                TimeUnit.MILLISECONDS);
    }

}
