package domain.purchase.policy.payment

import domain.purchase.PaymentMethod

class CardPaymentDiscountPolicy : PaymentDiscountPolicy {
    override fun supports(method: PaymentMethod): Boolean = method == PaymentMethod.CARD

    override fun apply(price: Int): Int = ((1 - DISCOUNT_PERCENT) * price).toInt()

    companion object {
        const val DISCOUNT_PERCENT = 0.05
    }
}
