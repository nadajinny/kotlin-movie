package domain.cinema

import domain.Id
import kotlinx.datetime.LocalDate

class MovieTheater(
//    val screens: List<Screen>,
    val movies: List<Movie>,
    val showings: List<Showing>,
) {
    fun findMovie(title: String): Movie? {
        return movies.find { it.title == title }
    }

    fun findMovieById(id: Id): Movie? {
        return movies.find { it.id.value == id.value }
    }

    fun findShowings(
        movie: Movie,
        date: LocalDate,
    ): List<Showing> {
        return showings.filter { it.movie == movie && it.startTime.date == date }
    }
}
