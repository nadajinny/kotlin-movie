package domain.cinema

import domain.Id
import kotlinx.datetime.LocalDate

class MovieTheater(
//    val screens: List<Screen>,
    val movies: List<Movie>,
    val screenings: List<Screening>,
) {
    fun findMovie(title: String): Movie? = movies.find { it.title == title }

    fun findMovieById(id: Id): Movie? = movies.find { it.id.value == id.value }

    fun findScreenings(
        movie: Movie,
        date: LocalDate,
    ): List<Screening> = screenings.filter { it.movie == movie && it.startTime.date == date }
}
