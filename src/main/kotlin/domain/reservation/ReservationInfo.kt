package domain.reservation

import domain.cinema.Screening
import domain.seat.Seat

class ReservationInfo(
    val screening: Screening,
    val seat: Seat,
) {
    companion object {
        fun create(
            screening: Screening,
            seat: Seat,
        ): ReservationInfo =
            ReservationInfo(
                screening,
                seat,
            )
    }
}
