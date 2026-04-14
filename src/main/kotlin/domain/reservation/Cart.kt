package domain.reservation

import domain.cinema.Screening
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
        screening: Screening,
        seats: List<Seat>,
    ): Cart =
        seats.fold(this) { cart, seat ->
            cart.addInfo(ReservationInfo(screening, seat))
        }

    fun checkReservationHistory(screening: Screening) {
        val history =
            reservationInfos.filter {
                screening.startTime >= it.screening.startTime && screening.startTime <= it.screening.endTime
            }

        require(history.isEmpty()) { ErrorMessage.OVERLAPPING_SCREENING }
    }

    fun getAllReservationInfo(): List<String> =
        reservationInfos
            .groupBy { it.screening }
            .map { (screening, group) ->
                val seats = group.joinToString(", ") { "${it.seat.coordinate.row}${it.seat.coordinate.column}" }
                "- [${screening.movie.title}] ${screening.startTime.toString().replace("T", " ").substring(0, 16)} 좌석: $seats"
            }
}
