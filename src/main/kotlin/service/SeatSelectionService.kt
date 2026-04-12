package service

import domain.cinema.Showing
import domain.seat.Seat

class SeatSelectionService {
    fun selectSeats(
        showing: Showing,
        input: String,
    ): List<Seat> {
        val seatInputs = input.split(',').map { it.trim() }

        return seatInputs.map(showing.screen::findAvailableSeat)
    }
}
