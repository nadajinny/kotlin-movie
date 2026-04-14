package service

import domain.cinema.Screening
import domain.seat.Seat

class SeatSelectionService {
    fun selectSeats(
        screening: Screening,
        input: String,
    ): List<Seat> {
        val seatInputs = input.split(',').map { it.trim() }

        return seatInputs.map(screening.screen::findAvailableSeat)
    }
}
