package priv.lee90.learn.controller

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification
import spock.lang.Title

@Title("HelloController Test")
@ExtendWith(SpringExtension)
@WebFluxTest(HelloController)
class HelloControllerTest extends Specification {

    @Autowired
    WebTestClient webTestClient

    def "hello endpoint returns 'World' with status 200"() {
        when:
        def response = webTestClient.get()
                .uri("/hello")
                .exchange()

        then:
        response.expectStatus().isOk()
        response.expectBody(String).isEqualTo("World")
    }

    def "hello endpoint response status 429"() {
        when:
        def resp = webTestClient.get()
                .uri("/hello")
                .exchange()
        def response = webTestClient.get()
                .uri("/hello")
                .exchange()

        then:
        response.expectStatus().is4xxClientError()
    }
}
