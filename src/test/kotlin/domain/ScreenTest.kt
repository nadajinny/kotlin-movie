package domain

import domain.seat.SeatCoordinate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import util.ErrorMessage

class ScreenTest {
    @Test
    fun `해당 상영관에 존재하지 않는 좌석이면 예외가 발생한다`() {
        val seat = SeatCoordinate('D', 1)

        val exception =
            assertThrows<IllegalArgumentException> {
                TestFixtureData.screens.first().findAvailableSeat(seat)
            }

        assertEquals(ErrorMessage.SEAT_NOT_FOUND, exception.message)
    }

    @Test
    fun `이미 예약된 좌석이면 예외가 발생한다`() {
        val seat = SeatCoordinate('A', 1)

        val exception =
            assertThrows<IllegalArgumentException> {
                TestFixtureData.screens.first().findAvailableSeat(seat)
            }

        assertEquals(ErrorMessage.SEAT_ALREADY_RESERVED, exception.message)
    }
}
