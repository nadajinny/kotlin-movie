package controller

import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.Showing
import domain.reservation.Cart
import domain.seat.Seat
import kotlinx.datetime.LocalDate
import service.MovieSelectionService
import service.SeatSelectionService
import service.ShowingSelectionService
import view.InputView
import view.OutputView

class ReservationController(
    val movieTheater: MovieTheater,
    val cart: Cart,
) {
    private val movieSelectionService = MovieSelectionService(movieTheater)
    private val showingSelectionService = ShowingSelectionService(movieTheater, cart)
    private val seatSelectionService = SeatSelectionService()

    fun run(): Pair<Showing, List<Seat>> {
        val movie = chooseMovie()
        val date = chooseDate(movie)
        val showing = chooseShowingTime(movie, date)
        val seats = chooseSeat(showing)

        return showing to seats
    }

    fun chooseMovie(): Movie {
        val input = InputView.readMovieTitle()
        return movieSelectionService.selectByTitle(input)
    }

    fun chooseDate(movie: Movie): LocalDate {
        val input = InputView.readDate()
        return showingSelectionService.validateDate(movie, input)
    }

    fun chooseShowingTime(
        movie: Movie,
        date: LocalDate,
    ): Showing {
        val showings = movieTheater.findShowings(movie, date)

        OutputView.printShowing(showings)
        val input = InputView.readShowingNumber()

        return showingSelectionService.selectShowing(movie, date, input)
    }

    fun chooseSeat(showing: Showing): List<Seat> {
        OutputView.printSeats(showing.screen)

        val input = InputView.readSeat()
        return seatSelectionService.selectSeats(showing, input)
    }
}
