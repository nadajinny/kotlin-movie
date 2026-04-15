package domain.purchase.policy.screening

import kotlinx.datetime.LocalDateTime

class MovieDayDiscountPolicy : ScreeningDiscountPolicy {
    override fun apply(
        price: Int,
        date: LocalDateTime,
    ): Int =
        if (date.day in MOVIE_DAYS) {
            ((1 - DISCOUNT_PERCENT) * price).toInt()
        } else {
            price
        }

    companion object {
        private val MOVIE_DAYS = listOf(10, 20, 30)
        const val DISCOUNT_PERCENT = 0.1
    }
}
