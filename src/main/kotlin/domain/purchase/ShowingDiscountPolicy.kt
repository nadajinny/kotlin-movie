package domain.purchase

import kotlinx.datetime.LocalDateTime

interface ShowingDiscountPolicy {
    fun apply(
        price: Int,
        date: LocalDateTime,
    ): Int
}
