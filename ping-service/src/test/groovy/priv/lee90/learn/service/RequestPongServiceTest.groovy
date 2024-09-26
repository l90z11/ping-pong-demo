package priv.lee90.learn.service


import org.mockito.MockitoAnnotations
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

class RequestPongServiceTest extends Specification {
    RequestPongService requestPongService

    def setup() {
        MockitoAnnotations.openMocks(this)
        def webClientBuilder = WebClient.builder()
        requestPongService = new RequestPongService(webClientBuilder, "ping.lock", "pingLockState.txt", "http://localhost:8080/hello");
    }

    def "test request Pong"() {
        when:
        requestPongService.requestPong()

        then:
        noExceptionThrown()
    }
}
