package api

data class MoviesResponse(
    val movies: List<MovieResponse>,
)

data class MovieResponse(
    val id: Int,
    val title: String,
    val runningTimeMinutes: Int,
    val screenings: List<ScreeningResponse>,
)

data class ScreeningResponse(
    val id: Int,
    val startAt: String,
    val endAt: String,
)

data class CreateReservationsRequest(
    val reservations: List<ReservationRequest>,
    val usedPoints: Int,
    val paymentMethod: String,
)

data class ReservationRequest(
    val screeningId: Int,
    val seats: List<String>,
)

data class CreateReservationsResponse(
    val reservationId: Long,
    val reservations: List<ReservationResponse>,
    val usedPoints: Int,
    val paymentMethod: String,
    val totalPrice: Int,
)

data class ReservationResponse(
    val screeningId: Int,
    val seats: List<String>,
)

data class ErrorResponse(
    val message: String,
)
