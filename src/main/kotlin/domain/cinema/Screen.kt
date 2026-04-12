package domain.cinema

import domain.Id
import domain.seat.Seat
import domain.seat.SeatState

class Screen(val seats: List<Seat>, val id: Id) {
    fun findSeat(
        row: Char,
        column: Int,
    ): Seat? {
        return seats.find { it.coordinate.row == row && it.coordinate.column == column }
    }

    fun findAvailableSeat(input: String): Seat {
        require(Regex("^[A-Z][0-9]+$").matches(input)) { "입력된 값이 유효하지 않습니다." }

        val row = input[0]
        val column = input.substring(1).toInt()
        val seat = findSeat(row, column)

        require(seat != null) { "해당 상영관에는 해당 좌석이 존재하지 않습니다." }
        require(seat.isReserved != SeatState.RESERVED) { "해당 좌석은 이미 예약되었습니다." }

        return seat
    }

    companion object {
        const val MAX_ROW = 5
        const val MAX_COLUMN = 4
    }
}
