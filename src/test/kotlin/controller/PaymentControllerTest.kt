package controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import util.ErrorMessage
import java.io.ByteArrayInputStream

class PaymentControllerTest {
    val controller =
        PaymentController(
            cart = TestFixtureData.cart,
            user = TestFixtureData.users.first(),
        )

    @Test
    fun `좌석 별로 무비데이 할인(10%)과 시간 할인(2,000원)이 적용된다`() {
        // given & when : 무비데이 할인과 시간 할인을 적용한다.
        val result = controller.discountPerSeat()

        // then : 할인된 금액이 반환된다.
        assertThat(result).isEqualTo(27_700)
    }

    @Test
    fun `사용할 포인트가 보유 포인트보다 크면 예외가 발생한다`() {
        // given : 사용자의 포인트보다 더 큰 포인트가 입력된다.
        val input = "3000"
        val price = 27_700
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 포인트를 처리하면
        val exception =
            assertThrows<IllegalArgumentException> {
                controller.getUserPoint(price)
            }

        // then : 예외가 발생한다.
        assertEquals(ErrorMessage.POINT_DEDUCTION_EXCEEDS_BALANCE, exception.message)
    }

    @Test
    fun `결제 수단 입력이 유효하지 않으면 예외가 발생한다`() {
        // given : 3을 입력한다
        val input = "3"
        val price = 27_700
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 포인트를 처리하면
        val exception =
            assertThrows<IllegalArgumentException> {
                controller.getPaymentMethod(price)
            }

        // then : 예외가 발생한다.
        assertEquals(ErrorMessage.INVALID_PAYMENT_METHOD, exception.message)
    }

    @Test
    fun `결제 수단 할인(신용카드 5%, 현금 2%)이 적용된다`() {
        // given : 결제 수단으로 신용카드가 제시된다.
        val input = "1"
        val price = 10_000
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 결제 수단을 적용하면
        val result = controller.getPaymentMethod(price)

        // then : 할인된 금액이 반환된다.
        assertThat(result).isEqualTo(9_500)
    }

    @Test
    fun `모든 할인이 순서대로 적용된 최종 금액을 계산한다`() {
        // given : 각각의 입력값을 받는다.
        val input = "2000\n1\nY"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 결제를 적용하면
        val result = controller.run()

        // then : 할인된 총 금액이 반환된다.
        assertThat(result).isEqualTo(24_415 to 2000)
    }

    @Test
    fun `포인트 입력 단계에서는 실제 포인트가 차감되지 않는다`() {
        val input = "500"
        val price = 27_700
        val user = TestFixtureData.users.first()
        val controller =
            PaymentController(
                cart = TestFixtureData.cart,
                user = user,
            )
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        controller.getUserPoint(price)

        assertThat(user.point.value).isEqualTo(2_000)
    }

    @Test
    fun `결제 확정 시 포인트가 실제로 차감된다`() {
        val user = TestFixtureData.users[1]
        val controller =
            PaymentController(
                cart = TestFixtureData.cart,
                user = user,
            )

        controller.confirmPayment(500)

        assertThat(user.point.value).isEqualTo(1_500)
    }

    @Test
    fun `결제 진행 중 잘못된 입력이 들어오면 올바른 입력이 들어올 때까지 다시 입력받는다`() {
        val user = TestFixtureData.users[2]
        val controller =
            PaymentController(
                cart = TestFixtureData.cart,
                user = user,
            )
        val input = "abc\n3000\n500\n3\n1"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        val result = controller.run()

        assertThat(result).isEqualTo(25_840 to 500)
    }
}
