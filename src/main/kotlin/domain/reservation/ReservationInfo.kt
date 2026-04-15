package domain.reservation

import domain.cinema.Screening
import domain.seat.Seat

class ReservationInfo(
    val screening: Screening,
    val seat: Seat,
) {
    fun price(): Int = screening.calculatePrice(seat.grade.price)
}
