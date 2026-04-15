package domain

import domain.purchase.PaymentMethod
import domain.purchase.policy.payment.CardPaymentDiscountPolicy
import domain.purchase.policy.payment.CashPaymentDiscountPolicy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PaymentDiscountPolicyTest {
    private val cardPolicy = CardPaymentDiscountPolicy()
    private val cashPolicy = CashPaymentDiscountPolicy()

    @Test
    fun `신용카드 결제 시 5퍼센트 할인된다`() {
        val result = cardPolicy.apply(10_000)

        assertEquals(9_500, result)
    }

    @Test
    fun `현금 결제 시 2퍼센트 할인된다`() {
        val result = cashPolicy.apply(10_000)

        assertEquals(9_800, result)
    }

    @Test
    fun `카드 정책은 카드 결제수단을 지원한다`() {
        val result = cardPolicy.supports(PaymentMethod.CARD)

        assertEquals(true, result)
    }

    @Test
    fun `현금 정책은 현금 결제수단을 지원한다`() {
        val result = cashPolicy.supports(PaymentMethod.CASH)

        assertEquals(true, result)
    }
}
