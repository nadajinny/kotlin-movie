package controller

import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.Showing
import domain.reservation.Reservation
import domain.reservation.ReservationInfo
import domain.seat.Seat
import kotlinx.datetime.LocalDate
import view.InputView
import view.OutputView

class ReservationController(
    val movieTheater: MovieTheater,
    val reservationInfos: List<ReservationInfo>,
) {
    fun run(): Pair<Showing, List<Seat>> {
        val movie = chooseMovie()
        val date = chooseDate(movie)
        val showing = chooseShowingTime(movie, date)
        val seats = chooseSeat(showing)

        return showing to seats
    }

    fun chooseMovie(): Movie {
        val input = InputView.readMovieTitle()
        val movie = movieTheater.findMovie(input)

        require(movie != null) { "존재하지 않는 영화입니다." }

        return movie
    }

    fun chooseDate(movie: Movie): LocalDate {
        val input = InputView.readDate()

        val date = runCatching { LocalDate.parse(input) }.getOrNull()
        require(date != null) { "올바른 날짜 형식이 아닙니다. (YYYY-MM-DD)" }

        val showings = movieTheater.findShowings(movie, date)
        require(showings.isNotEmpty()) { "해당 날짜에 선택한 영화의 상영이 없습니다." }

        return date
    }

    fun chooseShowingTime(
        movie: Movie,
        date: LocalDate,
    ): Showing {
        val showings = movieTheater.findShowings(movie, date)

        OutputView.printShowing(showings)
        val input = InputView.readShowingNumber()

        require(input.toIntOrNull() != null && input.toInt() <= showings.size) { "선택하신 상영 번호는 없는 상영 번호입니다." }

        Reservation.checkReservationHistory(reservationInfos, showings[input.toInt() - 1])

        return showings[input.toInt() - 1]
    }

    fun chooseSeat(showing: Showing): List<Seat> {

        val screen = showing.screen

        OutputView.printSeats(screen)

        val input = InputView.readSeat()

        val seatInputs = input.split(',').map { it.trim() }

        val seats = seatInputs.map(screen::findAvailableSeat)
        return seats
    }
}
