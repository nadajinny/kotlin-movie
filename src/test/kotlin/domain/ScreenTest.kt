package domain

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

        assertEquals("입력된 값이 유효하지 않습니다.", exception.message)
    }

    @Test
    fun `해당 상영관에 존재하지 않는 좌석이면 예외가 발생한다`() {
        val seat = "D1"

        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.screens.first().findAvailableSeat(seat)
        }

        assertEquals("해당 상영관에는 해당 좌석이 존재하지 않습니다.", exception.message)
    }

    @Test
    fun `이미 예약된 좌석이면 예외가 발생한다`() {
        val seat = "A1"

        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.screens.first().findAvailableSeat(seat)
        }

        assertEquals("해당 좌석은 이미 예약되었습니다.", exception.message)
    }
}
