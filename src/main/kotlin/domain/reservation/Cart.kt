package domain.reservation

import domain.cinema.Showing
import domain.seat.Seat
import util.ErrorMessage

class Cart(
    val reservationInfos: List<ReservationInfo>,
) {
    fun addInfo(info: ReservationInfo): Cart =
        Cart(
            reservationInfos.plus(info),
        )

    fun addAll(
        showing: Showing,
        seats: List<Seat>,
    ): Cart =
        seats.fold(this) { cart, seat ->
            cart.addInfo(ReservationInfo(showing, seat))
        }

    fun checkReservationHistory(showing: Showing) {
        val history =
            reservationInfos.filter {
                showing.startTime >= it.showing.startTime && showing.startTime <= it.showing.endTime
            }

        require(history.isEmpty()) { ErrorMessage.OVERLAPPING_SHOWING }
    }

    fun getAllReservationInfo(): List<String> =
        reservationInfos
            .groupBy { it.showing }
            .map { (showing, group) ->
                val seats = group.joinToString(", ") { "${it.seat.coordinate.row}${it.seat.coordinate.column}" }
                "- [${showing.movie.title}] ${showing.startTime.toString().replace("T", " ").substring(0, 16)} 좌석: $seats"
            }
}
