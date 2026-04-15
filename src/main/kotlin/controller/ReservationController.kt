package controller

import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.Screening
import domain.reservation.Cart
import domain.seat.Seat
import kotlinx.datetime.LocalDate
import service.MovieSelectionService
import service.ScreeningSelectionService
import service.SeatSelectionService
import util.retryOnInvalidInput
import view.InputView
import view.OutputView

class ReservationController(
    val movieTheater: MovieTheater,
) {
    private val movieSelectionService = MovieSelectionService(movieTheater)
    private val screeningSelectionService = ScreeningSelectionService(movieTheater)
    private val seatSelectionService = SeatSelectionService()

    fun run(cart: Cart): Pair<Screening, List<Seat>> {
        val movie = retryOnInvalidInput(OutputView::printError) { chooseMovie() }
        val date = retryOnInvalidInput(OutputView::printError) { chooseDate(movie) }
        val screening = retryOnInvalidInput(OutputView::printError) { chooseScreening(cart, movie, date) }
        val seats = retryOnInvalidInput(OutputView::printError) { chooseSeat(screening) }

        return screening to seats
    }

    fun chooseMovie(): Movie {
        val input = InputView.readMovieTitle()
        return movieSelectionService.selectByTitle(input)
    }

    fun chooseDate(movie: Movie): LocalDate {
        val input = InputView.readDate()
        return screeningSelectionService.validateDate(movie, input)
    }

    fun chooseScreening(
        cart: Cart,
        movie: Movie,
        date: LocalDate,
    ): Screening {
        val screenings = movieTheater.findScreenings(movie, date)

        OutputView.printScreenings(screenings)
        val input = InputView.readScreeningNumber()

        return screeningSelectionService.selectScreening(cart, movie, date, input)
    }

    fun chooseSeat(screening: Screening): List<Seat> {
        OutputView.printSeats(screening.screen)

        val coordinates = InputView.readSeat()
        return seatSelectionService.selectSeats(screening, coordinates)
    }
}
