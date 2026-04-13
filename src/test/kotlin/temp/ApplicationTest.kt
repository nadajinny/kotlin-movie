package temp

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.servlet.client.RestTestClient


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest(
    @param:LocalServerPort private val port: Int,
) {
    private lateinit var client: RestTestClient

    @BeforeEach
    fun setUp() {
        client = RestTestClient.bindToServer()
            .baseUrl("http://localhost:$port")
            .build()
    }

    @Test
    fun test() {
        println(port)
    }

    @Test
    fun test1() {
        client.get().uri("/greeting")
            .exchange()
            .expectBody()
            .jsonPath("$.name").isEqualTo("Default")
    }

    @Test
    fun test2() {
        client.get().uri("/greeting?name=Ember")
            .exchange()
            .expectBody()
            .jsonPath("$.name").isEqualTo("Ember")
    }
}