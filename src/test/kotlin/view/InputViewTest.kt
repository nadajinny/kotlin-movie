package view

import domain.purchase.PaymentMethod
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

    @Test
    fun `유효한 결제 수단 번호가 들어오면 해당 결제 수단을 반환한다`() {
        val result = InputView.parsePaymentMethod("1")

        assertEquals(PaymentMethod.CARD, result)
    }

    @Test
    fun `유효하지 않은 결제 수단 번호가 들어오면 예외가 발생한다`() {
        val exception =
            assertThrows<IllegalArgumentException> {
                InputView.parsePaymentMethod("3")
            }

        assertEquals(ErrorMessage.INVALID_PAYMENT_METHOD, exception.message)
    }
}
