package priv.lee90.learn.service


import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.slf4j.Logger
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

import java.util.concurrent.Callable
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadPoolExecutor

class RequestPongServiceTest extends Specification {

    @Mock
    Logger logger
    @Mock
    WebClient webClient

    RequestPongService requestPongService

    def setup() {
        MockitoAnnotations.openMocks(this)
        def webClientBuilder = WebClient.builder()
        requestPongService = new RequestPongService(webClientBuilder, "ping.lock", "pingLockState.txt", "http://localhost:8080/hello");
        webClient = webClientBuilder.baseUrl("http://localhost:8080/hello").build()
    }

    def "test request Pong"() {
        when:
        requestPongService.requestPong()

        then:
        noExceptionThrown()
    }

    def "test 3 request in the same time"() {
        when:
        ThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(3)
        def submit = threadPoolExecutor.submit(new Callable<String>() {
            @Override
            String call() throws Exception {
                requestPongService.requestPong()
                return true
            }
        })
        def submit1 = threadPoolExecutor.submit(new Callable<String>() {
            @Override
            String call() throws Exception {
                requestPongService.requestPong()
                return true
            }
        })
        def submit2 = threadPoolExecutor.submit(new Callable<String>() {
            @Override
            String call() throws Exception {
                requestPongService.requestPong()
                return true
            }
        })
        submit.get()
        submit1.get()
        submit2.get()

        then:
        thrown(java.util.concurrent.ExecutionException)
    }
}
