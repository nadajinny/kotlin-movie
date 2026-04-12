package domain.purchase.policy.showing

import kotlinx.datetime.LocalDateTime

class MovieDayDiscountPolicy : ShowingDiscountPolicy {
    override fun apply(
        price: Int,
        date: LocalDateTime,
    ): Int {
        return if (date.day in MOVIE_DAYS) {
            ((1 - DISCOUNT_PERCENT) * price).toInt()
        } else {
            price
        }
    }

    companion object {
        private val MOVIE_DAYS = listOf(10, 20, 30)
        const val DISCOUNT_PERCENT = 0.1
    }
}
