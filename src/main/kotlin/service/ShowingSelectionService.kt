package service

import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.Showing
import domain.reservation.Cart
import kotlinx.datetime.LocalDate
import util.ErrorMessage

class ShowingSelectionService(
    private val movieTheater: MovieTheater,
    private val cart: Cart,
) {
    fun validateDate(
        movie: Movie,
        input: String,
    ): LocalDate {
        val date = runCatching { LocalDate.parse(input) }.getOrNull()
        require(date != null) { ErrorMessage.INVALID_DATE_FORMAT }

        val showings = movieTheater.findShowings(movie, date)
        require(showings.isNotEmpty()) { ErrorMessage.SHOWING_NOT_FOUND_FOR_DATE }

        return date
    }

    fun selectShowing(
        movie: Movie,
        date: LocalDate,
        input: String,
    ): Showing {
        val showings = movieTheater.findShowings(movie, date)

        require(input.toIntOrNull() != null && input.toInt() <= showings.size) { ErrorMessage.INVALID_SHOWING_NUMBER }

        val showing = showings[input.toInt() - 1]
        cart.checkReservationHistory(showing)

        return showing
    }
}
