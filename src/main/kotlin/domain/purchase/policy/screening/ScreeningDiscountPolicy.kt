package domain.purchase.policy.screening

import kotlinx.datetime.LocalDateTime

interface ScreeningDiscountPolicy {
    fun apply(
        price: Int,
        date: LocalDateTime,
    ): Int
}
