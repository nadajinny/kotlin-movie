package domain.reservation

import domain.cinema.Showing
import domain.seat.Seat

class Cart(val reservationInfos: List<ReservationInfo>) {
    fun addInfo(info: ReservationInfo): Cart {
        return Cart(
            reservationInfos.plus(info),
        )
    }

    fun addAll(
        showing: Showing,
        seats: List<Seat>,
    ): Cart {
        return seats.fold(this) { cart, seat ->
            cart.addInfo(ReservationInfo(showing, seat))
        }
    }

    fun checkReservationHistory(showing: Showing) {
        val history = reservationInfos.filter {
            showing.startTime >= it.showing.startTime && showing.startTime <= it.showing.endTime
        }

        require(history.isEmpty()) { "선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요." }
    }

    fun getAllReservationInfo(): List<String> {
        return reservationInfos.groupBy { it.showing }
            .map { (showing, group) ->
                val seats = group.joinToString(", ") { "${it.seat.coordinate.row}${it.seat.coordinate.column}" }
                "- [${showing.movie.title}] ${showing.startTime.toString().replace("T", " ").substring(0, 16)} 좌석: $seats"
            }
    }
}
