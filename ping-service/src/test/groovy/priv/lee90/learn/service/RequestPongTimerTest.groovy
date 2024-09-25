package priv.lee90.learn.service;

import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension
import priv.lee90.learn.task.RequestPongTask;
import spock.lang.Specification

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit

@ExtendWith(SpringExtension)
@SpringBootTest
class RequestPongTimerTest extends Specification {

    @Mock
    ScheduledExecutorService executor

    @Autowired
    private RequestPongTimer requestPongTimer;

    def "helloPong should schedule task with fixed delay"() {
        given:
        String lockFilePath = "ping.lock"
        String stateFilePath = "pingLockState.txt"

        when:
        requestPongTimer.helloPong()

        then:
        requestPongTimer.executor.scheduleWithFixedDelay(
                new RequestPongTask(lockFilePath, stateFilePath), // 任务对象
                0L, // 初始延迟
                1000L, // 延迟时间
                TimeUnit.MILLISECONDS // 时间单位
        )
        Thread.sleep(3000)
    }

}
