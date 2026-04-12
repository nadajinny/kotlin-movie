package domain

import domain.purchase.PaymentMethod
import util.ErrorMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PaymentMethodTest {
    @Test
    fun `유효한 결제 수단 번호가 들어오면 해당 결제 수단을 반환한다`() {
        val result = PaymentMethod.from("1")

        assertEquals(PaymentMethod.CARD, result)
    }

    @Test
    fun `유효하지 않은 결제 수단 번호가 들어오면 예외가 발생한다`() {
        val exception = assertThrows<IllegalArgumentException> {
            PaymentMethod.from("3")
        }

        assertEquals(ErrorMessage.INVALID_PAYMENT_METHOD, exception.message)
    }
}
