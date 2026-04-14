package view

import domain.seat.SeatCoordinate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import util.ErrorMessage

class InputViewTest {
    @Test
    fun `좌석 정보가 알파벳 숫자 형식이 아니면 예외가 발생한다`() {
        val input = "11"

        val exception =
            assertThrows<IllegalArgumentException> {
                InputView.parseSeatCoordinates(input)
            }

        assertEquals(ErrorMessage.INVALID_SEAT_INPUT, exception.message)
    }

    @Test
    fun `쉼표로 구분된 좌석 정보는 좌표 목록으로 변환된다`() {
        val result = InputView.parseSeatCoordinates("A1, B2")

        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(listOf(SeatCoordinate('A', 1), SeatCoordinate('B', 2)))
    }
}
