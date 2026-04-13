package temp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class Greeting(
    val id: Long,
    val name: String
) {
    fun sayHello() : String = "Hello, $name!"
}

@RestController
class GreetingController {
    @GetMapping("/greeting")
    fun hello(name: String): Greeting {
        return Greeting(1L, name)
    }
}


@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
