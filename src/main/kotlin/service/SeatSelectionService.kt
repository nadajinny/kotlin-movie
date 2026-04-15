package service

import domain.cinema.Screening
import domain.seat.Seat
import domain.seat.SeatCoordinate

class SeatSelectionService {
    fun selectSeats(
        screening: Screening,
        coordinates: List<SeatCoordinate>,
    ): List<Seat> = coordinates.map(screening.screen::findAvailableSeat)
}
