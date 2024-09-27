package priv.lee90.learn.task

import org.mockito.Mock
import org.mockito.MockitoAnnotations
import spock.lang.Specification

import java.util.concurrent.ScheduledExecutorService

class RequestPongTimerTest extends Specification {
    @Mock
    ScheduledExecutorService executor
//    @InjectMocks
    RequestPongTimer requestPongTimer

    def setup() {
        MockitoAnnotations.openMocks(this)
        requestPongTimer = new RequestPongTimer("ping.lock", "pingLockState.txt", "http://localhost:8080/hello")
        requestPongTimer.executor = executor
    }

    def "test init"() {
        when:
        requestPongTimer.init()

        then:
        noExceptionThrown()
    }

    def "test destroy"() {
        when:
        requestPongTimer.destroy()

        then:
        noExceptionThrown()
    }

    def "test hello Pong"() {
        when:
        requestPongTimer.helloPong()
        sleep(2000)

        then:
        noExceptionThrown()
    }
}

