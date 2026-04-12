package service

import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.Showing
import domain.reservation.Cart
import kotlinx.datetime.LocalDate

class ShowingSelectionService(
    private val movieTheater: MovieTheater,
    private val cart: Cart,
) {
    fun validateDate(
        movie: Movie,
        input: String,
    ): LocalDate {
        val date = runCatching { LocalDate.parse(input) }.getOrNull()
        require(date != null) { "올바른 날짜 형식이 아닙니다. (YYYY-MM-DD)" }

        val showings = movieTheater.findShowings(movie, date)
        require(showings.isNotEmpty()) { "해당 날짜에 선택한 영화의 상영이 없습니다." }

        return date
    }

    fun selectShowing(
        movie: Movie,
        date: LocalDate,
        input: String,
    ): Showing {
        val showings = movieTheater.findShowings(movie, date)

        require(input.toIntOrNull() != null && input.toInt() <= showings.size) { "선택하신 상영 번호는 없는 상영 번호입니다." }

        val showing = showings[input.toInt() - 1]
        cart.checkReservationHistory(showing)

        return showing
    }
}
