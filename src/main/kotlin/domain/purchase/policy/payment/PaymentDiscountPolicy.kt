package domain.purchase.policy.payment

import domain.purchase.PaymentMethod

interface   PaymentDiscountPolicy {
    fun supports(method: PaymentMethod): Boolean

    fun apply(price: Int): Int
}
