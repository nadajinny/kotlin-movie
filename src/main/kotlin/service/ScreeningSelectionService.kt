package service

import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.Screening
import domain.reservation.Cart
import kotlinx.datetime.LocalDate
import util.ErrorMessage

class ScreeningSelectionService(
    private val movieTheater: MovieTheater,
    private val cart: Cart,
) {
    fun validateDate(
        movie: Movie,
        input: String,
    ): LocalDate {
        val date = runCatching { LocalDate.parse(input) }.getOrNull()
        require(date != null) { ErrorMessage.INVALID_DATE_FORMAT }

        val screenings = movieTheater.findScreenings(movie, date)
        require(screenings.isNotEmpty()) { ErrorMessage.SCREENING_NOT_FOUND_FOR_DATE }

        return date
    }

    fun selectScreening(
        movie: Movie,
        date: LocalDate,
        input: String,
    ): Screening {
        val screenings = movieTheater.findScreenings(movie, date)

        require(input.toIntOrNull() != null && input.toInt() <= screenings.size) { ErrorMessage.INVALID_SCREENING_NUMBER }

        val screening = screenings[input.toInt() - 1]
        cart.checkReservationHistory(screening)

        return screening
    }
}
