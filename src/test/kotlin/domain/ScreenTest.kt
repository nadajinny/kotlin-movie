package domain

import util.ErrorMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ScreenTest {
    @Test
    fun `좌석 정보가 알파벳 숫자 형식이 아니면 예외가 발생한다`() {
        val seat = "11"

        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.screens.first().findAvailableSeat(seat)
        }

        assertEquals(ErrorMessage.INVALID_SEAT_INPUT, exception.message)
    }

    @Test
    fun `해당 상영관에 존재하지 않는 좌석이면 예외가 발생한다`() {
        val seat = "D1"

        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.screens.first().findAvailableSeat(seat)
        }

        assertEquals(ErrorMessage.SEAT_NOT_FOUND, exception.message)
    }

    @Test
    fun `이미 예약된 좌석이면 예외가 발생한다`() {
        val seat = "A1"

        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.screens.first().findAvailableSeat(seat)
        }

        assertEquals(ErrorMessage.SEAT_ALREADY_RESERVED, exception.message)
    }
}
