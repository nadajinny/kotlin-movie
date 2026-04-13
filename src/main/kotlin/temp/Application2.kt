package temp

import domain.Id
import domain.cinema.Movie
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MovieController {
    @GetMapping("/api/movies")
    fun showMovies() : List<Movie> = SampleData.movies
}



@SpringBootApplication
class Application2

fun main(args: Array<String>) {
    runApplication<Application2>(*args)
}