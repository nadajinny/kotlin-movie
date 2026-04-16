package api

import controller.CartController
import controller.PaymentController
import domain.Id
import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.ScreeningSchedule
import domain.reservation.Cart
import domain.seat.SeatCoordinate
import domain.user.User
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import org.springframework.stereotype.Service
import persistence.CinemaDatabase
import util.ErrorMessage
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicLong

@Service
class MovieApiService(
    private val cinemaDatabase: CinemaDatabase,
) {
    private val cartController = CartController()
    private val paymentController = PaymentController()

    fun getMovies(): MoviesResponse {
        val movieTheater = cinemaDatabase.loadMovieTheater()
        val catalog = ApiCatalog.from(movieTheater)

        return MoviesResponse(
            movies =
                movieTheater.movies.map { movie ->
                    MovieResponse(
                        id = catalog.movieIdOf(movie),
                        title = movie.title,
                        runningTimeMinutes = movie.runningTime,
                        screenings =
                            catalog.screeningsOf(movie).map { screening ->
                                ScreeningResponse(
                                    id = catalog.screeningIdOf(screening),
                                    startAt = screening.startTime.formatForApi(),
                                    endAt = screening.endTime.formatForApi(),
                                )
                            },
                    )
                },
        )
    }

    fun createReservations(request: CreateReservationsRequest): CreateReservationsResponse {
        validate(request)

        val movieTheater = cinemaDatabase.loadMovieTheater()
        val catalog = ApiCatalog.from(movieTheater)
        val reservationResponses = mutableListOf<ReservationResponse>()
        var cart = Cart(emptyList())

        request.reservations.forEach { reservationRequest ->
            val screening = catalog.findScreening(reservationRequest.screeningId)
            cart.checkReservationHistory(screening)

            val seats =
                mapToApiException {
                    screening.screen.selectSeats(
                        reservationRequest.seats.map(::parseSeatCoordinate),
                    )
                }
            cart = cartController.addAllReservationInfo(cart, screening, seats)
            reservationResponses +=
                ReservationResponse(
                    screeningId = reservationRequest.screeningId,
                    seats = seats.map { "${it.coordinate.row}${it.coordinate.column}" },
                )
        }

        val user = User(Id("user-api"))
        var receipt = paymentController.createReceipt(cart)
        receipt = mapToApiException { receipt.applyPoint(user, request.usedPoints.toString()) }
        receipt = mapToApiException { receipt.applyPaymentMethod(request.paymentMethod.toDomainPaymentMethod()) }
        paymentController.confirmPayment(user, receipt)
        cinemaDatabase.save(receipt)

        return CreateReservationsResponse(
            reservationId = reservationSequence.incrementAndGet(),
            reservations = reservationResponses,
            usedPoints = receipt.usedPoint,
            paymentMethod = request.paymentMethod,
            totalPrice = receipt.totalPrice(),
        )
    }

    private fun validate(request: CreateReservationsRequest) {
        require(request.reservations.isNotEmpty()) { ErrorMessage.INVALID_INPUT }
        request.reservations.forEach { reservation ->
            require(reservation.seats.isNotEmpty()) { ErrorMessage.INVALID_SEAT_INPUT }
        }
    }

    private fun parseSeatCoordinate(value: String): SeatCoordinate {
        require(value.matches(SEAT_PATTERN)) { ErrorMessage.INVALID_SEAT_INPUT }

        val row = value.first()
        val column = value.substring(1).toIntOrNull()
        require(column != null) { ErrorMessage.INVALID_SEAT_INPUT }

        return SeatCoordinate(row, column)
    }

    private fun <T> mapToApiException(block: () -> T): T =
        try {
            block()
        } catch (exception: IllegalArgumentException) {
            when (exception.message) {
                ErrorMessage.SEAT_ALREADY_RESERVED -> throw SeatAlreadyReservedException(exception.message!!)
                else -> throw ApiBadRequestException(exception.message ?: ErrorMessage.INVALID_INPUT)
            }
        }

    private fun String.toDomainPaymentMethod(): domain.purchase.PaymentMethod =
        when (this) {
            "CREDIT_CARD" -> domain.purchase.PaymentMethod.CARD
            "CASH" -> domain.purchase.PaymentMethod.CASH
            else -> throw IllegalArgumentException(ErrorMessage.INVALID_PAYMENT_METHOD)
        }

    private fun LocalDateTime.formatForApi(): String = toJavaLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    private data class ApiCatalog(
        val movieIdsByMovieId: Map<String, Int>,
        val screeningIdsByKey: Map<ScreeningKey, Int>,
        val screeningsByApiId: Map<Int, ScreeningSchedule>,
        val screeningsByMovieId: Map<String, List<ScreeningSchedule>>,
    ) {
        fun movieIdOf(movie: Movie): Int = movieIdsByMovieId.getValue(movie.id.value)

        fun screeningsOf(movie: Movie): List<ScreeningSchedule> = screeningsByMovieId[movie.id.value].orEmpty()

        fun screeningIdOf(screening: ScreeningSchedule): Int = screeningIdsByKey.getValue(ScreeningKey.from(screening))

        fun findScreening(screeningId: Int): ScreeningSchedule =
            screeningsByApiId[screeningId] ?: throw ScreeningNotFoundException(ErrorMessage.SCREENING_NOT_FOUND)

        companion object {
            fun from(movieTheater: MovieTheater): ApiCatalog {
                val movieIdsByMovieId =
                    movieTheater.movies
                        .mapIndexed { index, movie ->
                            movie.id.value to index + 1
                        }.toMap()
                val screeningsByMovieId =
                    movieTheater.screenings
                        .groupBy { it.movie.id.value }
                        .mapValues { (_, screenings) -> screenings.sortedBy { it.startTime.toString() } }
                val screeningIdsByKey = mutableMapOf<ScreeningKey, Int>()
                val screeningsByApiId = mutableMapOf<Int, ScreeningSchedule>()

                movieTheater.movies.forEach { movie ->
                    screeningsByMovieId[movie.id.value].orEmpty().forEachIndexed { index, screening ->
                        val screeningId = movieIdsByMovieId.getValue(movie.id.value) * 100 + index + 1
                        screeningIdsByKey[ScreeningKey.from(screening)] = screeningId
                        screeningsByApiId[screeningId] = screening
                    }
                }

                return ApiCatalog(
                    movieIdsByMovieId = movieIdsByMovieId,
                    screeningIdsByKey = screeningIdsByKey,
                    screeningsByApiId = screeningsByApiId,
                    screeningsByMovieId = screeningsByMovieId,
                )
            }
        }
    }

    private data class ScreeningKey(
        val movieId: String,
        val screenId: String,
        val startTime: LocalDateTime,
    ) {
        companion object {
            fun from(screening: ScreeningSchedule): ScreeningKey =
                ScreeningKey(
                    movieId = screening.movie.id.value,
                    screenId = screening.screen.id.value,
                    startTime = screening.startTime,
                )
        }
    }

    companion object {
        private val reservationSequence = AtomicLong()
        private val SEAT_PATTERN = Regex("^[A-Z][0-9]+$")
    }
}
