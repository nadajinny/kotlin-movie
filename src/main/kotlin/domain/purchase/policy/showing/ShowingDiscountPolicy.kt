package domain.purchase.policy.showing

import kotlinx.datetime.LocalDateTime

interface ShowingDiscountPolicy {
    fun apply(
        price: Int,
        date: LocalDateTime,
    ): Int
}
